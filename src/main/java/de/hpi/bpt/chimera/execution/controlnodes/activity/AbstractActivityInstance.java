package de.hpi.bpt.chimera.execution.controlnodes.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.AbstractDataControlNodeInstance;
import de.hpi.bpt.chimera.execution.controlnodes.ControlNodeInstanceFactory;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.event.BoundaryEventInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.BoundaryEvent;

@Entity
public abstract class AbstractActivityInstance extends AbstractDataControlNodeInstance {
	private static final Logger log = Logger.getLogger(AbstractActivityInstance.class);

	private boolean isAutomaticTask;
	// TODO: find out what canTerminate is exactly needed for
	// private boolean canTerminate;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "attachedToActivity")
	private List<BoundaryEventInstance> attachedBoundaryEventInstances;


	/**
	 * for JPA only
	 */
	public AbstractActivityInstance() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}


	/**
	 * Create a new AbstractActivityInstance.
	 * 
	 * @param activity
	 * @param fragmentInstance
	 */
	public AbstractActivityInstance(AbstractActivity activity, FragmentInstance fragmentInstance) {
		super(activity, fragmentInstance);
		this.isAutomaticTask = false;
		this.attachedBoundaryEventInstances = new ArrayList<>();
	}

	/**
	 * Enables the incoming control flow of the ActivityInstance. Any previously
	 * selected DataObjects are cleared.
	 * If state is INIT, it is set to CONTROLFLOW_ENABLED.
	 * If data preconditions are fulfilled (or state is already DATAFLOW_ENABLED),
	 * state is set to READY. If an automatic tasks is READY, it is started.   
	 */
	@Override
	public void enableControlFlow() {
		getSelectedDataObjects().clear();
		if (getState().equals(State.INIT)) {
			setState(State.CONTROLFLOW_ENABLED);
		}
		if (getState().equals(State.DATAFLOW_ENABLED) || 
				getControlNode().getPreCondition().isFulfilled(getDataManager().getDataStateConditions())) {
			setState(State.READY);
		}
		// TODO: see #50 in github, allow automatic execution with unique input set
		if (getState().equals(State.READY) && this.isAutomaticTask 
				&& !getControlNode().hasPreCondition()) {
			getCaseExecutioner().beginActivityInstance(this, new ArrayList<DataObject>());
		}
	}

	/**
	 * Used for updating the DataFlow of the ActivityInstance.
	 */
	public void checkDataFlow() {
		if (getControlNode().getPreCondition().isFulfilled(getDataManager().getDataStateConditions())) {
			enableDataFlow();
		} else {
			disableDataFlow();
		}
	}

	private void enableDataFlow() {
		if (getState().equals(State.INIT)) {
			setState(State.DATAFLOW_ENABLED);
		} else if (getState().equals(State.CONTROLFLOW_ENABLED)) {
			setState(State.READY);
		}
	}

	private void disableDataFlow() {
		if (getState().equals(State.DATAFLOW_ENABLED)) {
			setState(State.INIT);
		} else if (getState().equals(State.READY)) {
			setState(State.CONTROLFLOW_ENABLED);
		}
	}

	/**
	 * Begin the Activity Instance. The selected DataObjects were set by the
	 * CaseExecutioner. If the ActivityInstance is an automatic task and has one
	 * or none condition set in the output the task will be terminated
	 * automatically and if there is one condition set it will be used for the
	 * termination of the task.
	 */
	@Override
	public void begin() {
		// assert ... maybe not needed
		if (!getState().equals(State.READY)) {
			log.info(String.format("%s not set to running, because the activty isn't in state READY", this.getControlNode().getName()));
			return;
		}
		// TODO: implement skipBehaviour
		createAttachedBoundaryEvents();

		setState(State.RUNNING);
		log.info(String.format("enabled %d Boundary events", attachedBoundaryEventInstances.size()));
		execute();
		if (this.isAutomaticTask && getControlNode().hasUniquePostCondition()) {
			Map<DataClass, ObjectLifecycleState> dataObjectToObjectLifecycleTransition = new HashMap<>();
			if (getControlNode().hasPostCondition()) {
				dataObjectToObjectLifecycleTransition = getControlNode().getPostCondition().getConditionSets().get(0).getDataClassToObjectLifecycleState();
			}
			
			getCaseExecutioner().handleActivityOutputTransitions(this, dataObjectToObjectLifecycleTransition);
			getCaseExecutioner().terminateActivityInstance(this);
		}
	}

	private void createAttachedBoundaryEvents() {
		for (BoundaryEvent boundaryEvent : getControlNode().getAttachedBoundaryEvents()) {
			BoundaryEventInstance boundaryEventInstance = (BoundaryEventInstance) getFragmentInstance().createControlNodeInstance(boundaryEvent);
			boundaryEventInstance.setAttachedToActivity(this);
			attachedBoundaryEventInstances.add(boundaryEventInstance);
			boundaryEventInstance.enableControlFlow();
		}
	}

	/**
	 * Terminate the Activity. The transition of the States of the DataClasses
	 * is handled by the CaseExecutioner.
	 */
	@Override
	public void terminate() {
		if (!getState().equals(State.RUNNING)) {
			log.info(String.format("%s not terminated, because the activity isn't in state RUNNING", this.getControlNode().getName()));
			return;
		}
		for (BoundaryEventInstance boundaryEvent : attachedBoundaryEventInstances) {
			boundaryEvent.skip();
		}
		// TODO: maybe state after creation of following
		setState(State.TERMINATED);
		this.getFragmentInstance().createFollowing(getControlNode());
	}


	@Override
	public void skip() {
		for (BoundaryEventInstance boundaryEvent : attachedBoundaryEventInstances) {
			boundaryEvent.skip();
		}
		setState(State.SKIPPED);
	}

	public void cancel() {
		if (!this.getState().equals(State.RUNNING)) {
			String errorMsg = "Tried cancelling an activity instance, which is not running";
			log.warn(errorMsg);
			throw new IllegalStateException(errorMsg);
		}
		setState(State.CANCEL);
		// TODO: is the following right?
		List<DataObject> workingItems = getSelectedDataObjects();
		this.getCaseExecutioner().getDataManager().unlockDataObjects(workingItems);
	}

	public abstract void execute();

	// GETTER & SETTER
	@Override
	public void setState(State state) {
		getCaseExecutioner().logActivityTransition(this, state);
		super.setState(state);
	}

	@Override
	public AbstractActivity getControlNode() {
		return (AbstractActivity) super.getControlNode();
	}

	public boolean isAutomaticTask() {
		return isAutomaticTask;
	}

	/**
	 * Tries to set the flag for automatic execution of this activity instance to {@literal true}. 
	 * This fails if the activity has multiple input or output sets which would require user choice.
	 * Gateways themselves take care to forbid automatic execution of their successor activities, 
	 * @see ExclusiveGatewayInstance. 
	 */
	public void allowAutomaticExecution() {
		if (getControlNode().hasUniquePostCondition() &&
				getControlNode().hasUniquePreCondition()) {
			isAutomaticTask = true;
		} else {
			log.warn("Tasks with more than one input or output set cannot be executed automatically.");
			isAutomaticTask = false;
		}
	}

	/**
	 * Sets the flag for automatic execution of this activity to {@literal false}. 
	 * This is used by exclusive gateways to prevent that the branch starting with the automatic
	 * activity is always taken. In this case, the automatic activity has to be started manually
	 * by the user.  
	 */
	public void forbidAutomaticStart() {
		isAutomaticTask = false;
	}
	
	public List<BoundaryEventInstance> getAttachedBoundaryEventInstances() {
		return attachedBoundaryEventInstances;
	}

	public void setAttachedBoundaryEventInstances(List<BoundaryEventInstance> attachedBoundaryEventInstances) {
		this.attachedBoundaryEventInstances = attachedBoundaryEventInstances;
	}
}
