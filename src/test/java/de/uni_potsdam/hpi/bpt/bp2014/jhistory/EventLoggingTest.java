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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class EventLoggingTest extends JerseyTest {
    @After
    public void teardown() throws IOException, SQLException {
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
        ScenarioTestHelper.terminateActivityInstanceByName("BeforeEvent", instance);
        assertEquals(1, service.getEventEntries(instance.getScenarioInstanceId()).size());
    }

    @Test
    public void testEventReceivingLogging() throws IOException {
        HistoryService service = new HistoryService();
        String path = "src/test/resources/EventScenarios/EventLoggingScenario.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.terminateActivityInstanceByName("BeforeEvent", instance);
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
        ScenarioTestHelper.terminateActivityInstanceByName("BeforeEvent", instance);
        List<AbstractEvent> events =  instance.getEventsForScenarioInstance();
        assert (events.size() == 1): "Event was not registered properly";
        int eventControlNodeInstanceId = events.get(0).getControlNodeInstanceId();

        String json = FileUtils.readFileToString(
                new File("src/test/resources/history/exampleWebserviceJson.json"));
        ScenarioTestHelper.triggerEventInScenario(instance, base, json);

        HistoryService service = new HistoryService();
        Map<Integer, Map<String, Object>> dataattributeEntries =
                service.getDataattributeEntries(instance.getScenarioInstanceId());
        assertEquals(eventControlNodeInstanceId,
                dataattributeEntries.get(2).get("h.controlnodeinstance_id"));

        Map<Integer, Map<String, Object>> eventEntries=
                service.getEventEntries(instance.getScenarioInstanceId());
        assertEquals(eventControlNodeInstanceId,
                eventEntries.get(2).get("eventid"));
    }

    @Test
    public void testEventDataattributeWritingLog() throws IOException {
        String path = "src/test/resources/EventScenarios/EventLoggingScenario.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.terminateActivityInstanceByName("BeforeEvent", instance);
        List<AbstractEvent> events =  instance.getEventsForScenarioInstance();
        assert (events.size() == 1): "Event was not registered properly";
        String json = FileUtils.readFileToString(
                new File("src/test/resources/history/exampleWebserviceJson.json"));
        ScenarioTestHelper.triggerEventInScenario(instance, base, json);

        HistoryService service = new HistoryService();
        Map<Integer, Map<String, Object>> dataattributeEntries =
                service.getDataattributeEntries(instance.getScenarioInstanceId());
        assertEquals(2, dataattributeEntries.size());
    }

    @Test
    public void testEventLogValues() throws IOException {
        String path = "src/test/resources/EventScenarios/EventLoggingScenario.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.terminateActivityInstanceByName("BeforeEvent", instance);
        List<AbstractEvent> events =  instance.getEventsForScenarioInstance();
        assert (events.size() == 1): "Event was not registered properly";
        String json = FileUtils.readFileToString(
                new File("src/test/resources/history/exampleWebserviceJson.json"));

        ScenarioTestHelper.triggerEventInScenario(instance, base, json);
        HistoryService service = new HistoryService();
        Map<Integer, Map<String, Object>> eventEntries =
                service.getEventEntries(instance.getScenarioInstanceId());
        assertEquals("registered", eventEntries.get(1).get("state"));
        assertEquals("received", eventEntries.get(2).get("state"));

        int eventControlNodeInstanceId = events.get(0).getControlNodeInstanceId();
        assertEquals(eventControlNodeInstanceId, eventEntries.get(1).get("eventid"));
        assertEquals(eventControlNodeInstanceId, eventEntries.get(2).get("eventid"));

        assertEquals("SomeEvent", eventEntries.get(1).get("eventname"));
        assertEquals("SomeEvent", eventEntries.get(2).get("eventname"));
    }

}
