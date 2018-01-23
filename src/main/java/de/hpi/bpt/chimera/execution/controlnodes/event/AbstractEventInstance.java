package de.hpi.bpt.chimera.execution.controlnodes.event;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.AbstractDataControlNodeInstance;
import de.hpi.bpt.chimera.execution.controlnodes.ControlNodeInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.event.behavior.AbstractEventBehavior;
import de.hpi.bpt.chimera.execution.controlnodes.event.behavior.MessageReceiveEventBehavior;
import de.hpi.bpt.chimera.execution.controlnodes.gateway.EventBasedGatewayInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;

@Entity
public abstract class AbstractEventInstance extends AbstractDataControlNodeInstance {

	private static final Logger log = Logger.getLogger(ControlNodeInstance.class);

	@OneToOne(cascade = CascadeType.ALL)
	private AbstractEventBehavior behavior;
	@OneToOne
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

		if (getControlNode().hasPostCondition() && getControlNode().hasUniquePostCondition()) {
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

	public void setBehavior(AbstractEventBehavior behavior) {
		this.behavior = behavior;
	}

	public AbstractEventBehavior getBehavior() {
		return behavior;
	}
}
