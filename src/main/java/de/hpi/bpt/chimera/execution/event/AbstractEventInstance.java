package de.hpi.bpt.chimera.execution.event;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.Behaving;
import de.hpi.bpt.chimera.execution.ControlNodeInstance;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractEvent;

public abstract class AbstractEventInstance extends ControlNodeInstance implements Behaving {
	private static Logger log = Logger.getLogger(AbstractEventInstance.class);

	private AbstractEvent event;

	public AbstractEventInstance(AbstractEvent event, FragmentInstance fragmentInstance) {
		super(event, fragmentInstance);
		this.event = event;
		// TODO: maybe have to look whether ControlNode / AbstractEvent is
		// already used in FragmentInstance. Then use State of this Instance.
		this.state = State.INIT;
	}

	/**
	 * IncomingBehaviour
	 */
	@Override
	public void enableControlFlow() {
		this.state = State.REGISTERED;
		log.info("ControlFlow of Event enabled");
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
		log.info("Begin Event execution");
		terminate();
	}

	/**
	 * OutgoingBehaviour
	 */
	@Override
	public void terminate() {
		// TODO: use CaseExecutioner of Case of FragmentInstance
		// this.fragmentInstance.createDataObjectInstances(this.getControlNode());
		this.fragmentInstance.getCase().getCaseExecutioner().createDataObjectInstances(event);
		log.info("DataObjectInstances created");
		// TODO: write DataAttributes with Json
		this.fragmentInstance.updateDataFlow();
		log.info("DataFlowUpdated");
		this.fragmentInstance.enableFollowing(event);
		log.info("Following Enabled");
		this.fragmentInstance.getCase().getCaseExecutioner().startAutomaticTasks();
		this.state = State.TERMINATED;
		log.info("Event terminated");
	}

	@Override
	public void skip() {
	}
}
