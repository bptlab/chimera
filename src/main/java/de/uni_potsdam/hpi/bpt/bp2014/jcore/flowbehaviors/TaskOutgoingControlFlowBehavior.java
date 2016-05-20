package de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataManager;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.ActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

        ScenarioInstance scenarioInstance = this.getScenarioInstance();
        scenarioInstance.updateDataFlow();
        scenarioInstance.checkXorGatewaysForTermination(this.getControlNodeId());

		this.enableFollowing();
		this.runAutomaticTasks();
	}

    /**
     * Since at the moment each output set can only contain the same data objects with different
     * states it is enough to look only at one output set and free all data objects in this.
     */
    public void cancel() {
        LinkedList<Integer> inputSets = dbDataFlow.getInputSetsForControlNode(
                this.getControlNodeId());
        if (inputSets.size() > 0) {
            int inputSet = inputSets.getFirst();
            for (de.uni_potsdam.hpi.bpt.bp2014.database.DataObject dataObject : dbDataNode.getDataObjectsForDataSets(inputSet)) {
                this.lockDataObjects(dataObject.getId());
            }
        }
    }

	/**
     * TODO improve this method
	 * Sets the states of the data object to the output states of the activity.
	 * Sets all this data object to not on change.
	 */
	private void setDataStates(int outputSetId) {
		List<Integer> outputSets = dbDataFlow.getOutputSetsForControlNode(
				this.getControlNodeId());
		for (int outputSet : outputSets) {
			LinkedList<de.uni_potsdam.hpi.bpt.bp2014.database.DataObject> dataObjects =
					dbDataNode.getDataObjectsForDataSets(outputSet);
			for (de.uni_potsdam.hpi.bpt.bp2014.database.DataObject dataObject : dataObjects) {
				this.lockDataObjects(dataObject.getId());
			}
		}
		if (outputSets.size() != 0) {
			int outputSet = outputSets.get(0);
			if (outputSets.size() > 1) {
				outputSet = outputSetId;
			}
			LinkedList<de.uni_potsdam.hpi.bpt.bp2014.database.DataObject> dataObjects = dbDataNode.getDataObjectsForDataSets(
					outputSet);
			for (de.uni_potsdam.hpi.bpt.bp2014.database.DataObject dataObject : dataObjects) {
				this.changeDataObjectInstanceState(
						dataObject.getId(), dataObject.getStateID());
			}
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
        DataManager dataManager = this.getScenarioInstance().getDataManager();
        return dataManager.changeDataObjectInstanceState(dataObjectId, stateId,
                activityInstance.getControlNodeInstanceId());
	}

	/**
	 * Sets the data object to not on change.
	 * @param dataObjectId This is the database id from the data object.
	 */
	public void lockDataObjects(int dataObjectId) {
        DataManager dataManager = this.getScenarioInstance().getDataManager();
        Optional<DataObject> dataObjectInstance =
                dataManager.getDataobjectForId(dataObjectId);
        assert dataObjectInstance.isPresent(): "invalid data object id";
        dataObjectInstance.get().unlock();
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
