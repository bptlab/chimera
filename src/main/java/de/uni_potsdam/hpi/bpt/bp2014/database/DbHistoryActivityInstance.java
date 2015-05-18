package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.Map;

/**
 * This class provides the actual implementation for the logging of ActivityInstances and the retrieval of log entries for presentation.
 */
public class DbHistoryActivityInstance extends DbObject {
    /**
     * This method saves a log entry containing an activity state transition into the database.
     *
     * @param id    the ID of the ActivityInstance that is changed.
     * @param state the new state of the ActivityInstance.
     * @return the generated key for the insert statement.
     */
    public int createEntry(int id, String state) {
        String sql = "INSERT INTO historyactivityinstance(`activityinstance_id`, `oldstate`,`newstate`, `scenarioinstance_id`)" +
                " SELECT `id`, " +
                "(SELECT activity_state FROM activityinstance WHERE id = " + id + ") AS `oldstate`, \"" + state + "\" AS `newstate`, " +
                "(SELECT scenarioinstance_id FROM fragmentinstance, controlnodeinstance WHERE fragmentinstance.id = controlnodeinstance.fragmentinstance_id AND controlnodeinstance.id = " + id + ") AS `scenarioinstance_id` " +
                "FROM activityinstance WHERE id = " + id;

        return this.executeInsertStatement(sql);
    }

    /**
     * This method saves a log entry of a newly created ActivityInstance into the database.
     *
     * @param id the ID of the ActivityInstance that is created.
     * @return the generated key for the insert statement.
     */
    public int createNewActivityEntry(int id) {
        String sql = "INSERT INTO historyactivityinstance(`activityinstance_id`,`newstate`, `scenarioinstance_id`) " +
                "SELECT `id`, " +
                "(SELECT activity_state FROM activityinstance WHERE id = " + id + ") AS `newstate`, " +
                "(SELECT scenarioinstance_id FROM fragmentinstance, controlnodeinstance WHERE fragmentinstance.id = controlnodeinstance.fragmentinstance_id AND controlnodeinstance.id = " + id + ") AS `scenarioinstance_id` " +
                "FROM activityinstance WHERE id = " + id;

        return this.executeInsertStatement(sql);
    }

    /**
     * This method returns the Activity log entries for a ScenarioInstance.
     *
     * @param scenarioInstanceId ID of the ScenarioInstance for which the activity log entries shall be returned.
     * @return a Map with a Map of the log entries' attribute names as keys and their respective values.
     */
    public Map<Integer, Map<String, Object>> getLogEntriesForScenarioInstance(int scenarioInstanceId) {
        String sql = "SELECT h.id, h.scenarioinstance_id, cn.label, h.activityinstance_id, h.oldstate, h.newstate, h.timestamp FROM historyactivityinstance AS h, controlnode AS cn, controlnodeinstance AS cni WHERE h.scenarioinstance_id = " + scenarioInstanceId + "  AND h.activityinstance_id = cni.id AND cni.controlnode_id = cn.id  ORDER BY timestamp DESC";
        return this.executeStatementReturnsMapWithMapWithKeys(sql, "h.id", "h.scenarioinstance_id", "cn.label", "h.activityinstance_id", "h.oldstate", "h.newstate", "h.timestamp");
    }

    /**
     * This method returns the terminated Activity log entries for a ScenarioInstance.
     *
     * @param scenarioInstanceId ID of the ScenarioInstance for which the activity log entries shall be returned.
     * @return a Map with a Map of the log entries' attribute names as keys and their respective values.
     */
    public Map<Integer, Map<String, Object>> getterminatedLogEntriesForScenarioInstance(int scenarioInstanceId) {
        String sql = "SELECT h.id, h.scenarioinstance_id, cn.label, h.activityinstance_id, h.oldstate, h.newstate, h.timestamp FROM historyactivityinstance AS h, controlnode AS cn, controlnodeinstance AS cni WHERE h.scenarioinstance_id = " + scenarioInstanceId + "  AND h.activityinstance_id = cni.id AND cni.controlnode_id = cn.id AND h.newstate = 'terminated' ORDER BY timestamp DESC";
        return this.executeStatementReturnsMapWithMapWithKeys(sql, "cn.label", "h.scenarioinstance_id", "h.id", "h.activityinstance_id", "h.oldstate", "h.newstate", "h.timestamp");
    }
}
