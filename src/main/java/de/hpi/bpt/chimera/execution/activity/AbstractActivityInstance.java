package de.hpi.bpt.chimera.execution.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.AbstractDataControlNodeInstance;
import de.hpi.bpt.chimera.execution.ControlNodeInstanceFactory;
import de.hpi.bpt.chimera.execution.DataObject;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.State;
import de.hpi.bpt.chimera.execution.event.BoundaryEventInstance;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.BoundaryEvent;

public abstract class AbstractActivityInstance extends AbstractDataControlNodeInstance {
	private static final Logger log = Logger.getLogger(AbstractActivityInstance.class);

	private boolean isAutomaticTask;
	// TODO: find out what canTerminate is exactly needed for
	// private boolean canTerminate;
	private List<BoundaryEventInstance> attachedBoundaryEventInstances;

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
	 * Enable the ControlFlow and clear the DataObjectInstances that were
	 * selected. If the Abstract Activity is an automatic task and does not have
	 * any input condition sets begin the task automatically by the case
	 * executioner.
	 */
	@Override
	public void enableControlFlow() {
		getSelectedDataObjects().clear();
		if (getState().equals(State.INIT)) {
			setState(State.CONTROLFLOW_ENABLED);
		}
		if (getState().equals(State.DATAFLOW_ENABLED) || getControlNode().getPreCondition().isFulfilled(getDataManager().getDataStateConditions())) {
			setState(State.READY);
		}

		if (this.isAutomaticTask && !getControlNode().hasPreCondition()) {
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
			BoundaryEventInstance boundaryEventInstance = (BoundaryEventInstance) ControlNodeInstanceFactory.createControlNodeInstance(boundaryEvent, getFragmentInstance());
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

	public void setAutomaticTask(boolean isAutomaticTask) {
		this.isAutomaticTask = isAutomaticTask;
	}

	public List<BoundaryEventInstance> getAttachedBoundaryEventInstances() {
		return attachedBoundaryEventInstances;
	}

	public void setAttachedBoundaryEventInstances(List<BoundaryEventInstance> attachedBoundaryEventInstances) {
		this.attachedBoundaryEventInstances = attachedBoundaryEventInstances;
	}
}
