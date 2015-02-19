package de.uni_potsdam.hpi.bpt.bp2014.database;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

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

/**
 * This class is the representation of a dataNode in the database.
 * It provides the functionality to get all dataObjects belonging to a dataSet as well as their states they can have.
 */
public class DbDataNode extends DbObject {
    /**
     * This method returns all database ID's of all dataObjects belonging to a dataSet.
     *
     * @param dataSet_id This is the database ID of a dataSet.
     * @return a list of database ID's of all dataObjects belonging to this dataSet.
     */
    public LinkedList<Integer> getDataObjectIdsForDataSets(int dataSet_id) {
        String sql = "Select dataobject_id FROM datanode, datasetconsistsofdatanode WHERE datanode.id = datasetconsistsofdatanode.datanode_id AND dataset_id = " + dataSet_id + " ORDER BY dataobject_id";
        return this.executeStatementReturnsListInt(sql, "dataobject_id");
    }

    /**
     * This method returns all database ID's for all states a dataSet can have.
     *
     * @param dataSet_id This is the database ID of a dataSet.
     * @return a list of all database ID's of all states this dataSet can have.
     */
    public LinkedList<Integer> getDataStatesForDataSets(int dataSet_id) {
        String sql = "Select state_id FROM datanode, datasetconsistsofdatanode WHERE datanode.id = datasetconsistsofdatanode.datanode_id AND dataset_id = " + dataSet_id + " ORDER BY dataobject_id";
        return this.executeStatementReturnsListInt(sql, "state_id");
    }

    /**
     * This method returns all database data objects a dataSet can have.
     * @param dataSet_id This is the database ID of a dataSet.
     * @return a list of all instances from DataObject.
     */
    public LinkedList<DataObject> getDataObjectsForDataSets(int dataSet_id) {
        String sql = "Select dataobject_id, state_id FROM datanode, datasetconsistsofdatanode WHERE datanode.id = datasetconsistsofdatanode.datanode_id AND dataset_id = " + dataSet_id + " ORDER BY dataobject_id";
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        LinkedList<DataObject> results = new LinkedList<DataObject>();
        if (conn == null) {
            return results;
        }

        try {
            //Execute a query
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                results.add(new DataObject(rs.getInt("dataobject_id"), rs.getInt("state_id")));
            }
            //Clean-up environment
            rs.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return results;
    }

}
