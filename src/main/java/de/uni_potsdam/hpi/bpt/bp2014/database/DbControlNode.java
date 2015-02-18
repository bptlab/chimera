package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * ********************************************************************************
 * <p/>
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 * <p/>
 * ******************************************************************
 * <p/>
 * Copyright © All Rights Reserved 2014 - 2015
 * <p/>
 * Please be aware of the License. You may found it in the root directory.
 * <p/>
 * **********************************************************************************
 */


public class DbControlNode extends DbObject {
    public int getStartEventID(int fragment_id) {
        String sql = "SELECT id FROM controlnode WHERE type = 'Startevent' AND fragment_id = " + fragment_id;
        return this.executeStatementReturnsInt(sql, "id");
    }

    public String getType(int controlNode_id) {
        String sql = "SELECT type FROM controlnode WHERE id = " + controlNode_id;
        return this.executeStatementReturnsString(sql, "type");
    }

    public String getLabel(int controlNode_id) {
        String sql = "SELECT label FROM controlnode WHERE id = " + controlNode_id;
        return this.executeStatementReturnsString(sql, "label");
    }

    public Boolean controlNodesHaveSameOutputs(int controlNode_id1, int controlNode_id2) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        if (conn == null) return false;

        try {
            //Execute a query
            stmt = conn.createStatement();
            String sql = "(SELECT dataobject_id, state_id FROM datasetconsistsofdatanode, datanode, dataflow " +
                    "WHERE dataflow.dataset_id = datasetconsistsofdatanode.dataset_id AND " +
                    "datasetconsistsofdatanode.datanode_id = datanode.id AND controlnode_id = " + controlNode_id1 + " AND " +
                    "dataflow.input = 0 AND (dataobject_id, state_id) NOT IN " +
                    "(SELECT dataobject_id, state_id FROM datasetconsistsofdatanode, datanode, dataflow " +
                    "WHERE dataflow.dataset_id = datasetconsistsofdatanode.dataset_id AND " +
                    "datasetconsistsofdatanode.datanode_id = datanode.id AND controlnode_id = " + controlNode_id2 + " AND " +
                    "dataflow.input = 0)) UNION (SELECT dataobject_id, state_id " +
                    "FROM datasetconsistsofdatanode, datanode, dataflow " +
                    "WHERE dataflow.dataset_id = datasetconsistsofdatanode.dataset_id AND " +
                    "datasetconsistsofdatanode.datanode_id = datanode.id AND controlnode_id = " + controlNode_id2 + " " +
                    "AND dataflow.input = 0 AND (dataobject_id, state_id) NOT IN " +
                    "(SELECT dataobject_id, state_id FROM datasetconsistsofdatanode, datanode, dataflow " +
                    "WHERE dataflow.dataset_id = datasetconsistsofdatanode.dataset_id AND " +
                    "datasetconsistsofdatanode.datanode_id = datanode.id AND controlnode_id = " + controlNode_id1 + " AND " +
                    "dataflow.input = 0))";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                rs.close();
                stmt.close();
                conn.close();
                return false;
            } else {
                rs.close();
                stmt.close();
                conn.close();
                return true;
            }
            //Clean-up environment

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
}
