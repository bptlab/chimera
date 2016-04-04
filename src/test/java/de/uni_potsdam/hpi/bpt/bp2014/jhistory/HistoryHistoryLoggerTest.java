package de.uni_potsdam.hpi.bpt.bp2014.jhistory;

import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.junit.Test;

import java.io.IOException;
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
        assertEquals(1, activityEntries.keySet().size());

        Map<Integer, Map<String, Object>> dataObjectEntries =
                service.getDataObjectEntries(instance.getScenarioInstanceId());
        assertEquals(1, dataObjectEntries.keySet().size());
    }

}