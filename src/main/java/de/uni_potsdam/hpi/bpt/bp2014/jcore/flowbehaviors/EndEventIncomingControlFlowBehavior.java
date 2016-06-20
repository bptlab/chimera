package de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventSpawner;

/**
 * Represents the incoming behavior of end events.
 */
public class EndEventIncomingControlFlowBehavior extends AbstractIncomingBehavior {
	private final String type;
	private final boolean isSendEvent;
	private EventSpawner eventSpawner;

	/**
	 * Creates and initializes an event incoming control flow behavior.
	 * This behavior is only for an end event.
	 *
	 * @param controlNodeInstance This is an instance of the class AbstractControlNodeInstance.
	 * @param scenarioInstance    This is an instance of the class ScenarioInstance.
	 * @param type                This is the type of the event.
	 */
	public EndEventIncomingControlFlowBehavior(AbstractControlNodeInstance controlNodeInstance,
											   ScenarioInstance scenarioInstance, String type,
											   boolean isSendEvent) {
		this.setControlNodeInstance(controlNodeInstance);
		this.setScenarioInstance(scenarioInstance);
		this.type = type;
		this.isSendEvent = isSendEvent;
		if (isSendEvent) {
			this.eventSpawner = new EventSpawner(scenarioInstance);
		}
	}

	@Override public void enableControlFlow() {
		if (isSendEvent) {
			eventSpawner.spawnEvent(getControlNodeInstance().getControlNodeId());
		}
		getScenarioInstance().restartFragment(this.getControlNodeInstance()
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
