package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DataObject;
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
                if (!scenarioInstance.checkDataObjectState(dataObject.getId(), dataObject.getStateID())) {
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
            scenarioInstance.beginEnabledReferenceControlNodeInstanceForControlNodeInstanceID(controlNodeInstance.getControlNode_id(), activity_id);
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
                scenarioInstance.setDataObjectInstanceToOnChange(dataObject);
            }
        }
    }
}
