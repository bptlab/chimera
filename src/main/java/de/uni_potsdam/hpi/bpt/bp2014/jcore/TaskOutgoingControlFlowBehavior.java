package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DataObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataNode;

import java.util.LinkedList;

/**
 * Handles the behavior of a terminating activity instance.
 */
public class TaskOutgoingControlFlowBehavior extends AbstractParallelOutgoingBehavior {
	private final ActivityInstance activityInstance;
	/**
	 * Database Connection objects.
	 */
	private final DbDataNode dbDataNode = new DbDataNode();
	private final DbDataFlow dbDataFlow = new DbDataFlow();

	/**
	 * Initializes the TaskOutgoingControlFlowBehavior.
	 *
	 * @param activityId         This is the database id from the activity instance.
	 * @param scenarioInstance    This is an instance from the class ScenarioInstance.
	 * @param fragmentInstanceId This is the database id from the fragment instance.
	 * @param activityInstance 	This is an AbstractControlNodeInstance.
	 */
	public TaskOutgoingControlFlowBehavior(int activityId, ScenarioInstance scenarioInstance,
			int fragmentInstanceId, ActivityInstance activityInstance) {
		this.setControlNodeId(activityId);
		this.setScenarioInstance(scenarioInstance);
		this.setFragmentInstanceId(fragmentInstanceId);
		this.activityInstance = activityInstance;
	}

	@Override public void terminate() {
		this.terminate(-1);
	}

	/**
	 * Terminates the activity.
	 *
	 * @param outputSetId of the set that get executed.
	 */
	public void terminate(int outputSetId) {
		setDataStates(outputSetId);
		this.checkAfterTermination();
		this.enableFollowing();
		this.runAfterTermination();
	}

	/**
	 * Sets the states of the data object to the output states of the activity.
	 * Sets all this data object to not on change.
	 */
	private void setDataStates(int outputSetId) {
		LinkedList<Integer> outputSets = dbDataFlow.getOutputSetsForControlNode(
				this.getControlNodeId());
		for (int outputSet : outputSets) {
			LinkedList<DataObject> dataObjects =
					dbDataNode.getDataObjectsForDataSets(outputSet);
			for (DataObject dataObject : dataObjects) {
				//resets DataObjectInstance from OnChange back to not OnChange
				this.setDataObjectInstanceToNotOnChange(dataObject.getId());
			}
		}
		if (outputSets.size() != 0) {
			int outputSet = outputSets.get(0);
			if (outputSets.size() > 1) {
				outputSet = outputSetId;
			}
			LinkedList<DataObject> dataObjects = dbDataNode.getDataObjectsForDataSets(
					outputSet);
			for (DataObject dataObject : dataObjects) {
				this.changeDataObjectInstanceState(
						dataObject.getId(), dataObject.getStateID());
			}
		}

	}

	/**
	 * Terminates all referential running activities.
	 */
	public void terminateReferences() {
		for (int activityId : (activityInstance).getReferences()) {
			this.terminateReferenceControlNodeInstanceForControlNodeInstanceID(
					activityId);
		}
	}

	/**
	 * Change the state of the given data object.
	 *
	 * @param dataObjectId This is the database id from the data object.
	 * @param stateId      This is the database id from the state.
	 * @return true if the data object state could been changed. false if not
	 */
	public Boolean changeDataObjectInstanceState(int dataObjectId, int stateId) {
		for (DataObjectInstance dataObjectInstance
				: this.getScenarioInstance().getDataObjectInstances()) {
			if (dataObjectInstance.getDataObjectId() == dataObjectId) {
				dataObjectInstance.setState(stateId);
				return true;
			}
		}
		return false;
	}

	/**
	 * Sets the data object to not on change.
	 * Write this into the database.
	 *
	 * @param dataObjectId This is the database id from the data object.
	 * @return true if the on change could been set. false if not.
	 */
	public Boolean setDataObjectInstanceToNotOnChange(int dataObjectId) {
		DataObjectInstance dataObjectInstanceOnChange = null;
		for (DataObjectInstance dataObjectInstance : this.getScenarioInstance()
				.getDataObjectInstancesOnChange()) {
			if (dataObjectInstance.getDataObjectId() == dataObjectId) {
				dataObjectInstanceOnChange = dataObjectInstance;
				break;
			}
		}
		if (dataObjectInstanceOnChange != null) {
			this.getScenarioInstance().getDataObjectInstancesOnChange()
					.remove(dataObjectInstanceOnChange);
			this.getScenarioInstance().getDataObjectInstances()
					.add(dataObjectInstanceOnChange);
			dataObjectInstanceOnChange.setOnChange(false);
			return true;
		}
		return false;
	}

	/**
	 * Checks if the referenced controlNode can be terminated.
	 * The referenced controlNode have to be referential running.
	 *
	 * @param controlNodeId This is the database id from the control node.
	 */
	public void terminateReferenceControlNodeInstanceForControlNodeInstanceID(
			int controlNodeId) {
		for (AbstractControlNodeInstance controlNodeInstance : this.getScenarioInstance()
				.getReferentialRunningControlNodeInstances()) {
			if (controlNodeInstance.getControlNodeId() == controlNodeId) {
				if (controlNodeInstance.getClass() == ActivityInstance.class) {
					((ActivityInstance) controlNodeInstance)
							.referenceTerminated();
					return;
				}
			}
		}
	}
}
