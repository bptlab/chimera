package de.uni_potsdam.hpi.bpt.bp2014.database;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is the representation of a dataNode in the database.
 * It provides the functionality to get all dataObjects
 * belonging to a dataSet as well as the states they can have.
 */
public class DbDataNode extends DbObject {
	private static Logger log = Logger.getLogger(DbDataNode.class);

	/**
	 * This method returns all database ID's of all dataObjects belonging to a dataSet.
	 *
	 * @param dataSetId This is the database ID of a dataSet.
	 * @return a list of database ID's of all dataObjects belonging to this dataSet.
	 */
	public LinkedList<Integer> getDataObjectIdsForDataSets(int dataSetId) {
		String sql =
				"Select dataobject_id FROM datanode, datasetconsistsofdatanode "
						+ "WHERE datanode.id = "
						+ "datasetconsistsofdatanode.datanode_id "
						+ "AND dataset_id = " + dataSetId
						+ " ORDER BY dataobject_id";
		return this.executeStatementReturnsListInt(sql, "dataobject_id");
	}

	/**
	 * This method returns all database ID's for all states a dataSet can have.
	 *
	 * @param dataSetId This is the database ID of a dataSet.
	 * @return a list of all database ID's of all states this dataSet can have.
	 */
	public LinkedList<Integer> getDataStatesForDataSets(int dataSetId) {
		String sql =
				"Select state_id FROM datanode, datasetconsistsofdatanode "
						+ "WHERE datanode.id = "
						+ "datasetconsistsofdatanode.datanode_id "
						+ "AND dataset_id = " + dataSetId
						+ " ORDER BY dataobject_id";
		return this.executeStatementReturnsListInt(sql, "state_id");
	}

    public int getDataObjectIdForDataNode(int dataNodeId) {
        String sql = "SELECT dataobject_id FROM datanode " +
                "WHERE datanode.id = %d";
        return this.executeStatementReturnsInt(String.format(sql, dataNodeId), "dataobject_id");
    }


    public List<Integer> getDataobjectIdsForSet(int dataSetId) {
        String sql =
                "SELECT dataobject_id, state_id "
                        + "FROM datanode, datasetconsistsofdatanode "
                        + "WHERE datanode.id = "
                        + "datasetconsistsofdatanode.datanode_id "
                        + "AND dataset_id = " + dataSetId
                        + " ORDER BY dataobject_id";
        return this.executeStatementReturnsListInt(sql, "dataobject_id");
    }

	/**
	 * This method returns all database data objects a dataSet can have.
	 *
	 * @param dataSetId This is the database ID of a dataSet.
	 * @return a list of all instances from DataObject.
	 */
	public LinkedList<DataObject> getDataObjectsForDataSets(int dataSetId) {
		String sql =
				"SELECT dataobject_id, state_id "
						+ "FROM datanode, datasetconsistsofdatanode "
						+ "WHERE datanode.id = "
						+ "datasetconsistsofdatanode.datanode_id "
						+ "AND dataset_id = " + dataSetId
						+ " ORDER BY dataobject_id";
		java.sql.Connection conn = Connection.getInstance().connect();
		Statement stmt = null;
		ResultSet rs;
		LinkedList<DataObject> results = new LinkedList<>();
		if (conn == null) {
			return results;
		}

		try {

			//Execute a query
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				results.add(new DataObject(
						rs.getInt("dataobject_id"), rs.getInt("state_id")));
			}
			//Clean-up environment
			rs.close();
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
				conn.close();
			} catch (SQLException se) {
				log.error("SQL Error!: ", se);
				se.printStackTrace();
			}
		}
		return results;
	}

}
