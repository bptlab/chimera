package de.uni_potsdam.hpi.bpt.bp2014.database.data;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;

import java.util.LinkedList;
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

    public int getPrecedingControlNode(int datanodeId) {
        String sql = "SELECT d.controlnode_id as cn_id " +
                "FROM datanode as n, datasetconsistsofdatanode as s, dataflow as d " +
                "WHERE n.id = s.datanode_id AND d.dataset_id = s.dataset_id AND n.id = %d;";
        return this.executeStatementReturnsInt(String.format(sql, datanodeId), "cn_id");
    }
}
