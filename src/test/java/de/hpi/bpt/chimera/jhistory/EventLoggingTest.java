package de.hpi.bpt.chimera.jhistory;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.ScenarioTestHelper;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractEvent;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class EventLoggingTest extends JerseyTest {
    @After
    public void tearDown() throws Exception {
        super.tearDown();
        AbstractDatabaseDependentTest.resetDatabase();
    }
    WebTarget base;

    @Override
    protected Application configure() {
        return new ResourceConfig(EventDispatcher.class);
    }

    @Before
    public void setUpBase() {
        base = target("eventdispatcher");
    }


    @Test
    public void testEventRegistrationLogging() throws IOException {
        HistoryService service = new HistoryService();
        String path = "src/test/resources/EventScenarios/EventLoggingScenario.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);

        // Before reaching an event no event should be activated
        assertEquals(0, service.getEventEntries(instance.getId()).size());
        ScenarioTestHelper.executeActivityByName("Before Event", instance);
        assertEquals(1, service.getEventEntries(instance.getId()).size());
    }

    @Test
    public void testEventReceivingLogging() throws IOException {
        HistoryService service = new HistoryService();
        String path = "src/test/resources/EventScenarios/EventLoggingScenario.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.executeActivityByName("Before Event", instance);
        // Only the registration log should be in the database
        assertEquals(1, service.getEventEntries(instance.getId()).size());
        ScenarioTestHelper.triggerEventInScenario(instance, base);
        // After triggering the event there should be another log entry
        assertEquals(2, service.getEventEntries(instance.getId()).size());
    }

    /**
     * Tests whether the the dataattributeChange log entries refer to the event
     * control node id which caused them
     * @throws IOException
     */
    @Test
    public void testEventLogLinking() throws IOException {
        String path = "src/test/resources/EventScenarios/EventLoggingScenario.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.executeActivityByName("Before Event", instance);
        List<AbstractEvent> events =  instance.getEventsForScenarioInstance();
        assert (events.size() == 1): "Event was not registered properly";
        int eventControlNodeInstanceId = events.get(0).getControlNodeInstanceId();

        String json = FileUtils.readFileToString(
                new File("src/test/resources/history/exampleWebserviceJson.json"));
        ScenarioTestHelper.triggerEventInScenario(instance, base, json);

        HistoryService service = new HistoryService();
        List<LogEntry> dataAttributeEntries =
                service.getDataAttributeEntries(instance.getId());
        assertEquals(2, dataAttributeEntries.size());
        // The change in data attribute has been caused by the event
        assertEquals(eventControlNodeInstanceId,
                dataAttributeEntries.get(1).getCause());

        List<LogEntry> eventEntries=
                service.getEventEntries(instance.getId());
        assertEquals(eventControlNodeInstanceId,
                eventEntries.get(1).getLoggedId());
    }

    @Test
    public void testEventDataAttributeWritingLog() throws IOException {
        String path = "src/test/resources/EventScenarios/EventLoggingScenario.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.executeActivityByName("Before Event", instance);
        List<AbstractEvent> events =  instance.getEventsForScenarioInstance();
        assert (events.size() == 1): "Event was not registered properly";
        String json = FileUtils.readFileToString(
                new File("src/test/resources/history/exampleWebserviceJson.json"));
        ScenarioTestHelper.triggerEventInScenario(instance, base, json);

        HistoryService service = new HistoryService();
        List<LogEntry> dataattributeEntries =
                service.getDataAttributeEntries(instance.getId());
        assertEquals(2, dataattributeEntries.size());
    }

    @Test
    public void testEventLogValues() throws IOException {
        String path = "src/test/resources/EventScenarios/EventLoggingScenario.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.executeActivityByName("Before Event", instance);
        List<AbstractEvent> events =  instance.getEventsForScenarioInstance();
        assert (events.size() == 1): "Event was not registered properly";
        String json = FileUtils.readFileToString(
                new File("src/test/resources/history/exampleWebserviceJson.json"));

        ScenarioTestHelper.triggerEventInScenario(instance, base, json);
        HistoryService service = new HistoryService();
        List<LogEntry> eventEntries =
                service.getEventEntries(instance.getId());
        assertEquals("received", eventEntries.get(1).getNewValue());
        assertEquals("registered", eventEntries.get(0).getNewValue());

        int eventControlNodeInstanceId = events.get(0).getControlNodeInstanceId();
        assertEquals(eventControlNodeInstanceId, eventEntries.get(0).getLoggedId());
        assertEquals(eventControlNodeInstanceId, eventEntries.get(1).getLoggedId());

        assertEquals("event", eventEntries.get(0).getLabel());
        assertEquals("event", eventEntries.get(1).getLabel());
    }
}
