package de.uni_potsdam.hpi.bpt.bp2014.database;

import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * This class is the representation of a controlNode in the database.
 * It provides the functionality to get the startEvent of a fragment as well as the type and label of a controlNode.
 * Moreover it is possible to check if two controlNodes have the same output.
 */
public class DbControlNode extends DbObject {
    static Logger log = Logger.getLogger(DbControlNode.class.getName());

    /**
     * This method returns the database Id of a startEvent in the context of a fragment.
     *
     * @param fragment_id This is the database ID of a fragment.
     * @return -1 if something went wrong else the database ID of the startEvent.
     */
    public int getStartEventID(int fragment_id) {
        String sql = "SELECT id FROM controlnode WHERE type = 'Startevent' AND fragment_id = " + fragment_id;
        return this.executeStatementReturnsInt(sql, "id");
    }

    /**
     * This method returns the type of a controlNode.
     *
     * @param controlNode_id This is the database ID of a controlNode.
     * @return the type of the controlNode as a String.
     */
    public String getType(int controlNode_id) {
        String sql = "SELECT type FROM controlnode WHERE id = " + controlNode_id;
        return this.executeStatementReturnsString(sql, "type");
    }

    /**
     * This method returns the name of a controlNode.
     *
     * @param controlNode_id This is the database ID of a controlNode.
     * @return the label of the controlNode as a String.
     */
    public String getLabel(int controlNode_id) {
        String sql = "SELECT label FROM controlnode WHERE id = " + controlNode_id;
        return this.executeStatementReturnsString(sql, "label");
    }

    /**
     * This method checks if 2 controlNodes have the same output (mostly used for shared activities).
     *
     * @param controlNode_id1 This is the database ID of the first controlNode.
     * @param controlNode_id2 This is the database ID of the second controlNode.
     * @return true if they have the same output else false.
     */
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
                    "dataflow.input = 0))"; //returns nothing if they have same output else something
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                rs.close();
                stmt.close();
                conn.close();
                return false;//not same output
            } else {
                rs.close();
                stmt.close();
                conn.close();
                return true;//same output
            }
            //Clean-up environment

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
        return false;
    }
}
