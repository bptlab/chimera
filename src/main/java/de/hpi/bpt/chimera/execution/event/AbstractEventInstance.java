package de.hpi.bpt.chimera.execution.event;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.ControlNodeInstance;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.State;
import de.hpi.bpt.chimera.execution.gateway.EventBasedGatewayInstance;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;

public abstract class AbstractEventInstance extends ControlNodeInstance {

	private final static Logger logger = Logger.getLogger(ControlNodeInstance.class);

	private EventBasedGatewayInstance previousEventBasedGatewayInstance;

	public AbstractEventInstance(AbstractEvent event, FragmentInstance fragmentInstance) {
		super(event, fragmentInstance);
		previousEventBasedGatewayInstance = null;
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
		// TODO why is that here, doesnt belong all EventQuerryStuff to
		// AbstractIntermediateCatchEventInstance?
		if (getControlNode().hasEventQuerry()) {
			String eventQuerry = getControlNode().getEventQuery();
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
		if (previousEventBasedGatewayInstance != null) {
			previousEventBasedGatewayInstance.skipAlternativeGateways(this);
		}

		// TODO: use CaseExecutioner of Case of FragmentInstance
		// this.fragmentInstance.createDataObjectInstances(this.getControlNode());

		// getCaseExecutioner().createDataObjectInstances(getControlNode());

		// TODO: write DataAttributes with Json
		getFragmentInstance().updateDataFlow();
		getFragmentInstance().createFollowing(getControlNode());
		getCaseExecutioner().startAutomaticTasks();
		setState(State.TERMINATED);
	}

	@Override
	public void skip() {
		this.setState(State.SKIPPED);
	}

	// GETTER & SETTER
	@Override
	public AbstractEvent getControlNode() {
		return (AbstractEvent) super.getControlNode();
	}

	public EventBasedGatewayInstance getPreviousEventBasedGatewayInstance() {
		return previousEventBasedGatewayInstance;
	}

	public void setPreviousEventBasedGatewayInstance(EventBasedGatewayInstance previousEventBasedGatewayInstance) {
		this.previousEventBasedGatewayInstance = previousEventBasedGatewayInstance;
	}

}
