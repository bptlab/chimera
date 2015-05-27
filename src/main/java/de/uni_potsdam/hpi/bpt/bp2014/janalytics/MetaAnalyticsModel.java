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
    static Logger log = Logger.getLogger(MetaAnalyticsModel.class.getName());

    // **************************** DB Execution Statements ********************************************************

    /**
     * This method returns the Activity log entries for a ScenarioInstance.
     *
     * @param scenarioInstanceId ID of the ScenarioInstance for which the activity log entries shall be returned.
     * @return a Map with a Map of the log entries' attribute names as keys and their respective values.
     */
    public static Map<Integer, Map<String, Object>> getLogEntriesForScenarioInstanceWithinActivity(int scenarioInstanceId) {
        String sql = "SELECT h.id, h.scenarioinstance_id, cn.label, h.activityinstance_id, h.oldstate, h.newstate, h.timestamp FROM historyactivityinstance AS h, controlnode AS cn, controlnodeinstance AS cni WHERE h.scenarioinstance_id = " + scenarioInstanceId + "  AND h.activityinstance_id = cni.id AND cni.controlnode_id = cn.id  ORDER BY timestamp DESC";
        return executeStatementReturnsMapWithMapWithKeys(sql, "h.id", "h.scenarioinstance_id", "cn.label", "h.activityinstance_id", "h.oldstate", "h.newstate", "h.timestamp");
    }

    /**
     * This method returns the DataAttributeInstance log entries for a ScenarioInstance.
     *
     * @param scenarioInstanceId ID of the ScenarioInstance for which the DataAttributeInstance log entries shall be returned.
     * @return a Map with a Map of the log entries' attribute names as keys and their respective values.
     */
    public static Map<Integer, Map<String, Object>> getLogEntriesForScenarioInstanceWithinDataAttribute(int scenarioInstanceId) {
        String sql = "SELECT h.id, h.scenarioinstance_id, h.timestamp, h.oldvalue, h.newvalue, h.dataattributeinstance_id, da.name, do.name FROM historydataattributeinstance AS h, dataattributeinstance AS dai, dataattribute AS da, dataobjectinstance AS doi, dataobject AS do WHERE h.scenarioinstance_id = " + scenarioInstanceId + " AND h.dataattributeinstance_id = dai.id AND dai.dataattribute_id = da.id AND dai.dataobjectinstance_id = doi.id AND doi.dataobject_id = do.id ORDER BY timestamp DESC";
        return executeStatementReturnsMapWithMapWithKeys(sql, "h.id", "h.scenarioinstance_id", "da.name", "h.timestamp", "h.oldvalue", "h.newvalue", "h.dataattributeinstance_id", "do.name");
    }

    /**
     * This method returns the DataObjectInstances log entries for a ScenarioInstance.
     *
     * @param scenarioInstanceId ID of the ScenarioInstance for which the DataObjectInstance log entries shall be returned.
     * @return a Map with a Map of the log entries' attribute names as keys and their respective values.
     */
    public static Map<Integer, Map<String, Object>> getLogEntriesForScenarioInstanceWithinDataObject(int scenarioInstanceId) {
        String sql = "SELECT h.id, h.scenarioinstance_id, h.timestamp, h.oldstate_id, h.newstate_id, h.dataobjectinstance_id, do.name, ns.name AS newstate_name, os.name AS oldstate_name FROM historydataobjectinstance AS h, dataobjectinstance AS doi, dataobject AS do, state AS ns, state AS os WHERE h.scenarioinstance_id = " + scenarioInstanceId + " AND ns.id = h.newstate_id AND os.id = h.oldstate_id ORDER BY timestamp DESC";
        return executeStatementReturnsMapWithMapWithKeys(sql, "h.id", "h.oldstate_id", "h.newstate_id", "h.scenarioinstance_id", "do.name", "h.timestamp", "h.dataobjectinstance_id", "oldstate_name", "newstate_name");
    }

    /**
     * This method returns the timestamps of the start and end of a scenario instance.
     *
     * @param scenarioinstance_id ID of the ScenarioInstance for which the start and end timestamps shall be returned.
     * @return a Map with a Map of the start and end log entries timestamp as keys and their respective dates as values.
     */
    public static Map<Integer, Map<String, Object>> getLogTimestampsForScenarioInstance(int scenarioinstance_id) {
        String sql = "SELECT MAX(timestamp) AS end_timestamp, MIN(timestamp) AS start_timestamp FROM `historydataobjectinstance` as h, scenarioinstance as s WHERE h.scenarioinstance_id = " + scenarioinstance_id + " AND h.scenarioinstance_id = s.id AND s.terminated = 1";
        return executeStatementReturnsMapWithMapWithKeys(sql, "start_timestamp", "end_timestamp");
    }

    /**
     * @param scenario_id The ID of the scenario.
     * @return a List
     */
    public static List<ExampleAlgorithm.DbScenarioInstanceIDsAndTimestamps> getScenarioInstancesForScenario(int scenario_id) {
        String sql = "SELECT scenarioinstance.id FROM scenarioinstance WHERE scenarioinstance.terminated = 1 AND scenarioinstance.scenario_id = " + scenario_id;
        java.sql.Connection conn = Connection.getInstance().connect();
        ResultSet results = null;
        List<ExampleAlgorithm.DbScenarioInstanceIDsAndTimestamps> scenarioInstances = new ArrayList<>();
        try {
            results = conn.prepareStatement(sql).executeQuery();
            while (results.next()) {
                scenarioInstances.add(new ExampleAlgorithm.DbScenarioInstanceIDsAndTimestamps(results.getInt("scenarioinstance.id")));
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
                results.close();
            } catch (SQLException e) {
                log.error("SQL Error!: ", e);
            }
        }

        return scenarioInstances;
    }


    // **************************** HELPER ********************************************************

    /**
     * @param sql  the sql statement which is supposed to be executed
     * @param keys for select statement
     * @return a map of maps with keys
     */
    public static Map<Integer, Map<String, Object>> executeStatementReturnsMapWithMapWithKeys(String sql, String... keys) {
        java.sql.Connection conn = Connection.getInstance().connect();
        ResultSet results = null;
        Map<Integer, Map<String, Object>> keysValues = new HashMap<>();
        try {
            results = conn.prepareStatement(sql).executeQuery();
            while (results.next()) {
                keysValues.put(results.getInt("id"), new HashMap<String, Object>());
                for (String key : keys) {
                    (keysValues.get(results.getInt("id"))).put(key, results.getObject(key));
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
                results.close();
            } catch (SQLException e) {
                log.error("SQL Error!: ", e);
            }
        }
        return keysValues;
    }


    /**
     * @param sql the sql statement which is supposed to be executed
     * @return a hashmap
     */
    public static ArrayList<HashMap<String, Object>> executeStatementReturnsHashMap(String sql) {
        java.sql.Connection conn = de.uni_potsdam.hpi.bpt.bp2014.database.Connection.getInstance().connect();
        Statement stmt = null;
        try {
            //Execute a query
            stmt = conn.createStatement();

            //query
            ResultSet Result;
            boolean Returning_Rows = stmt.execute(sql);
            if (Returning_Rows)
                Result = stmt.getResultSet();
            else
                return new ArrayList<>();

            //get metadata
            ResultSetMetaData Meta;
            Meta = Result.getMetaData();

            //get column names
            int Col_Count = Meta.getColumnCount();
            ArrayList<String> Cols = new ArrayList<>();
            for (int Index = 1; Index <= Col_Count; Index++)
                Cols.add(Meta.getColumnName(Index));

            //fetch out rows
            ArrayList<HashMap<String, Object>> Rows =
                    new ArrayList<>();

            while (Result.next()) {
                HashMap<String, Object> Row = new HashMap<>();
                for (String Col_Name : Cols) {
                    Object Val = Result.getObject(Col_Name);
                    Row.put(Col_Name, Val);
                }
                Rows.add(Row);
            }

            //close statement
            stmt.close();

            //pass back rows
            return Rows;
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
