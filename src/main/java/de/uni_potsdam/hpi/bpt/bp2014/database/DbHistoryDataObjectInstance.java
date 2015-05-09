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
     * @param state_id           the new state of the DataObjectInstance.
     * @return the generated key for the insert statement.
     */
    public int createEntry(int object_instance_id, int state_id) {
        String sql = "INSERT INTO `historydataobjectinstance` (`scenarioinstance_id`,`dataobjectinstance_id`,`oldstate_id`,`newstate_id`) " +
                "SELECT (SELECT `scenarioinstance_id` FROM `dataobjectinstance` WHERE `id` = " + object_instance_id + ") AS`scenarioinstance_id`," +
                " `id`," +
                "(SELECT `state_id` FROM `dataobjectinstance` WHERE `id` = " + object_instance_id + ") AS `oldstate_id`, " +
                "" + state_id + " AS `newstate_id` " +
                "FROM `dataobjectinstance` WHERE `id` = " + object_instance_id;
        return this.executeInsertStatement(sql);
    }

    /**
     * This method saves a log entry of a newly created DataObjectInstance into the database.
     *
     * @param object_instance_id the ID of the DataObjectInstance that is created.
     * @return the generated key for the insert statement.
     */
    public int createNewDataObjectInstanceEntry(int object_instance_id) {
        String sql = "INSERT INTO `historydataobjectinstance` (`scenarioinstance_id`,`dataobjectinstance_id`,`newstate_id`) " +
                "SELECT (SELECT `scenarioinstance_id` FROM `dataobjectinstance` WHERE `id` = " + object_instance_id + ") AS`scenarioinstance_id`, " +
                "`id`, " +
                "(SELECT `state_id` FROM `dataobjectinstance` WHERE `id` = " + object_instance_id + ") AS `newstate_id` " +
                "FROM `dataobjectinstance` WHERE `id` = " + object_instance_id;
        return this.executeInsertStatement(sql);
    }

    /**
     * This method returns the DataObjectInstances log entries for a ScenarioInstance.
     *
     * @param scenarioInstanceId ID of the ScenarioInstance for which the DataObjectInstance log entries shall be returned.
     * @return a Map with a Map of the log entries' attribute names as keys and their respective values.
     */
    public Map<Integer, Map<String, Object>> getLogEntriesForScenarioInstance(int scenarioInstanceId) {
        String sql = "SELECT h.id, h.scenarioinstance_id, h.timestamp, h.oldstate_id, h.newstate_id, h.dataobjectinstance_id, do.name, ns.name AS newstate_name, os.name AS oldstate_name FROM historydataobjectinstance AS h, dataobjectinstance AS doi, dataobject AS do, state AS ns, state AS os WHERE h.scenarioinstance_id = " + scenarioInstanceId + " AND ns.id = h.newstate_id AND os.id = h.oldstate_id ORDER BY timestamp DESC";
        return this.executeStatementReturnsMapWithMapWithKeys(sql, "h.id", "h.oldstate_id", "h.newstate_id", "h.scenarioinstance_id", "do.name", "h.timestamp", "h.dataobjectinstance_id", "oldstate_name", "newstate_name");
    }


}
