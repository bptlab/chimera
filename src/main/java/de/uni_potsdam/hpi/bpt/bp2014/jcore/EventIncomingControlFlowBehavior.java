package de.uni_potsdam.hpi.bpt.bp2014.jcore;

/**
 * Represents the incoming behavior of end events.
 */
public class EventIncomingControlFlowBehavior extends AbstractIncomingBehavior {
	private final String type;
	private ScenarioInstance scenarioInstance;

	/**
	 * Creates and initializes an event incoming control flow behavior.
	 * This behavior is only for an end event.
	 *
	 * @param controlNodeInstance This is an instance of the class AbstractControlNodeInstance.
	 * @param scenarioInstance    This is an instance of the class ScenarioInstance.
	 * @param type                This is the type of the event.
	 */
	public EventIncomingControlFlowBehavior(AbstractControlNodeInstance controlNodeInstance,
			ScenarioInstance scenarioInstance, String type) {
		this.setControlNodeInstance(controlNodeInstance);
		this.scenarioInstance = scenarioInstance;
		this.type = type;
	}

	@Override public void enableControlFlow() {
		scenarioInstance.restartFragment(this.getControlNodeInstance()
				.getFragmentInstanceId());
	}

    /*
	 * Getter
     */

	/**
	 * @return the type of behavior.
	 */
	public String getType() {
		return type;
	}
}
