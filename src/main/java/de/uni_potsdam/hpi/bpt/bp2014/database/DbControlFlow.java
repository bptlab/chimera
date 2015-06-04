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
     * @param controlNode_id This is the database ID of a controlNode.
     * @return -1 if something went wrong else it returns the database ID of a controlNode which is right after a startEvent.
     */
    public int getNextControlNodeAfterStartEvent(int controlNode_id) {
        String sql = "SELECT controlnode_id2 FROM controlflow WHERE controlnode_id1 = " + controlNode_id;
        return this.executeStatementReturnsInt(sql, "controlnode_id2");
    }

    /**
     * This method returns all database ID's of all controlNodes succeeding the given controlNode.
     *
     * @param controlNode_id This is the database ID of a controlNode.
     * @return a list of database ID's of controlNodes which succeed the given controlNode.
     */
    public LinkedList<Integer> getFollowingControlNodes(int controlNode_id) {
        String sql = "SELECT controlnode_id2 FROM controlflow WHERE controlnode_id1 = " + controlNode_id;
        return this.executeStatementReturnsListInt(sql, "controlnode_id2");
    }

    /**
     * This method returns all database ID's of all controlNodes preceding the given controlNode.
     *
     * @param controlNode_id This is the database ID of a controlNode.
     * @return a list of database ID's of controlNodes which precede the given controlNode.
     */
    public LinkedList<Integer> getPredecessorControlNodes(int controlNode_id) {
        String sql = "SELECT controlnode_id1 FROM controlflow WHERE controlnode_id2 = " + controlNode_id;
        return this.executeStatementReturnsListInt(sql, "controlnode_id1");
    }

    /**
     * This method returns all database ID's of all controlNodes and the conditions preceding the given controlNode.
     *
     * @param controlNode_id This is the database ID of a controlNode.
     * @return a map of database ID's of controlNodes and conditions which precede the given controlNode.
     */
    public Map<Integer, String> getConditions(int controlNode_id) {
        String sql = "SELECT controlnode_id2, controlflow.condition FROM controlflow WHERE controlnode_id1 = " + controlNode_id;
        return this.executeStatementReturnsMap(sql, "controlnode_id2", "condition");
    }
}
