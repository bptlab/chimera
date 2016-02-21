package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.EventIncomingControlFlowBehavior;

/**
 * This class represents events.
 * This engine supports only end and starts event. But only end events get represented as instance.
 * So this class represents an end event.
 */

public class EventInstance extends AbstractControlNodeInstance {
	@SuppressWarnings("unused") private final String type;
	//Only support Event is an End Event
	//Don't writes anything in the database

	/**
	 * Creates and initializes a new event instance.
	 *
	 * @param type                This is the type of the event.
	 * @param fragmentInstanceId This is the database id from the fragment instance.
	 * @param scenarioInstance    This is an instance from the class ScenarioInstance.
	 */
	public EventInstance(int fragmentInstanceId,
			ScenarioInstance scenarioInstance, String type) {
		this.scenarioInstance = scenarioInstance;
		this.setFragmentInstanceId(fragmentInstanceId);
		this.type = type;
		this.setIncomingBehavior(new EventIncomingControlFlowBehavior(
				this, scenarioInstance, type));
	}

	@Override public boolean skip() {
		return false;
	}

	@Override public boolean terminate() {
		return false;
	}
}
