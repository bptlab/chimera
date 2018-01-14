package de.hpi.bpt.chimera.execution.controlnodes.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.AbstractDataControlNodeInstance;
import de.hpi.bpt.chimera.execution.controlnodes.ControlNodeInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.event.behavior.AbstractEventBehavior;
import de.hpi.bpt.chimera.execution.controlnodes.gateway.EventBasedGatewayInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;

public abstract class AbstractEventInstance extends AbstractDataControlNodeInstance {

	private static final Logger log = Logger.getLogger(ControlNodeInstance.class);

	private AbstractEventBehavior behavior;
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
		behavior.enableControlFlow();
		setState(State.REGISTERED);

		if (getControlNode().hasUniquePreCondition()) {
			List<DataObject> inputDataObjects = new ArrayList<>();
			for (AtomicDataStateCondition condition : getControlNode().getPostCondition().getAtomicDataStateConditions()) {
				List<DataObject> availableDataObjects = new ArrayList<>(getDataManager().getAvailableDataObjects(condition));
				if (availableDataObjects.size() == 1) {
					DataObject dataObject = availableDataObjects.get(0);
					inputDataObjects.add(dataObject);
				} else {
					log.info(String.format("There is none or more than one possible DataObject with DataClass: %s and State: %s for the input of the event: %s", condition.getDataClassName(), condition.getStateName(), getControlNode().getId()));
				}
			}
			getCaseExecutioner().beginEventInstance(this, inputDataObjects);
		} else {
			begin();
		}
	}

	/**
	 * ExecutionBehaviour
	 */
	@Override
	public void begin() {
		behavior.begin();
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

		if (getControlNode().hasUniquePostCondition()) {
			Map<DataClass, ObjectLifecycleState> dataObjectToObjectLifecycleTransition = new HashMap<>();
			if (getControlNode().hasPostCondition()) {
				dataObjectToObjectLifecycleTransition = getControlNode().getPostCondition().getConditionSets().get(0).getDataClassToObjectLifecycleState();
			}

			getCaseExecutioner().handleEventOutputTransitions(this, dataObjectToObjectLifecycleTransition);
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
