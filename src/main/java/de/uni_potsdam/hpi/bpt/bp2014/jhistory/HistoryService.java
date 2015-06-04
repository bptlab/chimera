package de.uni_potsdam.hpi.bpt.bp2014.jhistory;

import de.uni_potsdam.hpi.bpt.bp2014.database.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class HistoryService {
    /**
     * Database Connection objects
     */
    private final DbActivityInstance dbActivityInstance = new DbActivityInstance();
    private final DbControlNode dbControlNode = new DbControlNode();


    /**
     * Gives all ids of terminated activities for a scenario instance id.
     *
     * @param scenarioInstanceId This is the id of the scenario instance.
     * @return a list of int ids of the activities.
     */
    public LinkedList<Integer> getTerminatedActivitiesForScenarioInstance(int scenarioInstanceId) {
        return dbActivityInstance.getTerminatedActivitiesForScenarioInstance(scenarioInstanceId);
    }

    /**
     * Returns the Labels of terminated activities for a scenario instance id.
     *
     * @param scenarioInstanceId This is the id of the scenario instance.
     * @return a Map. Keys are the activity ids. Values are the labels of the activities.
     */
    public HashMap<Integer, String> getTerminatedActivityLabelsForScenarioInstance(int scenarioInstanceId) {
        LinkedList<Integer> ids = dbActivityInstance.getTerminatedActivitiesForScenarioInstance(scenarioInstanceId);
        HashMap<Integer, String> labels = new HashMap<Integer, String>();
        for (int id : ids) {
            labels.put(id, dbControlNode.getLabel(id));
        }
        return labels;
    }

    /**
     * This method returns the DataObjectInstances log entries for a ScenarioInstance.
     *
     * @param scenarioInstanceId ID of the ScenarioInstance for which the DataObjectInstance log entries shall be returned.
     * @return a Map with a Map of the log entries' attribute names as keys and their respective values.
     */
    public Map<Integer, Map<String, Object>> getDataObjectLogEntriesForScenarioInstance(int scenarioInstanceId) {
        DbHistoryDataObjectInstance dbHistoryDataObjectInstance = new DbHistoryDataObjectInstance();
        return dbHistoryDataObjectInstance.getLogEntriesForScenarioInstance(scenarioInstanceId);
    }

    /**
     * This method returns the Activity log entries for a ScenarioInstance.
     *
     * @param scenarioInstanceId ID of the ScenarioInstance for which the activity log entries shall be returned.
     * @return a Map with a Map of the log entries' attribute names as keys and their respective values.
     */
    public Map<Integer, Map<String, Object>> getActivityInstanceLogEntriesForScenarioInstance(int scenarioInstanceId) {
        DbHistoryActivityInstance dbHistoryActivityInstance = new DbHistoryActivityInstance();
        return dbHistoryActivityInstance.getLogEntriesForScenarioInstance(scenarioInstanceId);
    }

    /**
     * This method returns the terminated Activity log entries for a ScenarioInstance.
     *
     * @param scenarioInstanceId ID of the ScenarioInstance for which the activity log entries shall be returned.
     * @return a Map with a Map of the log entries' attribute names as keys and their respective values.
     */
    public Map<Integer, Map<String, Object>> getSelectedActivityInstanceLogEntriesForScenarioInstance(int scenarioInstanceId) {
        DbHistoryActivityInstance dbHistoryActivityInstance = new DbHistoryActivityInstance();
        return dbHistoryActivityInstance.getterminatedLogEntriesForScenarioInstance(scenarioInstanceId);
    }

    /**
     * This method returns the DataAttributeInstance log entries for a ScenarioInstance.
     *
     * @param scenarioInstanceId ID of the ScenarioInstance for which the DataAttributeInstance log entries shall be returned.
     * @return a Map with a Map of the log entries' attribute names as keys and their respective values.
     */
    public Map<Integer, Map<String, Object>> getDataAttributeInstanceLogEntriesForScenarioInstance(int scenarioInstanceId) {
        DbHistoryDataAttributeInstance dbHistoryDataAttributeInstance = new DbHistoryDataAttributeInstance();
        return dbHistoryDataAttributeInstance.getLogEntriesForScenarioInstance(scenarioInstanceId);
    }
}
