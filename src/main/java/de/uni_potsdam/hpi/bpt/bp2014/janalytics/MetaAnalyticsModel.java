package de.uni_potsdam.hpi.bpt.bp2014.janalytics;

import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class MetaAnalyticsModel {

	private static Logger log = Logger.getLogger(MetaAnalyticsModel.class);

	// **************************** DB Execution Statements ****************************

	/**
	 * This method returns the Activity log entries for a ScenarioInstance.
	 *
	 * @param scenarioInstanceId This is the database ID of a ScenarioInstance.
	 * @return a Map with a Map of the log entries' attribute names as keys with values.
	 */
	public static Map<Integer, Map<
			String, Object>> getLogEntriesForScenarioInstanceWithinActivity(
			int scenarioInstanceId) {
		String sql =
				"SELECT h.id, h.scenarioinstance_id, cn.label, "
						+ "h.activityinstance_id, h.oldstate, "
						+ "h.newstate, h.timestamp "
						+ "FROM historyactivityinstance AS h, "
						+ "controlnode AS cn, "
						+ "controlnodeinstance AS cni "
						+ "WHERE h.scenarioinstance_id = "
						+ scenarioInstanceId + " "
						+ "AND h.activityinstance_id = cni.id "
						+ "AND cni.controlnode_id = cn.id"
						+ "ORDER BY timestamp DESC";
		return executeStatementReturnsMapWithMapWithKeys(sql, "h.id",
				"h.scenarioinstance_id", "cn.label", "h.activityinstance_id",
				"h.oldstate", "h.newstate", "h.timestamp");
	}

	/**
	 * This method returns the DataAttributeInstance log entries for a ScenarioInstance.
	 *
	 * @param scenarioInstanceId This is the database ID of the ScenarioInstance.
	 * @return a Map with a Map of the log entries' attribute names as keys with values.
	 */
	public static Map<Integer, Map<
			String, Object>> getLogEntriesForScenarioInstanceWithinDataAttribute(
			int scenarioInstanceId) {
		String sql =
				"SELECT h.id, h.scenarioinstance_id, h.timestamp, h.oldvalue, "
						+ "h.newvalue, h.dataattributeinstance_id, "
						+ "da.name, do.name "
						+ "FROM historydataattributeinstance AS h, "
						+ "dataattributeinstance AS dai, "
						+ "dataattribute AS da, "
						+ "dataobjectinstance AS doi, "
						+ "dataobject AS do "
						+ "WHERE h.scenarioinstance_id = "
						+ scenarioInstanceId
						+ " AND h.dataattributeinstance_id = dai.id "
						+ "AND dai.dataattribute_id = da.id "
						+ "AND dai.dataobjectinstance_id = doi.id "
						+ "AND doi.dataobject_id = do.id "
						+ "ORDER BY timestamp DESC";
		return executeStatementReturnsMapWithMapWithKeys(sql, "h.id",
				"h.scenarioinstance_id", "da.name", "h.timestamp", "h.oldvalue",
				"h.newvalue", "h.dataattributeinstance_id",	"do.name");
	}

	/**
	 * This method returns the DataObjectInstances log entries for a ScenarioInstance.
	 *
	 * @param scenarioInstanceId This is the database ID of the ScenarioInstance.
	 * @return a Map with a Map of the log entries' attribute names as keys with values.
	 */
	public static Map<Integer, Map<
			String, Object>> getLogEntriesForScenarioInstanceWithinDataObject(
			int scenarioInstanceId) {
		String sql =
				"SELECT h.id, h.scenarioinstance_id, h.timestamp, h.oldstate_id, "
						+ "h.newstate_id, h.dataobjectinstance_id, "
						+ "do.name, ns.name AS newstate_name, "
						+ "os.name AS oldstate_name "
						+ "FROM historydataobjectinstance AS h, "
						+ "dataobjectinstance AS doi, "
						+ "dataobject AS do, state AS ns, "
						+ "state AS os "
						+ "WHERE h.scenarioinstance_id = "
						+ scenarioInstanceId
						+ " AND ns.id = h.newstate_id "
						+ "AND os.id = h.oldstate_id "
						+ "ORDER BY timestamp DESC";
		return executeStatementReturnsMapWithMapWithKeys(sql, "h.id", "h.oldstate_id",
				"h.newstate_id", "h.scenarioinstance_id", "do.name", "h.timestamp",
				"h.dataobjectinstance_id", "oldstate_name", "newstate_name");
	}

	/**
	 * This method returns the timestamps of the start and end of a scenario instance.
	 *
	 * @param scenarioinstanceId This is the database ID of the ScenarioInstance.
	 * @return a Map with a Map of the start and end log entries timestamp as keys with values.
	 */
	public static Map<Integer, Map<String, Object>> getLogTimestampsForScenarioInstance(
			int scenarioinstanceId) {
		String sql =
				"SELECT MAX(timestamp) AS end_timestamp, MIN(timestamp) "
						+ "AS start_timestamp "
						+ "FROM `historydataobjectinstance` as h, "
						+ "scenarioinstance as s "
						+ "WHERE h.scenarioinstance_id = "
						+ scenarioinstanceId
						+ " AND h.scenarioinstance_id = s.id "
						+ "AND s.terminated = 1";
		return executeStatementReturnsMapWithMapWithKeys(
				sql, "start_timestamp", "end_timestamp");
	}

	/**
	 * @param scenarioId The ID of the scenario.
	 * @return a List
	 */
	public static List<ExampleAlgorithm.DbScenarioInstanceIDsAndTimestamps>
			getScenarioInstancesForScenario(int scenarioId) {
		String sql =
				"SELECT scenarioinstance.id FROM scenarioinstance "
						+ "WHERE scenarioinstance.terminated = 1 "
						+ "AND scenarioinstance.scenario_id = "
						+ scenarioId;
		java.sql.Connection conn = Connection.getInstance().connect();
		ResultSet results = null;
		List<ExampleAlgorithm.DbScenarioInstanceIDsAndTimestamps>
				scenarioInstances = new ArrayList<>();
		try {
			results = conn.prepareStatement(sql).executeQuery();
			while (results.next()) {
				scenarioInstances.add(new
						ExampleAlgorithm.DbScenarioInstanceIDsAndTimestamps(
						results.getInt("scenarioinstance.id")));
			}

		} catch (SQLException e) {
			log.error("SQL Error!: ", e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error("SQL Error!: ", e);
			}
			try {
				if (results != null) {
					results.close();
				}
			} catch (SQLException e) {
				log.error("SQL Error!: ", e);
			}
		}

		return scenarioInstances;
	}

	// **************************** HELPER ****************************

	/**
	 * @param sql  the sql statement which is supposed to be executed
	 * @param keys for select statement
	 * @return a map of maps with keys
	 */
	public static Map<Integer, Map<String, Object>> executeStatementReturnsMapWithMapWithKeys(
			String sql, String... keys) {
		java.sql.Connection conn = Connection.getInstance().connect();
		ResultSet results = null;
		Map<Integer, Map<String, Object>> keysValues = new HashMap<>();
		try {
			results = conn.prepareStatement(sql).executeQuery();
			while (results.next()) {
				keysValues.put(results.getInt("id"), new HashMap<String, Object>());
				for (String key : keys) {
					(keysValues.get(results.getInt("id"))).put(
							key, results.getObject(key));
				}
			}
		} catch (SQLException e) {
			log.error("SQL Error!: ", e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error("SQL Error!: ", e);
			}
			try {
				if (results != null) {
					results.close();
				}
			} catch (SQLException e) {
				log.error("SQL Error!: ", e);
			}
		}
		return keysValues;
	}

	/**
	 * @param sql the sql statement which is supposed to be executed
	 * @return an ArrayList with Maps containing the query results
	 */
	public static ArrayList<Map<String, Object>> executeStatementReturnsHashMap(String sql) {
		java.sql.Connection conn =
				de.uni_potsdam.hpi.bpt.bp2014.database.Connection.getInstance()
				.connect();
		Statement stmt = null;
		try {
			//Execute a query
			stmt = conn.createStatement();

			//query
			ResultSet result;
			boolean returningRows = stmt.execute(sql);
			if (returningRows) {
				result = stmt.getResultSet();
			} else {
				return new ArrayList<>();
			}

			//get metadata
			ResultSetMetaData meta;
			meta = result.getMetaData();

			//get column names
			int columnCount = meta.getColumnCount();
			ArrayList<String> columns = new ArrayList<>();
			for (int index = 1; index <= columnCount; index++) {
				columns.add(meta.getColumnName(index));
			}

			//fetch out rows
			ArrayList<Map<String, Object>> rows = new ArrayList<>();

			while (result.next()) {
				Map<String, Object> row = new HashMap<>();
				for (String columnName : columns) {
					Object val = result.getObject(columnName);
					row.put(columnName, val);
				}
				rows.add(row);
			}

			//close statement
			stmt.close();

			//pass back rows
			return rows;
		} catch (SQLException se) {
			//Handle errors for JDBC
			log.error("SQL Error!: ", se);
		} finally {
			//finally block used to close resources
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException se2) {
				log.error("SQL Error!: ", se2);
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				log.error("SQL Error!: ", se);
			}
		}
		return null;
	}
}
