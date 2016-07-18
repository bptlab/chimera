package de.uni_potsdam.hpi.bpt.bp2014.jcore.data;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.ActivityInstance;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class LockingTest {

    @After
    public void tearDown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    /**
     * Test if the locked data object instances are correctly written and loaded
     */
    @Test
    public void reloadLockingTest() throws IOException {
        String path = "src/test/resources/core/LockingScenario.json";
        ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.executeActivityByName("Create Data Objects", scenarioInstance);

        Optional<AbstractControlNodeInstance> activityInstance =
                ScenarioTestHelper.findActivityInstanceInScenario("First alternative", scenarioInstance);
        assert activityInstance.isPresent(): "Error finding activity instance";
        ActivityInstance instance = (ActivityInstance) activityInstance.get();

        List<DataObject> possibleInputObjects = scenarioInstance.getDataManager()
                .getAvailableInput(instance.getControlNodeId());
        assert possibleInputObjects.size() == 2;

        instance.begin(possibleInputObjects.stream().map(DataObject::getId)
                .collect(Collectors.toList()));

        int scenarioId = scenarioInstance.getScenarioId();
        int scenarioInstanceId = scenarioInstance.getId();
        ScenarioInstance reloaded = new ScenarioInstance(scenarioId, scenarioInstanceId);

        Long locked = reloaded.getDataManager().getDataObjects()
                 .stream().filter(DataObject::isLocked).count();
        assertEquals(0, scenarioInstance.getEnabledControlNodeInstances().size());
        assertEquals(2, locked.intValue());
    }

    @Test
    public void testLocking() throws IOException {
        String path = "src/test/resources/core/LockingScenario.json";
        ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.executeActivityByName("Create Data Objects", scenarioInstance);
        assertEquals(2, scenarioInstance.getEnabledControlNodeInstances().size());

        Optional<AbstractControlNodeInstance> activityInstance =
            ScenarioTestHelper.findActivityInstanceInScenario("First alternative", scenarioInstance);
        assert activityInstance.isPresent(): "Error finding activity instance";
        ActivityInstance instance = (ActivityInstance) activityInstance.get();

        List<DataObject> possibleInputObjects = scenarioInstance.getDataManager()
                .getAvailableInput(instance.getControlNodeId());
        assert possibleInputObjects.size() == 2;

        instance.begin(possibleInputObjects.stream().map(DataObject::getId)
                .collect(Collectors.toList()));

        // Since both activities rely on the same data input object after starting one
        // the other one should not be enabled any more.
        assertEquals(0, scenarioInstance.getEnabledControlNodeInstances().size());
    }

    @Test
    public void testUnlocking() throws IOException {
        String path = "src/test/resources/core/LockingScenario.json";
        ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.executeActivityByName("Create Data Objects", scenarioInstance);
        assertEquals(2, scenarioInstance.getEnabledControlNodeInstances().size());

        Optional<AbstractControlNodeInstance> activityInstance =
                ScenarioTestHelper.findActivityInstanceInScenario("First alternative", scenarioInstance);
        assert activityInstance.isPresent(): "Error finding activity instance";
        ActivityInstance instance = (ActivityInstance) activityInstance.get();

        List<DataObject> possibleInputObjects = scenarioInstance.getDataManager()
                .getAvailableInput(instance.getControlNodeId());
        assert possibleInputObjects.size() == 2;

        instance.begin(possibleInputObjects.stream().map(DataObject::getId)
                .collect(Collectors.toList()));
        instance.terminate();
        assertEquals(1, scenarioInstance.getEnabledControlNodeInstances().size());
    }
}
