package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DataObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataNode;

import java.util.LinkedList;


public class TaskIncomingControlFlowBehavior extends IncomingBehavior {
    /**
     * Database Connection objects
     */
    private final DbDataFlow dbDataFlow = new DbDataFlow();
    private final DbDataNode dbDataNode = new DbDataNode();

    /**
     * Initializes the TaskIncomingControlFlowBehavior.
     *
     * @param controlNodeInstance This is an instance from the class ControlNodeInstance.
     * @param scenarioInstance    This is an instance from the class ScenarioInstance.
     * @param stateMachine        This is an instance from the class StateMachine.
     */
    public TaskIncomingControlFlowBehavior(ControlNodeInstance controlNodeInstance, ScenarioInstance scenarioInstance, StateMachine stateMachine) {
        this.controlNodeInstance = controlNodeInstance;
        this.scenarioInstance = scenarioInstance;
        this.stateMachine = stateMachine;
        if (checkInputObjects()) {
            ((ActivityStateMachine) stateMachine).enableData();
        }
    }

    @Override
    public void enableControlFlow() {
        ((ActivityStateMachine) stateMachine).enableControlFlow();
        if (checkInputObjects()) {
            ((ActivityStateMachine) stateMachine).enableData();
        }
    }

    /**
     * Checks if the activity can get data flow enabled.
     * Sets the activity to data flow enabled if the activity is data flow enabled.
     */
    public void checkDataFlowEnabledAndEnableDataFlow() {
        if (checkInputObjects()) {
            ((ActivityStateMachine) stateMachine).enableData();
        } else {
            ((ActivityStateMachine) stateMachine).disableData();
        }
    }

    /**
     * Checks if the input data objects have the same state as the data object of the scenario instance.
     *
     * @return true if the data object have the same state as the inputs of the activity. false if not.
     */
    private Boolean checkInputObjects() {
        LinkedList<Integer> inputSets = dbDataFlow.getInputSetsForControlNode(controlNodeInstance.controlNode_id);
        Boolean loopCheck = true;
        for (int inputSet : inputSets) {
            LinkedList<DataObject> dataObjects = dbDataNode.getDataObjectsForDataSets(inputSet);
            for (DataObject dataObject : dataObjects) {
                loopCheck = true;
                if (!this.compareDataObjectState(dataObject.getId(), dataObject.getStateID())) {
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
        for (int activity_id : ((ActivityInstance) controlNodeInstance).getReferences()) {
            this.beginEnabledReferenceControlNodeInstanceForControlNodeInstanceID(controlNodeInstance.getControlNode_id(), activity_id);
        }
    }

    /**
     * Sets the data objects which are outputs of the activity to on change.
     */
    public void setDataObjectInstancesOnChange() {
        LinkedList<Integer> outputSets = dbDataFlow.getOutputSetsForControlNode(controlNodeInstance.controlNode_id);
        for (int outputSet : outputSets) {
            LinkedList<Integer> dataObjects = dbDataNode.getDataObjectIdsForDataSets(outputSet);
            for (int dataObject : dataObjects) {
                this.setDataObjectInstanceToOnChange(dataObject);
            }
        }
    }


    /**
     * Sets the data object to on change.
     * Write this into the database.
     *
     * @param dataObject_id This is the database id from the data object.
     * @return true if the on change could been set. false if not.
     */
    public Boolean setDataObjectInstanceToOnChange(int dataObject_id) {
        DataObjectInstance dataObjectInstanceOnChange = null;
        for (DataObjectInstance dataObjectInstance : scenarioInstance.getDataObjectInstances()) {
            if (dataObjectInstance.getDataObject_id() == dataObject_id) {
                dataObjectInstanceOnChange = dataObjectInstance;
                break;
            }
        }
        if (dataObjectInstanceOnChange != null) {
            scenarioInstance.getDataObjectInstances().remove(dataObjectInstanceOnChange);
            scenarioInstance.getDataObjectInstancesOnChange().add(dataObjectInstanceOnChange);
            dataObjectInstanceOnChange.setOnChange(true);
            return true;
        }
        return false;
    }

    /**
     * Compares the given state for a data object with the state from the data object in the scenario.
     *
     * @param dataObject_id This is the database id from the data object.
     * @param state_id      This is the database id from the state.
     * @return true if the data object has the same state. false if not
     */
    public Boolean compareDataObjectState(int dataObject_id, int state_id) {
        for (DataObjectInstance dataObjectInstance : scenarioInstance.getDataObjectInstances()) {
            if (dataObjectInstance.getDataObject_id() == dataObject_id && dataObjectInstance.getState_id() == state_id) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks if the referenced controlNode can be started.
     * The referenced controlNode have to be control flow enabled and (data flow enabled or must have the same data output)
     *
     * @param controlNode_id           This is the database id from a control node.
     * @param referencedControlNode_id This is the database id from a control node.
     */
    public void beginEnabledReferenceControlNodeInstanceForControlNodeInstanceID(int controlNode_id, int referencedControlNode_id) {
        for (ControlNodeInstance controlNodeInstance : scenarioInstance.getControlFlowEnabledControlNodeInstances()) {
            if (controlNodeInstance.controlNode_id == referencedControlNode_id) {
                if (controlNodeInstance.getClass() == ActivityInstance.class) {
                    DbControlNode dbControlNode = new DbControlNode();
                    if (scenarioInstance.getEnabledControlNodeInstances().contains(controlNodeInstance) || dbControlNode.controlNodesHaveSameOutputs(controlNode_id, referencedControlNode_id)) {
                        ((ActivityInstance) controlNodeInstance).referenceStarted();
                        return;
                    }
                }
            }
        }

    }

}
