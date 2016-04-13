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
		LinkedList<Integer> inputSets = dbDataFlow.getInputSetsForControlNode(
				getControlNodeInstance().getControlNodeId());
		Boolean loopCheck = true;
		for (int inputSet : inputSets) {
			LinkedList<DataObject> dataObjects =
					dbDataNode.getDataObjectsForDataSets(inputSet);
			for (DataObject dataObject : dataObjects) {
				loopCheck = true;
				if (!this.compareDataObjectState(
						dataObject.getId(), dataObject.getStateID())) {
					loopCheck = false;
					break;
				}
			}
			if (loopCheck) {
				break;
			}
		}
		return loopCheck;
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
	public void setDataObjectInstancesOnChange() {
		LinkedList<Integer> outputSets = dbDataFlow
				.getOutputSetsForControlNode(
						getControlNodeInstance().getControlNodeId());
		DataManager dataManager = this.getScenarioInstance().getDataManager();
        for (int outputSet : outputSets) {
			LinkedList<Integer> dataObjects = dbDataNode
					.getDataObjectIdsForDataSets(outputSet);
			for (int dataObject : dataObjects) {
                dataManager.lockDataobject(dataObject);
			}
		}
	}


	/**
	 * Compares the state of a data object with the state from the data object in the scenario.
	 *
	 * @param dataObjectId This is the database id from the data object.
	 * @param stateId      This is the database id from the state.
	 * @return true if the data object has the same state. false if not
	 */
	public Boolean compareDataObjectState(int dataObjectId, int stateId) {
        DataManager dataManager = getScenarioInstance().getDataManager();
        for (DataObjectInstance dataObjectInstance : dataManager.getDataObjectInstances()) {
			if (dataObjectInstance.getDataObjectId() == dataObjectId
					&& dataObjectInstance.getStateId() == stateId) {
				return true;
			}
		}
		return false;
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
