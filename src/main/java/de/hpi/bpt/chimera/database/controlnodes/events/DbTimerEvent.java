package de.hpi.bpt.chimera.database.controlnodes.events;

import de.hpi.bpt.chimera.database.DbObject;
import de.hpi.bpt.chimera.jcore.controlnodes.TimerEventInstance;

/**
 * Data Access Object for timer events.
 * Used by the {@link TimerEventInstance} to retrieve the
 * specified time from the model.
 */
public class DbTimerEvent extends DbObject {

    /**
     *
     * @param controlNodeId
     * @return
     */
    public String retrieveTimerDefinition(int controlNodeId) {
        String retrieveTimerDefinition = "SELECT * FROM timerevent WHERE "
                + "controlNodeDatabaseId = %d;";
        String sql = String.format(retrieveTimerDefinition, controlNodeId);
        return this.executeStatementReturnsString(sql, "timerDefinition");
    }
}
