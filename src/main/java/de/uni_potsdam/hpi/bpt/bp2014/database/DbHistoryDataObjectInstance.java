package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.Map;

/**
 * This class provides the actual implementation for the logging of DataObjectInstances
 * and the retrieval of log entries for presentation.
 */

public class DbHistoryDataObjectInstance extends DbObject {
	/**
	 * This method saves a log entry with a DataAttributeInstance value change in the database.
	 *
	 * @param objectInstanceId the ID of the DataObjectInstance that is changed.
	 * @param stateId           the new state of the DataObjectInstance.
	 * @return the generated key for the insert statement.
	 */
	public int createEntry(int objectInstanceId, int stateId) {
		String sql =
				"INSERT INTO `historydataobjectinstance` ("
						+ "`scenarioinstance_id`,`dataobjectinstance_id`,"
						+ "`oldstate_id`,`newstate_id`) "
						+ "SELECT (SELECT `scenarioinstance_id` "
						+ "FROM `dataobjectinstance` "
						+ "WHERE `id` = " + objectInstanceId
						+ ") AS`scenarioinstance_id`, `id`,"
						+ "(SELECT `state_id` FROM `dataobjectinstance` "
						+ "WHERE `id` = " + objectInstanceId
						+ ") AS `oldstate_id`, "
						+ stateId + " AS `newstate_id` "
						+ "FROM `dataobjectinstance` "
						+ "WHERE `id` = " + objectInstanceId;
		return this.executeInsertStatement(sql);
	}

	/**
	 * This method saves a log entry of a newly created DataObjectInstance into the database.
	 *
	 * @param objectInstanceId the ID of the DataObjectInstance that is created.
	 * @return the generated key for the insert statement.
	 */
	public int createNewDataObjectInstanceEntry(int objectInstanceId) {
		String sql =
				"INSERT INTO `historydataobjectinstance` (`scenarioinstance_id`,"
						+ "`dataobjectinstance_id`,`newstate_id`) "
						+ "SELECT (SELECT `scenarioinstance_id` "
						+ "FROM `dataobjectinstance` "
						+ "WHERE `id` = "	+ objectInstanceId
						+ ") AS`scenarioinstance_id`, `id`, "
						+ "(SELECT `state_id` FROM `dataobjectinstance` "
						+ "WHERE `id` = " + objectInstanceId
						+ ") AS `newstate_id` "
						+ "FROM `dataobjectinstance` "
						+ "WHERE `id` = " + objectInstanceId;
		return this.executeInsertStatement(sql);
	}

	/**
	 * This method returns the DataObjectInstances log entries for a ScenarioInstance.
	 *
	 * @param scenarioInstanceId ID of the ScenarioInstance.
	 * @return a Map with a Map of the log entries' attribute names as keys with their values.
	 */
	public Map<Integer, Map<String, Object>> getLogEntriesForScenarioInstance(
			int scenarioInstanceId) {
		String sql =
				"SELECT h.id, h.scenarioinstance_id, h.timestamp, h.oldstate_id, "
						+ "h.newstate_id, h.dataobjectinstance_id, "
						+ "do.name, ns.name AS newstate_name, "
						+ "os.name AS oldstate_name "
						+ "FROM historydataobjectinstance AS h, "
						+ "dataobjectinstance AS doi, dataobject AS do, "
						+ "state AS ns, state AS os "
						+ "WHERE h.scenarioinstance_id = "
						+ scenarioInstanceId + " "
						+ "AND ns.id = h.newstate_id "
						+ "AND os.id = h.oldstate_id "
						+ "AND h.dataobjectinstance_id = doi.id "
						+ "AND doi.dataobject_id = do.id "
						+ "ORDER BY timestamp DESC";
		return this.executeStatementReturnsMapWithMapWithKeys(sql, "h.id", "h.oldstate_id",
				"h.newstate_id", "h.scenarioinstance_id", "do.name", "h.timestamp",
				"h.dataobjectinstance_id", "oldstate_name", "newstate_name");
	}

}
