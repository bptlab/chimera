package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataNode;

import java.util.LinkedList;

public class TaskIncomingControlFlowBehavior extends IncomingBehavior{
    private DbDataFlow dbDataFlow = new DbDataFlow();
    private DbDataNode dbDataNode = new DbDataNode();


    public TaskIncomingControlFlowBehavior(ControlNodeInstance controlNodeInstance, ScenarioInstance scenarioInstance, StateMachine stateMachine){
        this.controlNodeInstance = controlNodeInstance;
        this.scenarioInstance = scenarioInstance;
        this.stateMachine = stateMachine;


        if(checkInputObjects()){
            ((ActivityStateMachine) stateMachine).enableData();
        }
    }

    @Override
    public void enableControlFlow(){
        ((ActivityStateMachine)stateMachine).enableControlFlow();
        //TODO: Data Objects HERE
    }

    private Boolean checkInputObjects(){
        LinkedList<Integer> inputSets = dbDataFlow.getInputSetsForControlNode(controlNodeInstance.controlNode_id);
        Boolean loopCheck = true;
        for(int inputSet: inputSets){
            LinkedList<Integer> dataObjects = dbDataNode.getDataObjectIdsForDataSets(inputSet);
            LinkedList<Integer> states = dbDataNode.getDataStatesForDataSets(inputSet);
            for(int i=0; i < dataObjects.size(); i++){
                loopCheck = true;
                if(!scenarioInstance.checkDataObjectState(dataObjects.get(i), states.get(i))){
                    loopCheck = false;
                    break;
                }
            }
            if(loopCheck == true) break;
        }
        return loopCheck;
    }
}
