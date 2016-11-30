package de.hpi.bpt.chimera.database.controlnodes;

import de.hpi.bpt.chimera.database.DbObject;

/**
 * This class is the representation of a controlNode in the database.
 * It provides the functionality to get the startEvent of a fragment as well as the type and
 * label of a controlNode.
 * Moreover it is possible to check if two controlNodes have the same output.
 */
public class DbControlNode extends DbObject {

	/**
	 * This method returns the database Id of a startEvent in the context of a fragment.
	 *
	 * @param fragmentId This is the database ID of a fragment.
	 * @return -1 if something went wrong else the database ID of the startEvent.
	 */
	public int getStartEventID(int fragmentId) {
		String sql = "SELECT id FROM controlnode " + "WHERE type = 'StartEvent' " + "AND fragment_id = " + fragmentId;
		return this.executeStatementReturnsInt(sql, "id");
	}

	/**
	 * @param controlNodeId Id of the control node
	 * @return fragment id the control node belongs to.
	 */
	public int getFragmentId(int controlNodeId) {
		String sql = "SELECT * FROM controlnode WHERE id = " + controlNodeId;
		return this.executeStatementReturnsInt(sql, "fragment_id");
	}

	/**
	 * This method returns the type of a controlNode.
	 *
	 * @param controlNodeId This is the database ID of a controlNode.
	 * @return the type of the controlNode as a String.
	 */
	public String getType(int controlNodeId) {
		String sql = "SELECT type FROM controlnode WHERE id = " + controlNodeId;
		return this.executeStatementReturnsString(sql, "type");
	}

	/**
	 * This method returns the name of a controlNode.
	 *
	 * @param controlNodeId This is the database ID of a controlNode.
	 * @return the label of the controlNode as a String.
	 */
	public String getLabel(int controlNodeId) {
		String sql = "SELECT label FROM controlnode WHERE id = " + controlNodeId;
		return this.executeStatementReturnsString(sql, "label");
	}

	/**
	 * Retrieves whether a control node exists for a given id in a given scenario.
	 *
	 * @param controlNodeId Id of the control node.
	 * @param scenarioId    Id of the scenario.
	 * @return True, if a control node with that Id exists.
	 */
	public boolean existControlNode(int controlNodeId, int scenarioId) {
		String existControlNode = "SELECT * FROM controlnode, fragment WHERE " + "controlnode.fragment_id = fragment.id AND " + "controlnode.id = %d AND fragment.scenario_id = %d;";
		existControlNode = String.format(existControlNode, controlNodeId, scenarioId);
		return this.executeExistStatement(existControlNode);
	}
}
