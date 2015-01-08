package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

/**
 * Created by jaspar.mang on 07.01.15.
 */
public class DbDataNode {
    public LinkedList<Integer> getDataObjectIdsForDataSets(int dataSet_id) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        LinkedList<Integer> results = new LinkedList<Integer>();
        if (conn == null) return results;

        try {
            //Execute a query
            stmt = conn.createStatement();
            String sql = "Select dataobject_id FROM datanode, datasetconsistsofdatanode WHERE datanode.id = datasetconsistsofdatanode.datanode_id AND dataset_id = " + dataSet_id + " ORDER BY dataobject_id";
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                results.add(rs.getInt("dataobject_id"));
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
    public LinkedList<Integer> getDataStatesForDataSets(int dataSet_id) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        LinkedList<Integer> results = new LinkedList<Integer>();
        if (conn == null) return results;

        try {
            //Execute a query
            stmt = conn.createStatement();
            String sql = "Select state_id FROM datanode, datasetconsistsofdatanode WHERE datanode.id = datasetconsistsofdatanode.datanode_id AND dataset_id = " + dataSet_id + " ORDER BY dataobject_id";
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                results.add(rs.getInt("state_id"));
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
