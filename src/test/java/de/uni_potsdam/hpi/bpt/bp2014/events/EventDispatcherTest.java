package de.uni_potsdam.hpi.bpt.bp2014.events;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.AbstractControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class EventDispatcherTest extends JerseyTest {
    WebTarget base;


    @Before
    public void setUpBase() {
        base = target("eventdispatcher");
    }


    @After
    public void teardown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(
                de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher.class);
    }

    @Test
    public void testActivateEvent() {
        String path = "src/test/resources/EventScenarios/StartEventWithQuery.json";
        try {
            ScenarioInstance scenarioInstance = EventTestHelper.createScenarioInstance(path);
            int scenarioId = scenarioInstance.getScenarioId();
            int scenarioInstanceId = scenarioInstance.getScenarioInstanceId();
            List<String> registeredEventKeys = scenarioInstance.getRegisteredEventKeys();

            List<AbstractControlNodeInstance> activatedBeforeEvent =
                    scenarioInstance.getEnabledControlNodeInstances();
            assertEquals(activatedBeforeEvent.size(), 0);
            String registeredEvent = registeredEventKeys.get(0);
            String route = String.format("scenario/%d/instance/%d/events/%s", scenarioId,
                    scenarioInstanceId, registeredEvent);
            base.path(route).request().post(Entity.json(""));
            List<AbstractControlNodeInstance> activatedAfterEvent =
                    scenarioInstance.getEnabledControlNodeInstances();
            assertEquals(activatedAfterEvent.size(), 1);
        } catch (IOException e) {
            e.printStackTrace();
            assert(false);
        }
    }
}
