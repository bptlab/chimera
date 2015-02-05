package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

/**
 * Created by jaspar.mang on 04.02.15.
 */
public class DbTerminationCondition {
    public LinkedList<Condition> getConditionsForScenario(int scenario_id) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        LinkedList<Condition> results = new LinkedList<Condition>();
        if (conn == null) return results;

        try {
            //Execute a query
            stmt = conn.createStatement();
            String sql = "SELECT conditionset_id, dataobject_id, state_id, scenario_id FROM terminationcondition WHERE scenario_id = " + scenario_id;
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                results.add(new Condition(rs.getInt("conditionset_id"), rs.getInt("state_id"), rs.getInt("dataobject_id"), rs.getInt("scenario_id")));
            }
            //Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return results;
    }
    public LinkedList<Integer> getConditionsSetIDsForScenario(int scenario_id) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        LinkedList<Integer> results = new LinkedList<Integer>();
        if (conn == null) return results;

        try {
            //Execute a query
            stmt = conn.createStatement();
            String sql = "SELECT conditionset_id FROM terminationcondition WHERE scenario_id = " + scenario_id +" GROUP BY conditionset_id";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                results.add(rs.getInt("conditionset_id"));
            }
            //Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return results;
    }
    public LinkedList<Condition> getConditionsForConditionSetAndScenario(int scenario_id, int conditionSet_id) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        LinkedList<Condition> results = new LinkedList<Condition>();
        if (conn == null) return results;

        try {
            //Execute a query
            stmt = conn.createStatement();
            String sql = "SELECT conditionset_id, dataobject_id, state_id, scenario_id FROM terminationcondition WHERE scenario_id = " + scenario_id + " AND conditionset_id = " + conditionSet_id;
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                results.add(new Condition(rs.getInt("conditionset_id"), rs.getInt("state_id"), rs.getInt("dataobject_id"), rs.getInt("scenario_id")));
            }
            //Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return results;
    }
}
