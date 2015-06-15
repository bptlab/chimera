package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DataObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataNode;

import java.util.LinkedList;


/**
 * Handles the behavior of a terminating activity instance.
 */
public class TaskOutgoingControlFlowBehavior extends ParallelOutgoingBehavior {
    private final ActivityInstance activityInstance;
    /**
     * Database Connection objects.
     */
    private final DbDataNode dbDataNode = new DbDataNode();
    private final DbDataFlow dbDataFlow = new DbDataFlow();

    /**
     * Initializes the TaskOutgoingControlFlowBehavior.
     *
     * @param activity_id         This is the database id from the activity instance.
     * @param scenarioInstance    This is an instance from the class ScenarioInstance.
     * @param fragmentInstance_id This is the database id from the fragment instance.
     * @param activityInstance    This is an instance from the class ControlNodeInstance.
     */
    public TaskOutgoingControlFlowBehavior(int activity_id, ScenarioInstance scenarioInstance, int fragmentInstance_id, ActivityInstance activityInstance) {
        this.controlNode_id = activity_id;
        this.scenarioInstance = scenarioInstance;
        this.fragmentInstance_id = fragmentInstance_id;
        this.activityInstance = activityInstance;
    }

    @Override
    public void terminate() {
        this.terminate(-1);
    }

    /**
     * Terminates the activity.
     *
     * @param outputSet_id of the set that get executed.
     */
    public void terminate(int outputSet_id) {
        setDataStates(outputSet_id);
        this.checkAfterTermination();
        this.enableFollowing();
        this.runAfterTermination();
    }

    /**
     * Sets the states of the data object to the output states of the activity.
     * Sets all this data object to not on change.
     */
    private void setDataStates(int outputSet_id) {
        LinkedList<Integer> outputSets = dbDataFlow.getOutputSetsForControlNode(controlNode_id);
        for (int outputSet : outputSets) {
            LinkedList<DataObject> dataObjects = dbDataNode.getDataObjectsForDataSets(outputSet);
            for (DataObject dataObject : dataObjects) {
                //resets DataObjectInstance from OnChange back to not OnChange
                this.setDataObjectInstanceToNotOnChange(dataObject.getId());
            }
        }
        if (outputSets.size() != 0) {
            int outputSet = outputSets.get(0);
            if (outputSets.size() > 1) {
                outputSet = outputSet_id;
            }
            LinkedList<DataObject> dataObjects = dbDataNode.getDataObjectsForDataSets(outputSet);
            for (DataObject dataObject : dataObjects) {
                this.changeDataObjectInstanceState(dataObject.getId(), dataObject.getStateID());
            }
        }


    }

    /**
     * Terminates all referential running activities.
     */
    public void terminateReferences() {
        for (int activity_id : (activityInstance).getReferences()) {
            this.terminateReferenceControlNodeInstanceForControlNodeInstanceID(activity_id);
        }
    }

    /**
     * Change the state of the given data object.
     *
     * @param dataObject_id This is the database id from the data object.
     * @param state_id      This is the database id from the state.
     * @return true if the data object state could been changed. false if not
     */
    public Boolean changeDataObjectInstanceState(int dataObject_id, int state_id) {
        for (DataObjectInstance dataObjectInstance : scenarioInstance.getDataObjectInstances()) {
            if (dataObjectInstance.getDataObject_id() == dataObject_id) {
                dataObjectInstance.setState(state_id);
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the data object to not on change.
     * Write this into the database.
     *
     * @param dataObject_id This is the database id from the data object.
     * @return true if the on change could been set. false if not.
     */
    public Boolean setDataObjectInstanceToNotOnChange(int dataObject_id) {
        DataObjectInstance dataObjectInstanceOnChange = null;
        for (DataObjectInstance dataObjectInstance : scenarioInstance.getDataObjectInstancesOnChange()) {
            if (dataObjectInstance.getDataObject_id() == dataObject_id) {
                dataObjectInstanceOnChange = dataObjectInstance;
                break;
            }
        }
        if (dataObjectInstanceOnChange != null) {
            scenarioInstance.getDataObjectInstancesOnChange().remove(dataObjectInstanceOnChange);
            scenarioInstance.getDataObjectInstances().add(dataObjectInstanceOnChange);
            dataObjectInstanceOnChange.setOnChange(false);
            return true;
        }
        return false;
    }

    /**
     * Checks if the referenced controlNode can be terminated.
     * The referenced controlNode have to be referential running.
     *
     * @param controlNode_id This is the database id from the control node.
     */
    public void terminateReferenceControlNodeInstanceForControlNodeInstanceID(int controlNode_id) {
        for (ControlNodeInstance controlNodeInstance : scenarioInstance.getReferentialRunningControlNodeInstances()) {
            if (controlNodeInstance.controlNode_id == controlNode_id) {
                if (controlNodeInstance.getClass() == ActivityInstance.class) {
                    ((ActivityInstance) controlNodeInstance).referenceTerminated();
                    return;
                }
            }
        }
    }
}
