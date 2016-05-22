package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.ActivityInstance;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class TerminationConditionTest {
    @Test
    public void testTerminationCondition() {
        try {
            String path = "src/test/resources/Scenarios/TerminationConditionScenario.json";
            ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
            assertEquals(false, scenarioInstance.checkTerminationCondition());

            List<AbstractControlNodeInstance> controlNodeInstances =
                    scenarioInstance.getEnabledControlNodeInstances();
            ActivityInstance instance = (ActivityInstance) controlNodeInstances.get(0);
            instance.terminate();
            assertEquals(true, scenarioInstance.checkTerminationCondition());
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void testMultiTerminationCondition() {
        try {
            String path = "src/test/resources/Scenarios/TerminationConditionScenario.json";
            ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
            assertEquals(false, scenarioInstance.checkTerminationCondition());

            ScenarioTestHelper.beginActivityByName("Review Costumer", scenarioInstance);
            ScenarioTestHelper.terminateActivityInstanceByName("Review Costumer", scenarioInstance);
            ScenarioTestHelper.beginActivityByName("Accept Another", scenarioInstance);
            ScenarioTestHelper.terminateActivityInstanceByName("Accept Another", scenarioInstance);
            assertEquals(true, scenarioInstance.checkTerminationCondition());
        } catch (IOException e) {
            Assert.fail();
        }
    }
}
