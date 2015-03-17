package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.Map;

/**
 * Created by Jan Selke on 16.03.2015.
 */
public class DbHistoryActivityInstance extends DbObject {

    public int createEntry(int id, String state) {
        String sql = "INSERT INTO historyactivityinstance(`activityinstance_id`, `label`, `oldstate`,`newstate`, `scenarioinstance_id`)" +
                " SELECT `id`, " +
                "(SELECT `label` FROM `controlnode`, `controlnodeinstance` WHERE `controlnode`.id=`controlnodeinstance`.controlnode_id AND `controlnodeinstance`.id = "+ id +") AS `label`, " +
                "(SELECT activity_state FROM activityinstance WHERE id = " + id + ") AS `oldstate`, \"" + state + "\" AS `newstate`, " +
                "(SELECT scenarioinstance_id FROM fragmentinstance, controlnodeinstance WHERE fragmentinstance.id = controlnodeinstance.fragmentinstance_id AND controlnodeinstance.id = " + id + ") AS `scenarioinstance_id` " +
                "FROM activityinstance WHERE id = " + id;

        return this.executeInsertStatement(sql);
    }


    public int createNewActivityEntry(int id) {
        String sql = "INSERT INTO historyactivityinstance(`activityinstance_id`,`label`,`newstate`, `scenarioinstance_id`) " +
                "SELECT `id`, " +
                "(SELECT `label` FROM `controlnode`, `controlnodeinstance` WHERE `controlnode`.id=`controlnodeinstance`.controlnode_id AND `controlnodeinstance`.id = "+ id +") AS `label`, " +
                "(SELECT activity_state FROM activityinstance WHERE id = " + id + ") AS `newstate`, " +
                "(SELECT scenarioinstance_id FROM fragmentinstance, controlnodeinstance WHERE fragmentinstance.id = controlnodeinstance.fragmentinstance_id AND controlnodeinstance.id = " + id + ") AS `scenarioinstance_id` " +
                "FROM activityinstance WHERE id = " + id;

        return this.executeInsertStatement(sql);
    }

    public Map<Integer, Map<String, Object>> getLogEntriesForScenarioInstance(int scenarioInstanceId){
        String sql = "SELECT * FROM historyactivityinstance WHERE scenarioinstance_id = "+scenarioInstanceId;
        return this.executeStatementReturnsMapWithMapWithKeys(sql, "activityinstance_id", "timestamp", "oldstate", "newstate", "scenarioinstance_id", "role_id", "user_id", "comment");
    }
}