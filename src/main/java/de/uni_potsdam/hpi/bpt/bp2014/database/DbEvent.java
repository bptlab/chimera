package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class DbEvent extends DbObject{
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
}
