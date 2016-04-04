package de.uni_potsdam.hpi.bpt.bp2014.jhistory;

import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.DataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class HistoryHistoryLoggerTest {

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
        assertEquals(1, activityEntries.size());
    }


    @Test
    public void testDataobjectChange() throws IOException {
        String path = "src/test/resources/history/HistoryExample.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.terminateActivityInstanceByName("ChangeData", instance);

        HistoryService service = new HistoryService();

        Map<Integer, Map<String, Object>> dataObjectEntries =
                service.getDataObjectEntries(instance.getScenarioInstanceId());
        assertEquals(1, dataObjectEntries.size());
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
        activity.setDataAttributeValues(idToChangedValue);

        HistoryService service = new HistoryService();
        Map<Integer, Map<String, Object>> dataattributeEntries
                = service.getDataattributeEntries(instance.getScenarioInstanceId());
        assertEquals(1, dataattributeEntries.size());
    }

}