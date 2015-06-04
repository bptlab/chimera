package de.uni_potsdam.hpi.bpt.bp2014.janalytics;

import java.util.ArrayList;
import java.util.Map;


/**
 * This class represents the Model within the analytics framework
 */
public class AnalyticsModel extends MetaAnalyticsModel {

    /**
     * @param scenarioInstance_id The ID of the scenario instance.
     * @return
     */
    public static ArrayList<Map<Integer, Map<String, Object>>> exampleAlgorithm1(int scenarioInstance_id) {
        ArrayList<Map<Integer, Map<String, Object>>> result = new ArrayList<>();

        Map<Integer, Map<String, Object>> ActivityLog = getLogEntriesForScenarioInstanceWithinActivity(scenarioInstance_id);
        Map<Integer, Map<String, Object>> DataAttributeLog = getLogEntriesForScenarioInstanceWithinDataAttribute(scenarioInstance_id);
        Map<Integer, Map<String, Object>> DataObjectLog = getLogEntriesForScenarioInstanceWithinDataObject(scenarioInstance_id);
        result.add(ActivityLog);
        result.add(DataAttributeLog);
        result.add(DataObjectLog);

        return result;
    }


    /**
     * @param scenarioInstance_id The ID of the scenario instance.
     * @return
     */
    public static ArrayList<Map<Integer, Map<String, Object>>> calculateScenarioInstanceRunTime(int scenarioInstance_id) {
        ArrayList<Map<Integer, Map<String, Object>>> result = new ArrayList<>();

        Map<Integer, Map<String, Object>> timestampsForScenarioInstance = getLogTimestampsForScenarioInstance(scenarioInstance_id);
        result.add(timestampsForScenarioInstance);

        return result;
    }


}
