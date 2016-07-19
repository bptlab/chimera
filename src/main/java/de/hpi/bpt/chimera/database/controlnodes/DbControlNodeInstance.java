package de.hpi.bpt.chimera.database.controlnodes;

import de.hpi.bpt.chimera.database.DbObject;

import java.util.List;

/**
 * This class is the representation of a controlNode instance in the database.
 * It provides the functionality to check for existing instances as well as creating new ones.
 * Moreover it can retrieve all activities/gateways belonging to a specific fragment instance.
 */
public class DbControlNodeInstance extends DbObject {
	/**
	 * This method checks if a controlNode instance exists in the database
	 * and belongs to a controlNode and a fragment instance.
	 *
	 * @param controlNodeId      This is the database ID of a controlNode.
	 * @param fragmentInstanceId This is the database ID of a fragment instance.
	 * @return true if the controlNode instance exists else false.
	 */
	public Boolean existControlNodeInstance(int controlNodeId, int fragmentInstanceId) {
		String sql = "SELECT id FROM controlnodeinstance "
				+ "WHERE controlnode_id = " + controlNodeId + " "
				+ "AND fragmentinstance_id = " + fragmentInstanceId;
		return executeExistStatement(sql);
	}

	/**
	 *
	 * @param id This is the database ID of a controlNodeInstance.
	 * @return true if the controlNodeInstance instance exists else false.
	 */
	public Boolean existControlNodeInstance(int id) {
		String sql = "SELECT id FROM controlnodeinstance "
				+ "WHERE id = " + id;
		return executeExistStatement(sql);
	}

	/**
	 * This method creates and saves a new controlNode instance to the database
	 * in the context of a fragment instance.
	 *
	 * @param controlNodeId      This is the database ID of a controlNode.
	 * @param controlNodeType     This is the desirable type of the new controlNode instance.
	 * @param fragmentInstanceId This is the database ID of a fragment instance.
	 * @return the database ID of the newly created controlNode instance (Error -1).
	 */
	public int createNewControlNodeInstance(int controlNodeId, String controlNodeType,
			int fragmentInstanceId) {
		String sql =
				"INSERT INTO controlnodeinstance ("
						+ "Type, controlnode_id, fragmentinstance_id) "
						+ "VALUES ('" + controlNodeType + "', "
						+ controlNodeId + ", "
						+ fragmentInstanceId	+ ")";
		return executeInsertStatement(sql);
	}

	/**
	 * This method returns the database ID of a controlNode instance
	 * belonging to a controlNode and fragment instance.
	 *
	 * @param controlNodeId      This is the database ID of a controlNode.
	 * @param fragmentInstanceId This is the database ID of a fragment instance.
	 * @return -1 if something went wrong else the database ID of a controlNode instance.
	 */
	public int getControlNodeInstanceId(int controlNodeId, int fragmentInstanceId) {
		String sql = "SELECT id FROM controlnodeinstance "
				+ "WHERE controlnode_id = " + controlNodeId
				+ " AND fragmentinstance_id = " + fragmentInstanceId;
		return this.executeStatementReturnsInt(sql, "id");
	}
	/**
	 * This method returns all database ID's of all activities belonging to a fragment instance.
	 *
	 * @param fragmentInstanceId This is the database ID of a fragment instance.
	 * @return a list of database ID's of all activities of this fragment instance.
	 */
	public List<Integer> getActivitiesForFragmentInstanceId(int fragmentInstanceId) {
		String sql =
				"SELECT controlnode_id FROM controlnodeinstance "
						+ "WHERE controlnodeinstance.Type = 'Activity' "
						+ "AND fragmentinstance_id = " + fragmentInstanceId;
		return this.executeStatementReturnsListInt(sql, "controlnode_id");
	}

	/**
	 *
	 * @param fragmentInstanceId This is the database ID of a fragment instance.
	 * @return a list of database ID's of all activity instances of this fragment instance.
	 */
	public List<Integer> getActivityInstancesForFragmentInstanceID(
			int fragmentInstanceId) {
		String sql =
				"SELECT id FROM controlnodeinstance "
						+ "WHERE controlnodeinstance.Type = 'Activity' "
						+ "AND fragmentinstance_id = " + fragmentInstanceId;
		return this.executeStatementReturnsListInt(sql, "id");
	}

	/**
	 * This method returns all database ID's for all gateways
	 * belonging to a specific fragment instance.
	 *
	 * @param fragmentInstanceId This is the database ID of a fragment instance.
	 * @return a list of database ID's of gateways belonging to this fragment instance.
	 */
	public List<Integer> getGatewaysForFragmentInstanceID(int fragmentInstanceId) {
		String sql =
				"SELECT controlnode_id FROM controlnodeinstance "
						+ "WHERE (controlnodeinstance.Type = 'AND' "
						+ "OR controlnodeinstance.Type = 'XOR'"
						+ "OR controlnodeinstance.Type = 'EVENT_BASED') "
						+ "AND fragmentinstance_id = "
						+ fragmentInstanceId;
		return this.executeStatementReturnsListInt(sql, "controlnode_id");
	}

	/**
	 * This method returns the controlNodeID of a controlNodeInstance.
	 *
	 * @param id ID of the controlNodeInstance.
	 * @return controlNodeID.
	 */
	public int getControlNodeID(int id) {
		String sql = "SELECT controlnode_id FROM controlnodeinstance WHERE id = "
				+ id;
		return this.executeStatementReturnsInt(sql, "controlnode_id");
	}

	/**
	 *
	 * @param fragmentInstanceId This is the database ID of a fragment instance.
	 * @return a list of database ID's of gateway instances belonging to this fragment instance.
	 */
	public List<Integer> getGatewayInstancesForFragmentInstanceID(
			int fragmentInstanceId) {
		String sql =
				"SELECT id FROM controlnodeinstance "
						+ "WHERE (controlnodeinstance.Type = 'AND' "
						+ "OR controlnodeinstance.Type = 'XOR'"
						+ "OR controlnodeinstance.Type = 'EVENT_BASED') "
						+ "AND fragmentinstance_id = " + fragmentInstanceId;
		return this.executeStatementReturnsListInt(sql, "id");
	}
}
