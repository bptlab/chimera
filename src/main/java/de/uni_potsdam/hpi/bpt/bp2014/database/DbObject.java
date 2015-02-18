package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jaspar.mang on 17.02.15.
 */
public class DbObject {
    /**
     * Executes SQL statement and returns the result.
     * @return
     */
    public List<Integer> executeStatementReturnListInt(String sql, String column){
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        LinkedList<Integer> results = new LinkedList<Integer>();
        if (conn == null) return results;

        try {
            //Execute a query
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                results.add(rs.getInt(column));
            }
            //Clean-up environment
            rs.close();
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

    public String executeStatementReturnString(String sql, String column){
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        String results = "";
        if (conn == null) {
            return results;
        }
        try {
            //Execute a query
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                results = rs.getString(column);
            }
            //Clean-up environment
            rs.close();
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

    public int executeStatementReturnsInt(String sql, String column){
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        int results = -1;
        if (conn == null) return results;

        try {
            //Execute a query
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                results = rs.getInt(column);
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

    public boolean executeExistStatement(String sql){
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        if (conn == null) {
            return false;
        }

        try {
            //Execute a query
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()) {
                return true;
            }
            //Clean-up environment
            rs.close();
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

    public int executeInsertStatement(String sql){
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        if (conn == null) return -1;
        int result = -1;
        try {
            //Execute a query
            stmt = conn.createStatement();
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                result = rs.getInt(1);
            }
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

}
