package de.hpi.bpt.chimera.execution.event;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.Behaving;
import de.hpi.bpt.chimera.execution.ControlNodeInstance;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;

public abstract class AbstractEventInstance extends ControlNodeInstance implements Behaving {
	private static Logger log = Logger.getLogger(AbstractEventInstance.class);

	private AbstractEvent event;

	public AbstractEventInstance(AbstractEvent event, FragmentInstance fragmentInstance) {
		super(event, fragmentInstance);
		this.event = event;
		// TODO: maybe have to look whether ControlNode / AbstractEvent is
		// already used in FragmentInstance. Then use State of this Instance.
		setState(State.INIT);
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
		if (event.hasEventQuerry()) {
			String eventQuerry = event.getEventQuerry();
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
		getCaseExecutioner().createDataObjectInstances(event);
		// TODO: write DataAttributes with Json
		getFragmentInstance().updateDataFlow();
		getFragmentInstance().createFollowing(event);
		getCaseExecutioner().startAutomaticTasks();
		setState(State.TERMINATED);
	}

	@Override
	public void skip() {
	}
}
