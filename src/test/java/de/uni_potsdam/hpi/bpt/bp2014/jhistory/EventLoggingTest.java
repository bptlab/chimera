package de.uni_potsdam.hpi.bpt.bp2014.jhistory;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractEvent;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;
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
        assertEquals(0, service.getEventEntries(instance.getScenarioInstanceId()).size());
        ScenarioTestHelper.executeActivityByName("Before Event", instance);
        assertEquals(1, service.getEventEntries(instance.getScenarioInstanceId()).size());
    }

    @Test
    public void testEventReceivingLogging() throws IOException {
        HistoryService service = new HistoryService();
        String path = "src/test/resources/EventScenarios/EventLoggingScenario.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.executeActivityByName("Before Event", instance);
        // Only the registration log should be in the database

        assertEquals(1, service.getEventEntries(instance.getScenarioInstanceId()).size());
        ScenarioTestHelper.triggerEventInScenario(instance, base);

        // After triggering the event there should be another log entry
        assertEquals(2, service.getEventEntries(instance.getScenarioInstanceId()).size());
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
        List<LogEntry> dataattributeEntries =
                service.getDataattributeEntries(instance.getScenarioInstanceId());
        assertEquals(2, dataattributeEntries.size());
        // The change in data attribute has been caused by the event
        assertEquals(eventControlNodeInstanceId,
                dataattributeEntries.get(1).getCause());

        List<LogEntry> eventEntries=
                service.getEventEntries(instance.getScenarioInstanceId());
        assertEquals(eventControlNodeInstanceId,
                eventEntries.get(1).getLoggedId());
    }

    @Test
    public void testEventDataattributeWritingLog() throws IOException {
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
                service.getDataattributeEntries(instance.getScenarioInstanceId());
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
                service.getEventEntries(instance.getScenarioInstanceId());
        assertEquals("received", eventEntries.get(1).getNewValue());
        assertEquals("registered", eventEntries.get(0).getNewValue());

        int eventControlNodeInstanceId = events.get(0).getControlNodeInstanceId();
        assertEquals(eventControlNodeInstanceId, eventEntries.get(0).getLoggedId());
        assertEquals(eventControlNodeInstanceId, eventEntries.get(1).getLoggedId());

        assertEquals("SomeEvent", eventEntries.get(0).getLabel());
        assertEquals("SomeEvent", eventEntries.get(1).getLabel());
    }
}
