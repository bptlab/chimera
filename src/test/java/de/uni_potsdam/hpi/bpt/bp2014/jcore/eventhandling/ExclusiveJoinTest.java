package de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling;

import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.*;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class ExclusiveJoinTest {
    @Test
    public void testExclusiveJoin() throws InterruptedException {
        String path = "src/test/resources/EventScenarios/ExclusiveJoinScenario.json";
        try {
            ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
            Thread.sleep(2000);
            ScenarioInstance reloadedInstance = new ScenarioInstance(scenarioInstance.getScenarioId(),
                    scenarioInstance.getScenarioInstanceId());
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
