package de.hpi.bpt.chimera.database.controlnodes.events;

import de.hpi.bpt.chimera.database.DbObject;

/**
 *
 */
public class DbTimerEvent extends DbObject {
    public String retrieveTimerDefinition(int controlNodeId) {
        String retrieveTimerDefinition = "SELECT * FROM timerevent WHERE "
                + "controlNodeDatabaseId = %d;";
        String sql = String.format(retrieveTimerDefinition, controlNodeId);
        return this.executeStatementReturnsString(sql, "timerDefinition");
    }
}
