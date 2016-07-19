package de.hpi.bpt.chimera.jcore.controlnodes;

import de.hpi.bpt.chimera.database.DbObject;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.flowbehaviors.EndEventIncomingBehavior;


/**
 * This class represents an end event. Once an end event is reached the fragment containing
 * the event will be restarted. Thus, there only is an incoming control flow behavior.
 */
public class EndEvent extends AbstractControlNodeInstance {
	private boolean isSendEvent;

	/**
	 * Creates and initializes a new end event instance.
	 *
	 * @param fragmentInstanceId This is the database id from the fragment instance.
	 * @param scenarioInstance    This is an instance from the class ScenarioInstance.
	 */
	public EndEvent(int controlNodeId, int fragmentInstanceId,
					ScenarioInstance scenarioInstance) {
        this.setControlNodeId(controlNodeId);
        this.scenarioInstance = scenarioInstance;
        this.setFragmentInstanceId(fragmentInstanceId);
        this.isSendEvent = isSendEvent();
        this.setIncomingBehavior(new EndEventIncomingBehavior(
                this, scenarioInstance, this.isSendEvent));
    }

	@Override public boolean skip() {
		return false;
	}

	@Override public boolean terminate() {
		return false;
	}

	private boolean isSendEvent() {
		String isSendEventQuery = "SELECT * FROM sendevent WHERE controlnode_id = %d;";

		return new DbObject().executeExistStatement(
				String.format(isSendEventQuery, this.getControlNodeId()));
	}
}
