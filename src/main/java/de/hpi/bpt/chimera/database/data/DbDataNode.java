package de.hpi.bpt.chimera.database.data;

import de.hpi.bpt.chimera.database.DbObject;
import org.apache.log4j.Logger;


import java.util.List;
import java.util.Map;

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
	public List<Integer> getDataClassIdsForDataSets(int dataSetId) {
		String sql =
				"Select dataclass_id FROM datanode, datasetconsistsofdatanode "
						+ "WHERE datanode.id = "
						+ "datasetconsistsofdatanode.datanode_id "
						+ "AND dataset_id = " + dataSetId
						+ " ORDER BY dataclass_id";
		return this.executeStatementReturnsListInt(sql, "dataclass_id");
	}

	/**
	 * This method returns all database ID's for all states a dataSet can have.
	 *
	 * @param dataSetId This is the database ID of a dataSet.
	 * @return a list of all database ID's of all states this dataSet can have.
	 */
	public List<Integer> getDataStatesForDataSet(int dataSetId) {
		String sql =
				"Select state_id FROM datanode, datasetconsistsofdatanode "
						+ "WHERE datanode.id = "
						+ "datasetconsistsofdatanode.datanode_id "
						+ "AND dataset_id = " + dataSetId
						+ " ORDER BY dataclass_id";
		return this.executeStatementReturnsListInt(sql, "state_id");
	}

	/**
	 * Retrieve the Id of a data class of a data node.
	 * @param dataNodeId Id of the data node.
	 * @return Id of its data class.
     */
    public int getDataClassIdForDataNode(int dataNodeId) {
        String sql = "SELECT dataclass_id FROM datanode " +
                "WHERE datanode.id = %d";
        return this.executeStatementReturnsInt(String.format(sql, dataNodeId), "dataclass_id");
    }

	/**
	 * Retrieve a Map, linking each data class id in a data set to its
	 * respective data state id.
	 * @param dataSetId Id of the data set whose data classes should be retrieved.
	 * @return A Map linking data class Ids to the Ids of their state in the given set.
     */
	public Map<Integer, Integer> getDataSetClassToStateMap(int dataSetId) {
		String sql = "SELECT dataclass_id, state_id "
						+ "FROM datanode, datasetconsistsofdatanode "
						+ "WHERE datanode.id = "
						+ "datasetconsistsofdatanode.datanode_id "
						+ "AND dataset_id = " + dataSetId
						+ " ORDER BY dataclass_id";
		return executeStatementReturnsMapIntInt(sql, "dataclass_id", "state_id");
    }
}
