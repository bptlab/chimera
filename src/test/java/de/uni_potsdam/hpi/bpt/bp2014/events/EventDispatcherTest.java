package de.uni_potsdam.hpi.bpt.bp2014.events;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.*;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.ActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        return new ResourceConfig(EventDispatcher.class);
    }

    @Test
    public void testActivateEvent() {
        String path = "src/test/resources/Scenarios/StartEventWithQuery.json";
        try {
            ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
            List<AbstractControlNodeInstance> activatedBeforeEvent =
                    scenarioInstance.getEnabledControlNodeInstances();
            assertEquals(activatedBeforeEvent.size(), 0);
            ScenarioTestHelper.triggerEventInScenario(scenarioInstance, base, "");
            ScenarioInstance scenarioInstance2 = new ScenarioInstance(
                    scenarioInstance.getScenarioId(), scenarioInstance.getScenarioInstanceId());
            List<AbstractControlNodeInstance> activatedAfterEvent =
                    scenarioInstance2.getEnabledControlNodeInstances();
            assertFalse(activatedAfterEvent.isEmpty());
            assertTrue(activatedAfterEvent.get(0) instanceof ActivityInstance);
            //assertEquals(activatedAfterEvent.size(), 1);
        } catch (IOException e) {
            e.printStackTrace();
            assert(false);
        }
    }

    @Test
    public void testRemovalAfterActivation() {
        String path = "src/test/resources/Scenarios/StartEventWithQuery.json";
        try {
            ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
            List<String> registeredEventKeysBeforeEvent = scenarioInstance.getRegisteredEventKeys();
            assertEquals(1, registeredEventKeysBeforeEvent.size());
            ScenarioTestHelper.triggerEventInScenario(scenarioInstance, base, "");
            List<String> registeredEventKeysAfterEvent = scenarioInstance.getRegisteredEventKeys();
            assertEquals(0, registeredEventKeysAfterEvent.size());
        } catch (IOException e) {
            assert (false);
        }
    }

    @Test
    public void testParseQuery() throws IOException {
        String path = "src/test/resources/EventScenarios/VariablesInQueries.json";
        ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
        assertEquals("The query was inadvertently modified",
                "SELECT * FROM data.path",
                EventDispatcher.insertAttributesIntoQueryString("SELECT * FROM data.path",
                scenarioInstance.getScenarioInstanceId(),
                scenarioInstance.getScenarioId()));
        ScenarioTestHelper.executeActivityByName("create data", scenarioInstance);
        ScenarioTestHelper.executeActivityByName("modify data", scenarioInstance);
        new DataAttributeInstance(1).setValue("AnEvent");
        String replacedQuery = EventDispatcher.insertAttributesIntoQueryString("SELECT * FROM #data.path",
                scenarioInstance.getScenarioInstanceId(),
                scenarioInstance.getScenarioId());
        assertEquals("SELECT * FROM AnEvent", replacedQuery);
    }
}
