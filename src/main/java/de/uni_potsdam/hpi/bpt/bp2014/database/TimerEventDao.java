package de.uni_potsdam.hpi.bpt.bp2014.database;

/**
 *
 */
public class TimerEventDao extends DbObject {
    public String retrieveTimerDefinition(int controlNodeId) {
        String retrieveTimerDefinition = "SELECT * FROM timerevent WHERE "
                + "controlNodeDatabaseId = %d;";
        String sql = String.format(retrieveTimerDefinition, controlNodeId);
        return this.executeStatementReturnsString(sql, "timerDefinition");
    }
}
