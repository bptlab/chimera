package de.hpi.bpt.chimera.execution.controlnodes.event;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.AbstractDataControlNodeInstance;
import de.hpi.bpt.chimera.execution.controlnodes.ControlNodeInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.event.behavior.EventBehavior;
import de.hpi.bpt.chimera.execution.controlnodes.gateway.EventBasedGatewayInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;

@Entity
public abstract class AbstractEventInstance extends AbstractDataControlNodeInstance {

	private static final Logger log = Logger.getLogger(ControlNodeInstance.class);

	@OneToOne(cascade = CascadeType.ALL)
	private EventBehavior behavior;
	@OneToOne(cascade = CascadeType.ALL)
	private EventBasedGatewayInstance previousEventBasedGatewayInstance;


	/**
	 * for JPA only
	 */
	public AbstractEventInstance() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}


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
		setState(State.READY);
		behavior.enableControlFlow();
	}

	/**
	 * ExecutionBehaviour
	 */
	@Override
	public void begin() {
		behavior.begin();
	}

	/**
	 * OutgoingBehaviour
	 */
	@Override
	public void terminate() {
		if (previousEventBasedGatewayInstance != null) {
			previousEventBasedGatewayInstance.skipAlternativeGateways(this);
		}

		if (getControlNode().hasUniquePostCondition() && getControlNode().hasPostCondition()) {
			for (AtomicDataStateCondition condition : getControlNode().getPostCondition().getConditionSets().get(0).getConditions()) {
				DataObject dataObject = getDataManager().createDataObject(condition);
				getOutputDataObjects().add(dataObject);
			}
		}

		behavior.terminate();
		setState(State.TERMINATED);
		getFragmentInstance().updateDataFlow();
		getFragmentInstance().createFollowing(getControlNode());
	}

	@Override
	public void skip() {
		this.setState(State.SKIPPED);
	}

	/**
	 * 
	 * @return whether the data control node instance is in the correct state
	 *         for beginning.
	 */
	@Override
	public boolean canBegin() {
		return getState().equals(State.READY) || getState().equals(State.REGISTERED);
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

	public void setBehavior(EventBehavior behavior) {
		this.behavior = behavior;
	}

	public EventBehavior getBehavior() {
		return behavior;
	}
}
