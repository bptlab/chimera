package de.hpi.bpt.chimera.database.data;

import de.hpi.bpt.chimera.database.DbObject;

import java.util.List;

/**
 * This class is the representation of the dataFlow in the database.
 * It handles the connection between controlNodes and dataSets.
 * It provides the functionality to get all dataSets
 * belonging to the input- or outputSet of a controlNode.
 */
public class DbDataFlow extends DbObject {
	/**
	 * This method returns all database ID's of all dataSets
	 * belonging to the inputSet of a controlNode.
	 *
	 * @param controlNodeId This is the database ID of a controlNode.
	 * @return a list of database IDs of dataSets for the inputSet of this controlNode.
	 */
	public List<Integer> getInputSetsForControlNode(int controlNodeId) {
		String sql =
				"Select dataset_id FROM dataflow "
						+ "WHERE dataflow.input = 1 "
						+ "AND controlnode_id = " + controlNodeId;
		return this.executeStatementReturnsListInt(sql, "dataset_id");

	}

	/**
	 * This method returns all database ID's of all dataSets
	 * belonging to the outputSet of a controlNode.
	 *
	 * @param controlNodeId This is the database ID of a controlNode.
	 * @return a list of database IDs of dataSets in the outputSet of this controlNode.
	 */
	public List<Integer> getOutputSetsForControlNode(int controlNodeId) {
		String sql =
				"Select dataset_id FROM dataflow "
						+ "WHERE dataflow.input = 0 "
						+ "AND controlnode_id = " + controlNodeId;
		return this.executeStatementReturnsListInt(sql, "dataset_id");
	}

	/**
	 * Retrieve the id of the control node that writes data into a given data node.
	 * @param dataNodeId Id of the data node.
	 * @return The Id of the writing control node.
     */
	public int getPrecedingControlNode(int dataNodeId) {
		String sql = "SELECT d.controlnode_id as cn_id " +
				"FROM datanode as n, datasetconsistsofdatanode as s, dataflow as d " +
				"WHERE n.id = s.datanode_id AND d.dataset_id = s.dataset_id "
				+ "AND d.input = 0 AND n.id = %d;";
		return this.executeStatementReturnsInt(String.format(sql, dataNodeId), "cn_id");
	}

	/**
	 * Retrieve the Ids of the data classes of incoming data objects for a given
	 * control node.
	 * @param controlNodeId Id of the control node.
	 * @return All Ids of data classes of preceding data nodes.
     */
	public List<Integer> getPrecedingDataClassIds(int controlNodeId) {
		return getDataClassIds(1, controlNodeId);
	}

	/**
	 * Retrieve the Ids of the data classes of outgoing data objects for a given
	 * control node.
	 * @param controlNodeId Id of the control node.
	 * @return All Ids of data classes of following data nodes.
	 */
	public List<Integer> getFollowingDataClassIds(int controlNodeId) {
		return getDataClassIds(0, controlNodeId);
	}

	private List<Integer> getDataClassIds(int input, int controlNodeId) {
		String sql = "SELECT n.dataclass_id as class_id "
				+ "FROM datanode as n, dataflow as f, datasetconsistsofdatanode as s "
				+ "WHERE f.input = %d "
				+ "AND f.controlnode_id = %d "
				+ "AND f.dataset_id = s.dataset_id "
				+ "AND n.id = s.datanode_id";
		return this.executeStatementReturnsListInt(
				String.format(sql, input, controlNodeId), "class_id");
	}
}
