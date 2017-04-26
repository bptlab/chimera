package de.hpi.bpt.chimera.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.AbstractTest;
import de.hpi.bpt.chimera.ScenarioTestHelper;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractControlNodeInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import de.hpi.bpt.chimera.jcore.data.DataAttributeInstance;
import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;

/**
 *
 */
public class EventDispatcherTest extends AbstractTest {
    WebTarget base;


    @Before
    public void setUpBase() {
        base = target("eventdispatcher");
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
                    scenarioInstance.getScenarioId(), scenarioInstance.getId());
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
                scenarioInstance.getId(),
                scenarioInstance.getScenarioId()));
        ScenarioTestHelper.executeActivityByName("create data", scenarioInstance);
        ScenarioTestHelper.executeActivityByName("modify data", scenarioInstance);
        new DataAttributeInstance(1).setValue("AnEvent");
        String replacedQuery = EventDispatcher.insertAttributesIntoQueryString("SELECT * FROM #data.path",
                scenarioInstance.getId(),
                scenarioInstance.getScenarioId());
        assertEquals("SELECT * FROM AnEvent", replacedQuery);
    }
}
