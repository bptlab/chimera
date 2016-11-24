package de.hpi.bpt.chimera.jcore.flowbehaviors;

import de.hpi.bpt.chimera.database.controlnodes.DbBoundaryEvent;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;

/**
 *
 */
public class BoundaryEventOutgoingBehavior extends EventOutgoingBehavior {
	public BoundaryEventOutgoingBehavior(int controlNodeId, ScenarioInstance scenarioInstance, int fragmentInstanceId, int controlNodeInstanceId) {
		super(controlNodeId, scenarioInstance, fragmentInstanceId, controlNodeInstanceId);
	}

	@Override
	public void terminate() {
		DbBoundaryEvent boundaryEvent = new DbBoundaryEvent();
		int attachedControlNode = boundaryEvent.getControlNodeAttachedToEvent(this.getControlNodeId(), this.getFragmentInstanceId());
		ScenarioInstance scenario = this.getScenarioInstance();

		scenario.getControlNodeInstances();
		ActivityInstance attachedActivity = (ActivityInstance) scenario.getControlNodeInstanceWithId(attachedControlNode);
		attachedActivity.cancel();

		super.terminate();
	}

}
