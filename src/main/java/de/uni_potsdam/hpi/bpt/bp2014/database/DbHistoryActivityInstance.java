package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.Map;

/**
 * This class provides the actual implementation for the logging of ActivityInstances and the retrieval of log entries for presentation.
 */
public class DbHistoryActivityInstance extends DbObject {
    /**
     * This method saves a log entry containing an activity state transition into the database.
     *
     * @param id the ID of the ActivityInstance that is changed.
     * @param state the new state of the ActivityInstance.
     * @return the generated key for the insert statement.
     */
    public int createEntry(int id, String state) {
        String sql = "INSERT INTO historyactivityinstance(`activityinstance_id`, `label`, `oldstate`,`newstate`, `scenarioinstance_id`)" +
                " SELECT `id`, " +
                "(SELECT `label` FROM `controlnode`, `controlnodeinstance` WHERE `controlnode`.id=`controlnodeinstance`.controlnode_id AND `controlnodeinstance`.id = "+ id +") AS `label`, " +
                "(SELECT activity_state FROM activityinstance WHERE id = " + id + ") AS `oldstate`, \"" + state + "\" AS `newstate`, " +
                "(SELECT scenarioinstance_id FROM fragmentinstance, controlnodeinstance WHERE fragmentinstance.id = controlnodeinstance.fragmentinstance_id AND controlnodeinstance.id = " + id + ") AS `scenarioinstance_id` " +
                "FROM activityinstance WHERE id = " + id;

        return this.executeInsertStatement(sql);
    }
    /**
     * This method saves a log entry of a newly created ActivityInstance into the database.
     * @param id the ID of the ActivityInstance that is created.
     * @return the generated key for the insert statement.
     */
    public int createNewActivityEntry(int id) {
        String sql = "INSERT INTO historyactivityinstance(`activityinstance_id`,`label`,`newstate`, `scenarioinstance_id`) " +
                "SELECT `id`, " +
                "(SELECT `label` FROM `controlnode`, `controlnodeinstance` WHERE `controlnode`.id=`controlnodeinstance`.controlnode_id AND `controlnodeinstance`.id = "+ id +") AS `label`, " +
                "(SELECT activity_state FROM activityinstance WHERE id = " + id + ") AS `newstate`, " +
                "(SELECT scenarioinstance_id FROM fragmentinstance, controlnodeinstance WHERE fragmentinstance.id = controlnodeinstance.fragmentinstance_id AND controlnodeinstance.id = " + id + ") AS `scenarioinstance_id` " +
                "FROM activityinstance WHERE id = " + id;

        return this.executeInsertStatement(sql);
    }
    /**
     *This method returns the Activity log entries for a ScenarioInstance.
     *
     * @param scenarioInstanceId ID of the ScenarioInstance for which the activity log entries shall be returned.
     * @return a Map with a Map of the log entries' attribute names as keys and their respective values.
     */
    public Map<Integer, Map<String, Object>> getLogEntriesForScenarioInstance(int scenarioInstanceId){
        String sql = "SELECT * FROM historyactivityinstance WHERE scenarioinstance_id = "+scenarioInstanceId+" ORDER BY timestamp DESC";
        return this.executeStatementReturnsMapWithMapWithKeys(sql, "activityinstance_id", "label", "timestamp", "oldstate", "newstate", "scenarioinstance_id", "role_id", "user_id", "comment");
    }

    public Map<Integer, Map<String, Object>> getterminatedLogEntriesForScenarioInstance(int scenarioInstanceId){
        String sql = "SELECT * FROM historyactivityinstance WHERE scenarioinstance_id = "+scenarioInstanceId+" AND newstate = 'terminated' ORDER BY timestamp DESC";
        return this.executeStatementReturnsMapWithMapWithKeys(sql, "activityinstance_id", "label", "timestamp", "oldstate", "newstate", "scenarioinstance_id", "role_id", "user_id", "comment");
    }
}