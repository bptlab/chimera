package de.hpi.bpt.chimera.database.controlnodes;

import de.hpi.bpt.chimera.database.DbObject;

/**
 *
 */
public class DbBoundaryEvent extends DbObject {
    /**
     *
     * @param eventNodeId The control node Id of the boundary event
     * @param fragmentInstanceId The id of the fragment instance where the bounday event is
     *                           initialized in
     * @return databaseId of the activity the bounday event is attached to
     */
    public int getControlNodeAttachedToEvent(int eventNodeId, int fragmentInstanceId) {
        String editorIdQuery = "SELECT * FROM boundaryeventref where controlnode_id = "
                + eventNodeId + ";";
        Integer activityControlNodeId = this.executeStatementReturnsInt(editorIdQuery, "attachedtoref");
        DbControlNodeInstance controlNodeInstance = new DbControlNodeInstance();
        return controlNodeInstance.getControlNodeInstanceId(activityControlNodeId,
                fragmentInstanceId);
    }

    /**
     *
     * @param activityDatabaseId id of the activity control node, which should be checked for
     *                           an event.
     * @return returns the id of the boundary event attached to the activity or -1 of no boundary
     * event is attached.
     */
    public int getBoundaryEventForActivity(int activityDatabaseId) {
        String editorIdQuery = "SELECT * FROM boundaryeventref where attachedtoref = "
                + activityDatabaseId + ";";
        return this.executeStatementReturnsInt(editorIdQuery, "controlnode_id");
    }
}
