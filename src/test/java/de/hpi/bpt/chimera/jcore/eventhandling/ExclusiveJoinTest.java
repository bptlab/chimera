package de.hpi.bpt.chimera.jcore.eventhandling;

import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import de.hpi.bpt.chimera.ScenarioTestHelper;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractControlNodeInstance;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

/**
 * TODO look at this again
 */
public class ExclusiveJoinTest {
    @Test @Ignore
    public void testExclusiveJoin() throws InterruptedException {
        String path = "src/test/resources/Scenarios/ExclusiveJoinScenario.json";
        try {
            ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
            Thread.sleep(2000);
            ScenarioInstance reloadedInstance = new ScenarioInstance(scenarioInstance.getScenarioId(),
                    scenarioInstance.getId());
            List<AbstractControlNodeInstance> enabledActivities =
                    reloadedInstance.getEnabledControlNodeInstances();
            assertEquals(1, enabledActivities.size());
            ActivityInstance firstActivity = (ActivityInstance) enabledActivities.get(0);
            firstActivity.begin();
            firstActivity.terminate();
            List<AbstractControlNodeInstance> enabledActivitiesAfterJoin =
                    reloadedInstance.getEnabledControlNodeInstances();
            System.out.println(enabledActivitiesAfterJoin);
        } catch (IOException e) {
            fail();
        }
    }
}
