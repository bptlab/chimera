package de.uni_potsdam.hpi.bpt.bp2014.jhistory;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.ActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataManager;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class HistoryLoggerTest {
    @After
    public void teardown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
    public void testCreationUponInitialization() throws IOException {
        HistoryService service = new HistoryService();
        String path = "src/test/resources/history/HistoryExample.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);
        // After instantiating a scenario the creation of an activity instance should be logged
        assertEquals(1, service.getActivityInstanceEntries(
                instance.getId()).size());

        assertEquals(0, service.getDataObjectEntries(
                instance.getId()).size());

        assertEquals(0, service.getDataAttributeEntries(
                instance.getId()).size());
    }

    /**
     * This test loads an scenario with a single activity ChangeData which changes the state
     * data object data from init to changed. Therefore an activity log should be present as well
     * as an state transition references this activity.
     * @throws IOException
     */
    @Test
    public void testActivityLog() throws IOException {
        String path = "src/test/resources/history/HistoryExample.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.executeActivityByName("Create Data", instance);

        HistoryService service = new HistoryService();
        List<LogEntry> activityEntries =
                service.getActivityInstanceEntries(instance.getId());
        assertEquals(4, activityEntries.size());
    }


    @Test
    public void testDataobjectChange() throws IOException {
        String path = "src/test/resources/history/HistoryExample.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.executeActivityByName("Create Data", instance);
        ScenarioTestHelper.executeActivityByName("Change Data", instance);

        HistoryService service = new HistoryService();
        List<LogEntry> dataObjectEntries =
                service.getDataObjectEntries(instance.getId());
        // TODO also assert values if there is no other test
        assertEquals(2, dataObjectEntries.size());
    }

    @Test
    public void testDataattributeChange() throws IOException {
        String path = "src/test/resources/history/HistoryExample.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.executeActivityByName("Create Data", instance);
        ActivityInstance activity = ScenarioTestHelper.findActivityInstanceInNodes(
                "Change Data", instance.getEnabledControlNodeInstances());

        changeDataattributeValues(instance, activity);
        HistoryService service = new HistoryService();
        List<LogEntry> dataattributeEntries
                = service.getDataAttributeEntries(instance.getId());
        assertEquals(4, dataattributeEntries.size());
    }

    /**
     * All logs which are caused by one activityInstance should have and entry pointing to that
     * activity instance.
     */
    @Test
    public void testCorrectLinking() throws IOException {
        String path = "src/test/resources/history/HistoryExample.json";
        ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.executeActivityByName("Create Data", scenarioInstance);
        ActivityInstance activity = ScenarioTestHelper.findActivityInstanceInNodes(
                "Change Data", scenarioInstance.getEnabledControlNodeInstances());
        changeDataattributeValues(scenarioInstance, activity);
        activity.terminate();
        HistoryService service = new HistoryService();

        List<LogEntry> dataObjectEntries =
                service.getDataObjectEntries(scenarioInstance.getId());

        List<LogEntry> dataattributeEntries
                = service.getDataAttributeEntries(scenarioInstance.getId());

        List<LogEntry> activityEntries =
                service.getActivityInstanceEntries(scenarioInstance.getId());

        // First three activity entries belong to Create Data
        assertEquals(activity.getControlNodeInstanceId(),
                activityEntries.get(4).getLoggedId());

        assertEquals(activity.getControlNodeInstanceId(),
                dataObjectEntries.get(1).getCause());

        // First two data attribute entries correspond to the creation of data attributes
        assertEquals(activity.getControlNodeInstanceId(),
                dataattributeEntries.get(2).getCause());
    }

    private void changeDataattributeValues(
            ScenarioInstance scenarioInstance, ActivityInstance activity) {
        Map<Integer, String> idToChangedValue = new HashMap<>();
        DataManager manager = scenarioInstance.getDataManager();
        for (DataAttributeInstance attribute : manager.getDataAttributeInstances()) {
            idToChangedValue.put(attribute.getId(), "bar");
        }
        assert(idToChangedValue.size() > 0);
        // Begin activity so that it can alter the values of data attributes
        activity.begin();
        manager.setAttributeValues(
                activity.getControlNodeInstanceId(), idToChangedValue);
    }
}
