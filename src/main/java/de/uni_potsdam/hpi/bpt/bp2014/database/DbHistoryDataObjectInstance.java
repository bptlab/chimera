package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.Map;

/**
 * This class provides the actual implementation for the logging of DataObjectInstances and the retrieval of log entries for presentation.
 */

public class DbHistoryDataObjectInstance extends DbObject {
    /**
     * This method saves a log entry containing a DataAttributeInstance value change into the database.
     *
     * @param object_instance_id the ID of the DataObjectInstance that is changed.
     * @param state_id the new state of the DataObjectInstance.
     * @return the generated key for the insert statement.
     */
    public int createEntry(int object_instance_id, int state_id) {
        String sql = "INSERT INTO `historydataobjectinstance` (`scenarioinstance_id`,`dataobjectinstance_id`, `name`, `old_state_id`,`old_state_name`,`new_state_id`,`new_state_name`) " +
                "SELECT (SELECT `scenarioinstance_id` FROM `dataobjectinstance` WHERE `id` = "+object_instance_id+") AS`scenarioinstance_id`," +
                " `id`," +
                "(SELECT `name` FROM `dataobjectinstance`, `dataobject` WHERE `dataobject_id`=`dataobject`.id AND `dataobjectinstance`.id = "+object_instance_id+") AS `name`, " +
                "(SELECT `state_id` FROM `dataobjectinstance` WHERE `id` = "+object_instance_id+") AS `old_state_id`, " +
                "(SELECT `name` FROM `dataobjectinstance`, `state` WHERE `state`.id = `state_id` AND `dataobjectinstance`.id = "+object_instance_id+") AS `old_state_name`, " +
                ""+state_id+" AS `new_state_id`, " +
                "(SELECT `name`from state WHERE `id` = "+state_id+") AS `new_state_name` " +
                "FROM `dataobjectinstance` WHERE `id` = "+object_instance_id;
        return this.executeInsertStatement(sql);
    }
    /**
     * This method saves a log entry of a newly created DataObjectInstance into the database.
     *
     * @param object_instance_id the ID of the DataObjectInstance that is created.
     * @return the generated key for the insert statement.
     */
    public int createNewDataObjectInstanceEntry (int object_instance_id){
        String sql ="INSERT INTO `historydataobjectinstance` (`scenarioinstance_id`,`dataobjectinstance_id`, `name`, `new_state_id`,`new_state_name`) " +
                "SELECT (SELECT `scenarioinstance_id` FROM `dataobjectinstance` WHERE `id` = "+object_instance_id+") AS`scenarioinstance_id`, " +
                "`id`, " +
                "(SELECT `name` FROM `dataobjectinstance`, `dataobject` WHERE `dataobject_id`=`dataobject`.id AND `dataobjectinstance`.id = "+object_instance_id+") AS `name`," +
                "(SELECT `state_id` FROM `dataobjectinstance` WHERE `id` = "+object_instance_id+") AS `new_state_id`, " +
                "(SELECT `name` FROM `dataobjectinstance`, `state` WHERE `state`.id = `state_id` AND `dataobjectinstance`.id = "+object_instance_id+") AS `new_state_name` " +
                "FROM `dataobjectinstance` WHERE `id` = "+object_instance_id;
        return this.executeInsertStatement(sql);
    }

    /**
     * This method returns the DataObjectInstances log entries for a ScenarioInstance.
     *
     * @param scenarioInstanceId ID of the ScenarioInstance for which the DataObjectInstance log entries shall be returned.
     * @return a Map with a Map of the log entries' attribute names as keys and their respective values.
     */
    public Map<Integer, Map<String, Object>> getLogEntriesForScenarioInstance(int scenarioInstanceId){
        String sql = "SELECT * FROM historydataobjectinstance WHERE scenarioinstance_id = "+scenarioInstanceId+" ORDER BY timestamp DESC";
        return this.executeStatementReturnsMapWithMapWithKeys(sql, "scenarioinstance_id", "name", "timestamp","dataobjectinstance_id", "old_state_id", "old_state_name", "new_state_id", "new_state_name");
    }



}
