package de.hpi.bpt.chimera.database.controlnodes.events;

import de.hpi.bpt.chimera.database.DbObject;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractEvent;

/**
 * Data Access Object for Event Control nodes.
 * Used by subclasses of the {@link AbstractEvent} to retrieve their query.
 */
public class DbEvent extends DbObject {

    /**
     * Retrieves the event query of an event control node.
     * @param controlNodeId the database id of the event control node
     * @return query or exception if query is not found
     * @throws IllegalArgumentException if no query is found.
     */
    public String getQueryForControlNode(int controlNodeId) throws IllegalArgumentException {
        String sql = "SELECT * FROM event WHERE event.controlnode_id = " + controlNodeId + " ;";
        return this.executeStatementReturnsString(sql, "query");
    }
}
