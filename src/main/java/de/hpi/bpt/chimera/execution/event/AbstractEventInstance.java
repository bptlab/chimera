package de.hpi.bpt.chimera.execution.event;

import de.hpi.bpt.chimera.execution.ControlNodeInstance;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;

public abstract class AbstractEventInstance extends ControlNodeInstance {

	public AbstractEventInstance(AbstractEvent event, FragmentInstance fragmentInstance) {
		super(event, fragmentInstance);
		// TODO: maybe have to look whether ControlNode / AbstractEvent is
		// already used in FragmentInstance. Then use State of this Instance.
	}

	/**
	 * IncomingBehaviour
	 */
	@Override
	public void enableControlFlow() {
		setState(State.REGISTERED);
		this.begin();
	}

	/**
	 * ExecutionBehaviour
	 */
	@Override
	public void begin() {
		if (getControlNode().hasEventQuerry()) {
			String eventQuerry = getControlNode().getEventQuerry();
			if (!(eventQuerry.trim().isEmpty())) {
				// TODO: registerEvent in Unicorn
				// EventDispatcher.registerEvent(event,
				// event.getFragmentInstanceId(),
				// event.getScenarioInstance().getId(),
				// event.getScenarioInstance().getScenarioId());
				// return;
			}
		} 
		terminate();
	}

	/**
	 * OutgoingBehaviour
	 */
	@Override
	public void terminate() {
		// TODO: use CaseExecutioner of Case of FragmentInstance
		// this.fragmentInstance.createDataObjectInstances(this.getControlNode());
		getCaseExecutioner().createDataObjectInstances(getControlNode());
		// TODO: write DataAttributes with Json
		getFragmentInstance().updateDataFlow();
		getFragmentInstance().createFollowing(getControlNode());
		getCaseExecutioner().startAutomaticTasks();
		setState(State.TERMINATED);
	}

	@Override
	public void skip() {
	}

	// GETTER & SETTER
	@Override
	public AbstractEvent getControlNode() {
		return (AbstractEvent) super.getControlNode();
	}

}
