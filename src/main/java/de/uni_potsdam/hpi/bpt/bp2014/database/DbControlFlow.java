package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.LinkedList;
import java.util.Map;

/**
 * Represents the ControlFlow in the database.
 * Mostly used to get predecessors and successors of a controlNode.
 */

public class DbControlFlow extends DbObject {
	/**
	 * This method returns the database Id of the first controlNode after the startEvent.
	 *
	 * @param controlNodeId This is the database ID of a controlNode.
	 * @return the database ID of a controlNode right after a startEvent (Error: -1).
	 */
	public int getNextControlNodeAfterStartEvent(int controlNodeId) {
		String sql =
				"SELECT controlnode_id2 FROM controlflow "
						+ "WHERE controlnode_id1 = " + controlNodeId;
		return this.executeStatementReturnsInt(sql, "controlnode_id2");
	}

	/**
	 * This method returns all database ID's of controlNodes succeeding the given controlNode.
	 *
	 * @param controlNodeId This is the database ID of a controlNode.
	 * @return a list of database ID's of controlNodes which succeed the given controlNode.
	 */
	public LinkedList<Integer> getFollowingControlNodes(int controlNodeId) {
		String sql =
				"SELECT controlnode_id2 FROM controlflow "
						+ "WHERE controlnode_id1 = " + controlNodeId;
		return this.executeStatementReturnsListInt(sql, "controlnode_id2");
	}

	/**
	 * This method returns all database ID's of controlNodes preceding the given controlNode.
	 *
	 * @param controlNodeId This is the database ID of a controlNode.
	 * @return a list of database ID's of controlNodes which precede the given controlNode.
	 */
	public LinkedList<Integer> getPredecessorControlNodes(int controlNodeId) {
		String sql =
				"SELECT controlnode_id1 FROM controlflow "
						+ "WHERE controlnode_id2 = " + controlNodeId;
		return this.executeStatementReturnsListInt(sql, "controlnode_id1");
	}

	/**
	 * This method returns all database ID's of controlNodes
	 * and the conditions preceding the given controlNode.
	 *
	 * @param controlNodeId This is the database ID of a controlNode.
	 * @return a map of database ID's of controlNodes and conditions preceding the given
	 * controlNode.
	 */
	public Map<Integer, String> getConditions(int controlNodeId) {
		String sql =
				"SELECT controlnode_id2, controlflow.condition "
						+ "FROM controlflow "
						+ "WHERE controlnode_id1 = " + controlNodeId;
		return this.executeStatementReturnsMap(sql, "controlnode_id2", "condition");
	}
}
