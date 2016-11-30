package de.hpi.bpt.chimera.database.controlnodes;

import de.hpi.bpt.chimera.database.DbObject;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import de.hpi.bpt.chimera.jcore.flowbehaviors.BoundaryEventOutgoingBehavior;

/**
 * Database Access Object for boundary events.
 * Used by the {@link ActivityInstance} and the {@link BoundaryEventOutgoingBehavior}
 * to retrieve execution data.
 */
public class DbBoundaryEvent extends DbObject {
	/**
	 * Retrieve the id of the activity instance the boundary event is attached to.
	 *
	 * @param eventNodeId        The control node Id of the boundary event
	 * @param fragmentInstanceId The id of the fragment instance where the boundary event is
	 *                           initialized in
	 * @return databaseId of the activity the boundary event is attached to
	 */
	public int getControlNodeAttachedToEvent(int eventNodeId, int fragmentInstanceId) {
		String editorIdQuery = "SELECT * FROM boundaryeventref where controlnode_id = " + eventNodeId + ";";
		Integer activityControlNodeId = this.executeStatementReturnsInt(editorIdQuery, "attachedtoref");
		DbControlNodeInstance controlNodeInstance = new DbControlNodeInstance();
		return controlNodeInstance.getControlNodeInstanceId(activityControlNodeId, fragmentInstanceId);
	}

	/**
	 * Retrieve the node id of a boundary event attached to a given activity.
	 *
	 * @param activityDatabaseId id of the activity control node, which should be checked for
	 *                           an event.
	 * @return returns the id of the boundary event attached to the activity or -1 of no boundary
	 * event is attached.
	 */
	public int getBoundaryEventForActivity(int activityDatabaseId) {
		String editorIdQuery = "SELECT * FROM boundaryeventref where attachedtoref = " + activityDatabaseId + ";";
		return this.executeStatementReturnsInt(editorIdQuery, "controlnode_id");
	}
}
