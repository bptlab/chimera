package de.hpi.bpt.chimera.jcore.flowbehaviors;

import de.hpi.bpt.chimera.database.data.DbDataFlow;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.executionbehaviors.ActivityStateMachine;
import de.hpi.bpt.chimera.database.data.DbDataNode;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractControlNodeInstance;
import de.hpi.bpt.chimera.jcore.data.DataManager;
import de.hpi.bpt.chimera.jcore.executionbehaviors.AbstractStateMachine;

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

	/**
	 * Initializes the TaskIncomingControlFlowBehavior.
	 *
	 * @param controlNodeInstance This is an AbstractControlNodeInstance.
	 * @param scenarioInstance    This is an instance from the class ScenarioInstance.
	 * @param stateMachine        This is an instance from the class AbstractStateMachine.
	 */
	public TaskIncomingControlFlowBehavior(AbstractControlNodeInstance controlNodeInstance,
										   ScenarioInstance scenarioInstance, AbstractStateMachine stateMachine) {
		this.setControlNodeInstance(controlNodeInstance);
		this.setScenarioInstance(scenarioInstance);
		this.setStateMachine(stateMachine);
		if (checkInputObjects()) {
			((ActivityStateMachine) stateMachine).enableData();
		}
	}

	@Override public void enableControlFlow() {
		((ActivityStateMachine) this.getStateMachine()).enableControlFlow();
		if (checkInputObjects()) {
			((ActivityStateMachine) this.getStateMachine()).enableData();
		}
	}

	/**
	 * Checks if the activity can get data flow enabled.
	 * Sets the activity to data flow enabled if the activity is data flow enabled.
	 */
	public void checkDataFlowEnabledAndEnableDataFlow() {
		if (checkInputObjects()) {
			((ActivityStateMachine) this.getStateMachine()).enableData();
		} else {
			((ActivityStateMachine) this.getStateMachine()).disableData();
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
