package de.hpi.bpt.chimera.execution.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hpi.bpt.chimera.execution.ControlNodeInstance;
import de.hpi.bpt.chimera.execution.DataObjectInstance;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.model.fragment.bpmn.DataNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.Activity;

public abstract class AbstractActivityInstance extends ControlNodeInstance {
	private Activity activity;
	// standard for human task is false
	private boolean isAutomaticTask;
	// TODO: find out what canTerminate is exactly needed for
	// private boolean canTerminate;
	private Map<String, DataObjectInstance> selectedDataObjectInstances;

	/**
	 * Create a new AbstractActivityInstance.
	 * 
	 * @param activity
	 * @param fragmentInstance
	 */
	public AbstractActivityInstance(Activity activity, FragmentInstance fragmentInstance) {
		super(activity, fragmentInstance);
		this.setActivity(activity);
		this.isAutomaticTask = false;
		this.selectedDataObjectInstances = new HashMap<>();
		setState(State.INIT);
	}

	/**
	 * Enable the ControlFlow and clear the DataObjectInstances that were
	 * selected.
	 */
	@Override
	public void enableControlFlow() {
		this.selectedDataObjectInstances.clear();
		if (getState().equals(State.INIT)) {
			setState(State.CONTROLFLOW_ENABLED);
		}
		if (getState().equals(State.DATAFLOW_ENABLED) || getCaseExecutioner().isDataFlowEnabled(activity)) {
			setState(State.READY);
		}
	}

	/**
	 * Used for updating the DataFlow of the ActivityInstance.
	 */
	public void checkDataFlow() {
		if (getCaseExecutioner().isDataFlowEnabled(activity)) {
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
	 * Begin the Activity. The selected DataObjectInstances were set by the
	 * CaseExecutioner.
	 */
	@Override
	public void begin() {
		// assert ... maybe not needed
		if (!getState().equals(State.READY)) {
			return;
		}
		// TODO: think about whether selected DataObjectInstances should be set
		// here or by CaseExecutioner
		// saved here. By CaseExecutioner should be better.
		getFragmentInstance().updateDataFlow();
		// TODO: implement skipBehaviour
		// TODO: implement creation of possible attached BoundaryEvent
		setState(State.RUNNING);
		if (this.isAutomaticTask) {
			terminate();
		}
	}

	/**
	 * Terminate the Activity. The transition of the States of the DataClasses
	 * is handled by the ExecutionService.
	 */
	@Override
	public void terminate() {
		if (!getState().equals(State.RUNNING))
			return;
		// this.getCaseExecutioner().createDataObjectInstances(activity);
		this.getFragmentInstance().createFollowing(activity);
		this.getCaseExecutioner().startAutomaticTasks();
		// TODO: maybe state before creation of following
		setState(State.TERMINATED);
	}


	@Override
	public void skip() {
		setState(State.SKIPPED);
	}

	// GETTER & SETTER
	@Override
	public void setState(State state) {
		getCaseExecutioner().logActivityTransition(this, state);
		super.setState(state);
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Map<String, DataObjectInstance> getSelectedDataObjectInstances() {
		return selectedDataObjectInstances;
	}

	public void setSelectedDataObjectInstances(Map<String, DataObjectInstance> selectedDataObjectInstances) {
		this.selectedDataObjectInstances = selectedDataObjectInstances;
	}

	public boolean isAutomaticTask() {
		return isAutomaticTask();
	}

	public void setAutomaticTask(boolean isAutomaticTask) {
		this.isAutomaticTask = isAutomaticTask;
	}
}
