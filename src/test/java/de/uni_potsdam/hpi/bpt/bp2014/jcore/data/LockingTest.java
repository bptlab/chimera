package de.uni_potsdam.hpi.bpt.bp2014.jcore.data;

import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class LockingTest {
    /**
     * TODO remodel the scenario since there are no initial dataobjects anymore
     * Test if the locked data object instances are correctly written and loaded
     */
    @Test
    public void reloadLockingTest() throws IOException {
        String path = "src/test/resources/core/LockingScenario.json";
        ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.beginActivityByName("FirstAlternative", scenarioInstance);

        int scenarioId = scenarioInstance.getScenarioId();
        int scenarioInstanceId = scenarioInstance.getScenarioInstanceId();
        ScenarioInstance reloaded = new ScenarioInstance(scenarioId, scenarioInstanceId);

        Long locked = reloaded.getDataManager().getDataObjects()
                 .stream().filter(DataObject::isLocked).count();
        assertEquals(1, locked.intValue());
    }

    @Test
    public void testLocking() throws IOException {
        String path = "src/test/resources/core/LockingScenario.json";
        ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
        assertEquals(2, scenarioInstance.getEnabledControlNodeInstances().size());

        // Since both activities rely on the same data input object after starting one
        // the other one should not be enabled any more.
        ScenarioTestHelper.beginActivityByName("FirstAlternative", scenarioInstance);
        assertEquals(0, scenarioInstance.getEnabledControlNodeInstances().size());
    }
}
