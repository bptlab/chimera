package de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.events;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;

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
