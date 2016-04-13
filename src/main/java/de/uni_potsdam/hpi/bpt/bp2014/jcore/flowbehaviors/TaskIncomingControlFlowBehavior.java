package de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.database.DataObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.*;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.ActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors.AbstractStateMachine;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors.ActivityStateMachine;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * This class implements incoming behavior for tasks.
 */
public class TaskIncomingControlFlowBehavior extends AbstractIncomingBehavior {
	/**
	 * Database Connection objects
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
	 * Starts all referential activities of the activity.
	 */
	public void startReferences() {
		for (int activityId : ((ActivityInstance) getControlNodeInstance())
				.getReferences()) {
			this.beginEnabledReferenceControlNodeInstanceForControlNodeInstanceID(
					getControlNodeInstance().getControlNodeId(), activityId);
		}
	}

	/**
	 * Sets the data objects which are outputs of the activity to on change.
	 */
	public void lockDataObjectInstances() {
		List<Integer> inputSets = dbDataFlow.getInputSetsForControlNode(
						getControlNodeInstance().getControlNodeId());
		DataManager dataManager = this.getScenarioInstance().getDataManager();

        for (int outputSet : inputSets) {
			List<Integer> dataObjects = dbDataNode
					.getDataObjectIdsForDataSets(outputSet);
			for (int dataObject : dataObjects) {
                dataManager.lockDataobject(dataObject);
			}
		}
	}



	/**
	 * checks if the referenced controlNode can be started.
	 * The referenced controlNode have to be control flow enabled
	 * and data flow enabled or must have the same data output.
	 *
	 * @param controlNodeId           This is the database id from a control node.
	 * @param referencedControlNodeId This is the database id from a control node.
	 */
	public void beginEnabledReferenceControlNodeInstanceForControlNodeInstanceID(
			int controlNodeId, int referencedControlNodeId) {
		for (AbstractControlNodeInstance controlNodeInstance : getScenarioInstance()
				.getControlFlowEnabledControlNodeInstances()) {
			if (controlNodeInstance.getControlNodeId() == referencedControlNodeId) {
				if (controlNodeInstance.getClass() == ActivityInstance.class) {
					DbControlNode dbControlNode = new DbControlNode();
					if (getScenarioInstance().getEnabledControlNodeInstances()
							.contains(controlNodeInstance)
							|| dbControlNode
							.controlNodesHaveSameOutputs(controlNodeId,
									referencedControlNodeId)) {
						((ActivityInstance) controlNodeInstance)
								.referenceStarted();
						return;
					}
				}
			}
		}

	}

}
