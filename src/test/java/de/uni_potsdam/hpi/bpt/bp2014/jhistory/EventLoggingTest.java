package de.uni_potsdam.hpi.bpt.bp2014.jhistory;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import java.io.IOException;
import java.sql.SQLException;

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
        String path = "src/test/resources/EventScenarios/TestIntermediateEvent.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);

        // Before reaching an event no event should be activated
        assertEquals(0, service.getEventEntries().size());
        ScenarioTestHelper.terminateActivityInstanceByName("BeforeEvent", instance);
        assertEquals(1, service.getEventEntries().size());
    }

    @Test
    public void testEventReceivingLogging() throws IOException {
        HistoryService service = new HistoryService();
        String path = "src/test/resources/EventScenarios/TestIntermediateEvent.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.terminateActivityInstanceByName("BeforeEvent", instance);
        // Only the registration log should be in the database
        assertEquals(1, service.getEventEntries().size());
        ScenarioTestHelper.triggerEventInScenario(instance, base);

        // After triggering the event there should be another log entry
        assertEquals(2, service.getEventEntries().size());
    }

    @Test
    public void testEventLogLinking() {
        Assert.fail();
    }

    @Test
    public void testEventLogValues() {
        Assert.fail();
    }
}
