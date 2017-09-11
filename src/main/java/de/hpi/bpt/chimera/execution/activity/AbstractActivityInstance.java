package de.hpi.bpt.chimera.execution.activity;

import java.util.HashMap;
import java.util.Map;

import de.hpi.bpt.chimera.execution.ControlNodeInstance;
import de.hpi.bpt.chimera.execution.DataObjectInstance;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.model.fragment.bpmn.Activity;

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
		this.state = State.INIT;
	}

	/**
	 * Enable the ControlFlow and clear the DataObjectInstances that were
	 * selected.
	 */
	@Override
	public void enableControlFlow() {
		this.selectedDataObjectInstances.clear();
		if (state.equals(State.INIT)) {
			state = State.CONTROLFLOW_ENABLED;
		}
		if (state.equals(State.DATAFLOW_ENABLED) || checkInputObjects()) {
			state = State.READY;
		}
	}

	private boolean checkInputObjects() {
		// TODO: Checks if the input data objects have the same state as the
		// data object of the scenario instance.
		return true;
	}

	private void enableData() {
		if (state.equals(State.INIT)) {
			state = State.DATAFLOW_ENABLED;
		} else if (state.equals(State.CONTROLFLOW_ENABLED)) {
			state = State.READY;
		}
	}

	private void disableData() {
		if (state.equals(State.DATAFLOW_ENABLED)) {
			state = State.INIT;
		} else if (state.equals(State.READY)) {
			state = State.CONTROLFLOW_ENABLED;
		}
	}

	/**
	 * Begin the Activity. The selected DataObjectInstances were set by the
	 * CaseExecutioner.
	 */
	@Override
	public void begin() {
		// assert ... maybe not needed
		if (!state.equals(State.READY)) {
			return;
		}
		// TODO: think about whether selected DataObjectInstances should be set
		// here or by CaseExecutioner
		// saved here. By CaseExecutioner should be better.
		this.fragmentInstance.updateDataFlow();
		// TODO: implement skipBehaviour
		// TODO: implement creation of possible attached BoundaryEvent
		this.state = State.RUNNING;
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
		this.fragmentInstance.getCase().getCaseExecutioner().createDataObjectInstances(activity);
		this.fragmentInstance.enableFollowing(activity);
		this.fragmentInstance.getCase().getCaseExecutioner().startAutomaticTasks();
		this.state = State.TERMINATED;
	}

	@Override
	public void skip() {
		this.state = State.SKIPPED;
	}

	// GETTER & SETTER
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
}
