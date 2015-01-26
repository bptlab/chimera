package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by jaspar.mang on 05.01.15.
 */
public class DbDataObjectInstance {

    public Boolean existDataObjectInstance(int scenarioInstance_id, int dataObject_id) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        if (conn == null) return false;

        try {
            //Execute a query
            stmt = conn.createStatement();
            String sql = "SELECT id FROM dataobjectinstance WHERE scenarioinstance_id = " + scenarioInstance_id +" AND dataobject_id = "+ dataObject_id;
            rs = stmt.executeQuery(sql);
            if(rs.next()) {
                return true;
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
        return false;
    }
    public void setState(int id, int state) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        if (conn == null) return;

        try {
            //Execute a query
            stmt = conn.createStatement();
            String sql = "UPDATE dataobjectinstance SET state_id = " + state + " WHERE id = " + id;
            stmt.executeUpdate(sql);
            //Clean-up environment
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
    }
    public int createNewDataObjectInstance(int scenarioInstance_id, int state_id, int dataObject_id) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        if (conn == null) return -1;
        int result = -1;
        try {
            //Execute a query
            stmt = conn.createStatement();
            String sql = "INSERT INTO dataobjectinstance (scenarioinstance_id, state_id, dataobject_id) VALUES (" + scenarioInstance_id + ", "+ state_id +", " + dataObject_id+")";
            result = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            //Clean-up environment
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
        return result;
    }
    public int getDataObjectInstanceID(int scenarioInstance_id, int dataObject_id) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        int results = -1;
        if (conn == null) return results;

        try {
            //Execute a query
            stmt = conn.createStatement();
            String sql = "SELECT id FROM dataobjectinstance WHERE dataobject_id = " + dataObject_id + " AND scenarioinstance_id = "+ scenarioInstance_id;
            rs = stmt.executeQuery(sql);
            rs.next();
            results = rs.getInt("id");
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
    public int getStateID(int dataObjectInstance_id) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        int results = -1;
        if (conn == null) return results;

        try {
            //Execute a query
            stmt = conn.createStatement();
            String sql = "SELECT state_id FROM dataobjectinstance WHERE id = " + dataObjectInstance_id;
            rs = stmt.executeQuery(sql);
            rs.next();
            results = rs.getInt("state_id");
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
