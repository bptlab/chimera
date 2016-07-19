package de.hpi.bpt.chimera.database.controlnodes.events;

import de.hpi.bpt.chimera.database.DbObject;
import de.hpi.bpt.chimera.database.DbControlFlow;

import java.util.List;

/**
 *
 */
public class DbEvent extends DbObject {
    /**
     *
     * @return Searches Event in the database and returns control Node Id
     */
    public List<Integer> getFollowingControlNodesForEvent(int fragmentId) {
        String sql = "SELECT * FROM event WHERE model_id = '" + fragmentId + "';";
        int controlNodeId = this.executeStatementReturnsInt(sql, "controlnode_id");
        DbControlFlow flow = new DbControlFlow();
        List<Integer> followingControlNodes = flow.getFollowingControlNodes(controlNodeId);
        return followingControlNodes;
    }

    /**
     * Retrieves query for an control node.
     * @param controlNodeId id of the control node in the model
     * @return query or exception if query is not found
     * @throws IllegalArgumentException if no query is found.
     */
    public String getQueryForControlNode(int controlNodeId) throws IllegalArgumentException {
        String sql = "SELECT * FROM event WHERE event.controlnode_id = " + controlNodeId + " ;";
        return this.executeStatementReturnsString(sql, "query");
    }
}
