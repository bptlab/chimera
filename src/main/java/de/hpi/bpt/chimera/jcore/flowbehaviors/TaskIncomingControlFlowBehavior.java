package de.hpi.bpt.chimera.jcore.flowbehaviors;

import de.hpi.bpt.chimera.database.data.DbDataFlow;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.database.data.DbDataNode;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractControlNodeInstance;
import de.hpi.bpt.chimera.jcore.data.DataManager;

import java.util.List;

/**
 * This class implements incoming behavior for tasks.
 */
public class TaskIncomingControlFlowBehavior extends AbstractIncomingBehavior {
	/**
	 * Database ConnectionWrapper objects
	 */
	private final DbDataFlow dbDataFlow = new DbDataFlow();
	private final DbDataNode dbDataNode = new DbDataNode();

    private ActivityInstance activityInstance;
	/**
	 * Initializes the TaskIncomingControlFlowBehavior.
	 *
	 * @param instance the activity instance.
	 * @param scenarioInstance    This is an instance from the class ScenarioInstance.
	 */
	public TaskIncomingControlFlowBehavior(ActivityInstance instance,
										   ScenarioInstance scenarioInstance) {
        this.setControlNodeInstance(instance);
		this.setScenarioInstance(scenarioInstance);
        if (checkInputObjects()) {
            enableData();
        }
	}

	@Override public void enableControlFlow() {
        State currentState = this.getControlNodeInstance().getState();
        if (State.INIT.equals(currentState)) {
            this.getControlNodeInstance().setState(State.CONTROLFLOW_ENABLED);
        }
        if (State.DATAFLOW_ENABLED.equals(currentState) || checkInputObjects()) {
            this.getControlNodeInstance().setState(State.READY);
        }
	}

    private void enableData() {
        AbstractControlNodeInstance instance = this.getControlNodeInstance();
        if (instance.getState().equals(State.INIT)) {
            instance.setState(State.DATAFLOW_ENABLED);
        } else if (instance.getState().equals(State.CONTROLFLOW_ENABLED)) {
            instance.setState(State.READY);
        }
    }

    private void disableData() {
        AbstractControlNodeInstance instance = this.getControlNodeInstance();
        if (State.DATAFLOW_ENABLED.equals(instance.getState())) {
            instance.setState(State.INIT);
        } else if (State.READY.equals(instance.getState())) {
                instance.setState(State.CONTROLFLOW_ENABLED);
        }
    }

	/**
	 * Checks if the activity can get data flow enabled.
	 * Sets the activity to data flow enabled if the activity is data flow enabled.
	 */
	public void updateDataFlow() {
		if (checkInputObjects()) {
			enableData();
		} else {
            disableData();
    	}
	}

	/**
	 * Checks if the input data objects have the same state
	 * as the data object of the scenario instance.
	 *
	 * @return true if the data object have the same state as the inputs of the activity.
	 * 			false if not.
	 */
	private Boolean checkInputObjects() {
		List<Integer> inputSets = dbDataFlow.getInputSetsForControlNode(
				getControlNodeInstance().getControlNodeId());
        // if the activity has no input set it is dataflow enabled
        if (!inputSets.isEmpty()) {
            DataManager dataManager = this.getScenarioInstance().getDataManager();

            for (Integer inputSet : inputSets) {
                if (dataManager.checkInputSet(inputSet)) {
                    return true;
                }
            }
            return false;
        }
        return true;
	}

	/**
	 * Sets the data objects which are outputs of the activity to on change.
	 */
	public void lockDataObjects(List<Integer> dataObjectIds) {
		DataManager dataManager = this.getScenarioInstance().getDataManager();
		dataObjectIds.forEach(dataManager::lockDataobject);
	}
}
