package de.hpi.bpt.chimera.events;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.ScenarioTestHelper;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractControlNodeInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractEvent;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.StartEvent;

/**
 *
 */
public class StartEventTest extends AbstractDatabaseDependentTest {

    @Test
	public void testStartEventRegistration() {
        String path = "src/test/resources/Scenarios/StartEventWithQuery.json";
        try {
            ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
            List<AbstractControlNodeInstance> controlNodeInstances =
                    scenarioInstance.getEnabledControlNodeInstances();
            assert (controlNodeInstances.size() == 0);
            List<AbstractEvent> events = scenarioInstance.getEventsForScenarioInstance();
            assert (events.get(0) instanceof StartEvent);
        } catch (IOException e) {
            Assert.fail();
        }
    }


    // If the start event has no event query, the next control node should be activated
    // from the beginning.
    @Test
    public void testStartQueryWithoutEvent() {
        String path = "src/test/resources/Scenarios/StartEventWithoutQuery.json";
        try {
            ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
            List<AbstractControlNodeInstance> controlNodeInstances =
                    scenarioInstance.getEnabledControlNodeInstances();
            ActivityInstance firstActivity = (ActivityInstance) controlNodeInstances.get(0);
            assert (firstActivity.getLabel().equals("Do something"));
        } catch (IOException e) {
            Assert.fail();
        }
    }
}
