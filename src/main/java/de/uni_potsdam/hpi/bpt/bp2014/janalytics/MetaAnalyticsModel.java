package de.uni_potsdam.hpi.bpt.bp2014.janalytics;


import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;

import org.apache.log4j.Logger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    // **************************** HELPER ********************************************************

    /**
     * @param sql
     * @param keys
     * @return
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
     * @param sql
     * @return
     */
    public static ArrayList<HashMap<String, Object>> executeStatementReturnsHashMap(String sql) {
        java.sql.Connection conn = de.uni_potsdam.hpi.bpt.bp2014.database.Connection.getInstance().connect();
        Statement stmt = null;
        try {
            //Execute a query
            stmt = conn.createStatement();

            //query
            ResultSet Result = null;
            boolean Returning_Rows = stmt.execute(sql);
            if (Returning_Rows)
                Result = stmt.getResultSet();
            else
                return new ArrayList<HashMap<String, Object>>();

            //get metadata
            ResultSetMetaData Meta = null;
            Meta = Result.getMetaData();

            //get column names
            int Col_Count = Meta.getColumnCount();
            ArrayList<String> Cols = new ArrayList<String>();
            for (int Index = 1; Index <= Col_Count; Index++)
                Cols.add(Meta.getColumnName(Index));

            //fetch out rows
            ArrayList<HashMap<String, Object>> Rows =
                    new ArrayList<HashMap<String, Object>>();

            while (Result.next()) {
                HashMap<String, Object> Row = new HashMap<String, Object>();
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
