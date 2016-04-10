package de.uni_potsdam.hpi.bpt.bp2014.jhistory;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.DataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class HistoryHistoryLoggerTest {
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
                instance.getScenarioInstanceId()).size());

        assertEquals(1, service.getDataObjectEntries(
                instance.getScenarioInstanceId()).size());

        assertEquals(1, service.getDataattributeEntries(
                instance.getScenarioInstanceId()).size());
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
        ScenarioTestHelper.terminateActivityInstanceByName("ChangeData", instance);

        HistoryService service = new HistoryService();
        Map<Integer, Map<String, Object>> activityEntries =
                service.getActivityInstanceEntries(instance.getScenarioInstanceId());
        assertEquals(2, activityEntries.size());
        assertEquals(2, activityEntries.get(2).get("h.activityinstance_id"));
    }


    @Test
    public void testDataobjectChange() throws IOException {
        String path = "src/test/resources/history/HistoryExample.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.terminateActivityInstanceByName("ChangeData", instance);

        HistoryService service = new HistoryService();

        Map<Integer, Map<String, Object>> dataObjectEntries =
                service.getDataObjectEntries(instance.getScenarioInstanceId());
        assertEquals(2, dataObjectEntries.size());
    }

    @Test
    public void testDataattributeChange() throws IOException {
        String path = "src/test/resources/history/HistoryExample.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);
        ActivityInstance activity = ScenarioTestHelper.getActivityInstanceByName(
                "ChangeData", instance);

        Map<Integer, String> idToChangedValue = new HashMap<>();
        for (DataAttributeInstance attribute : instance.getDataAttributeInstances().values()) {
            idToChangedValue.put(attribute.getDataAttributeInstanceId(), "bar");
        }
        assert(idToChangedValue.size() > 0);
        // Begin activity so that it can alter the values of data attributes
        activity.begin();
        activity.setDataAttributeValues(idToChangedValue);

        HistoryService service = new HistoryService();
        Map<Integer, Map<String, Object>> dataattributeEntries
                = service.getDataattributeEntries(instance.getScenarioInstanceId());
        assertEquals(2, dataattributeEntries.size());
    }

}