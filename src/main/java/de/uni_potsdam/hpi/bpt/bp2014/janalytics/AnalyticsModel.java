package de.uni_potsdam.hpi.bpt.bp2014.janalytics;

import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class AnalyticsModel {
    static Logger log = Logger.getLogger(Connection.class.getName());

    /**
     *
     * @param scenarioInstance_id
     * @return
     */
    public ArrayList<HashMap<String, Object>> exampleAlgorithm1(int scenarioInstance_id) {

        String sql = "";
        return this.executeStatementReturnsHashMap(sql);
    }

    /**
     *
     * @param sql
     * @return
     */
    public static ArrayList<HashMap<String,Object>> executeStatementReturnsHashMap(String sql) {
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
                return new ArrayList<HashMap<String,Object>>();

            //get metadata
            ResultSetMetaData Meta = null;
            Meta = Result.getMetaData();

            //get column names
            int Col_Count = Meta.getColumnCount();
            ArrayList<String> Cols = new ArrayList<String>();
            for (int Index=1; Index<=Col_Count; Index++)
                Cols.add(Meta.getColumnName(Index));

            //fetch out rows
            ArrayList<HashMap<String,Object>> Rows =
                    new ArrayList<HashMap<String,Object>>();

            while (Result.next()) {
                HashMap<String,Object> Row = new HashMap<String,Object>();
                for (String Col_Name:Cols) {
                    Object Val = Result.getObject(Col_Name);
                    Row.put(Col_Name,Val);
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
