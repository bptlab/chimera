package de.hpi.bpt.chimera.database.data;

import de.hpi.bpt.chimera.database.DbObject;
import de.hpi.bpt.chimera.jcore.data.DataConditions;
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
	public List<Integer> getDataStatesForDataSets(int dataSetId) {
		String sql =
				"Select state_id FROM datanode, datasetconsistsofdatanode "
						+ "WHERE datanode.id = "
						+ "datasetconsistsofdatanode.datanode_id "
						+ "AND dataset_id = " + dataSetId
						+ " ORDER BY dataclass_id";
		return this.executeStatementReturnsListInt(sql, "state_id");
	}

    public DataConditions getInputSetFor(int inputSetId) {
        throw new RuntimeException("To be implemented");
    }

    public int getDataClassIdForDataNode(int dataNodeId) {
        String sql = "SELECT dataclass_id FROM datanode " +
                "WHERE datanode.id = %d";
        return this.executeStatementReturnsInt(String.format(sql, dataNodeId), "dataclass_id");
    }

	public Map<Integer, Integer> getDataClassIdToState(int dataSetId) {
		String sql = "SELECT dataclass_id, state_id "
						+ "FROM datanode, datasetconsistsofdatanode "
						+ "WHERE datanode.id = "
						+ "datasetconsistsofdatanode.datanode_id "
						+ "AND dataset_id = " + dataSetId
						+ " ORDER BY dataclass_id";
		return executeStatementReturnsMapIntInt(sql, "dataclass_id", "state_id");
    }
}
