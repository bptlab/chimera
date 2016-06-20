package de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.EndEventIncomingControlFlowBehavior;

public class EndEvent extends AbstractControlNodeInstance {
	@SuppressWarnings("unused") private final String type;
	private boolean isSendEvent;

	/**
	 * Creates and initializes a new event instance.
	 *
	 * @param type                This is the type of the event.
	 * @param fragmentInstanceId This is the database id from the fragment instance.
	 * @param scenarioInstance    This is an instance from the class ScenarioInstance.
	 */
	public EndEvent(int fragmentInstanceId,
					ScenarioInstance scenarioInstance, String type) {
		this.scenarioInstance = scenarioInstance;
		this.setFragmentInstanceId(fragmentInstanceId);
		this.type = type;
		this.isSendEvent = isSendEvent();
		this.setIncomingBehavior(new EndEventIncomingControlFlowBehavior(
				this, scenarioInstance, type, this.isSendEvent));
	}

	@Override public boolean skip() {
		return false;
	}

	@Override public boolean terminate() {
		return false;
	}

	private boolean isSendEvent() {
		String isSendEvent = "SELECT * FROM sendevent WHERE controlnode_id = %d;";

		return new DbObject().executeExistStatement(
				String.format(isSendEvent, this.getControlNodeId()));
	}
}
