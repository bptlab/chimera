package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.Map;


public class DbHistoryDataAttributeInstance extends DbObject {

    /**
     * This method saves a log entry containing a DataAttributeInstance value change into the database.
     *
     * @param data_attribute_instance_id the ID of the DataAttributeInstance that is changed.
     * @param value                      the new value of the DataAttributeInstance.
     * @return the generated key for the insert statement.
     */
    public int createEntry(int data_attribute_instance_id, Object value) {
        String sql = "INSERT INTO `historydataattributeinstance` (`scenarioinstance_id`,`dataattributeinstance_id`,`oldvalue`,`newvalue`) " +
                "SELECT (SELECT `scenarioinstance_id` FROM `dataattributeinstance`, dataobjectinstance WHERE dataobjectinstance.id = dataattributeinstance.dataobjectinstance_id AND dataattributeinstance.id = " + data_attribute_instance_id + ") AS `scenarioinstance_id`," +
                " `id` AS dataattributeinstance_id, " +
                "(SELECT `value` FROM `dataattributeinstance` WHERE `id` = " + data_attribute_instance_id + ") AS `oldvalue`, " +
                "(SELECT '" + value + "') AS `newvalue` " +
                "FROM `dataattributeinstance` WHERE `id` = " + data_attribute_instance_id;
        return this.executeInsertStatement(sql);
    }

    /**
     * This method saves a log entry of a newly created DataAttributeInstance into the database.
     *
     * @param data_attribute_instance_id the ID of the DataAttributeInstance that is created.
     * @return the generated key for the insert statement.
     */
    public int createNewDataAttributeInstanceEntry(int data_attribute_instance_id) {
        String sql = "INSERT INTO `historydataattributeinstance` (`scenarioinstance_id`,`dataattributeinstance_id`,`newvalue`) " +
                "SELECT (SELECT `scenarioinstance_id` FROM `dataattributeinstance`, dataobjectinstance WHERE dataobjectinstance.id = dataattributeinstance.dataobjectinstance_id AND dataattributeinstance.id = " + data_attribute_instance_id + ") AS `scenarioinstance_id`," +
                " `id` AS dataattributeinstance_id, " +
                "(SELECT `value` FROM `dataattributeinstance` WHERE `id` = " + data_attribute_instance_id + ") AS `newvalue` " +
                "FROM `dataattributeinstance` WHERE `id` = " + data_attribute_instance_id;
        return this.executeInsertStatement(sql);
    }

    /**
     * This method returns the DataAttributeInstance log entries for a ScenarioInstance.
     *
     * @param scenarioInstanceId ID of the ScenarioInstance for which the DataAttributeInstance log entries shall be returned.
     * @return a Map with a Map of the log entries' attribute names as keys and their respective values.
     */
    public Map<Integer, Map<String, Object>> getLogEntriesForScenarioInstance(int scenarioInstanceId) {
        String sql = "SELECT h.id, h.scenarioinstance_id, h.timestamp, h.oldvalue, h.newvalue, h.dataattributeinstance_id, da.name, do.name FROM historydataattributeinstance AS h, dataattributeinstance AS dai, dataattribute AS da, dataobjectinstance AS doi, dataobject AS do WHERE h.scenarioinstance_id = " + scenarioInstanceId + " AND h.dataattributeinstance_id = dai.id AND dai.dataattribute_id = da.id AND dai.dataobjectinstance_id = doi.id AND doi.dataobject_id = do.id ORDER BY timestamp DESC";
        return this.executeStatementReturnsMapWithMapWithKeys(sql, "h.id", "h.scenarioinstance_id", "da.name", "h.timestamp", "h.oldvalue", "h.newvalue", "h.dataattributeinstance_id", "do.name");
    }
}
