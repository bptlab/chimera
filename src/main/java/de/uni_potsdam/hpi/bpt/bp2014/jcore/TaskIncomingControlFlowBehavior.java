package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.DataObject;

import java.util.LinkedList;

/***********************************************************************************
*   
*   _________ _______  _        _______ _________ _        _______ 
*   \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
*      )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
*      |  |  | (__    |   \ | || |         | |   |   \ | || (__    
*      |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)   
*      |  |  | (      | | \   || | \_  )   | |   | | \   || (      
*   |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
*   (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
*
*******************************************************************
*
*   Copyright © All Rights Reserved 2014 - 2015
*
*   Please be aware of the License. You may found it in the root directory.
*
************************************************************************************/


public class TaskIncomingControlFlowBehavior extends IncomingBehavior{
    //Database Connection objects
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
        if(checkInputObjects()){
            ((ActivityStateMachine) stateMachine).enableData();
        }
    }

    public void checkDataFlowEnabled(){
        if(checkInputObjects()){
            ((ActivityStateMachine) stateMachine).enableData();
        }else{
            ((ActivityStateMachine) stateMachine).disableData();
        }
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

    public void startReferences(){
        for(int activity_id: ((ActivityInstance)controlNodeInstance).getReferences()){
            scenarioInstance.beginEnabledReferenceControlNodeInstanceForControlNodeInstanceID(activity_id);
        }
    }
    public void setDataObjectInstancesOnChange(){
        LinkedList<Integer> outputSets = dbDataFlow.getOutputSetsForControlNode(controlNodeInstance.controlNode_id);
        //TODO: Output Set
        for(int outputSet: outputSets){
            LinkedList<Integer> dataObjects = dbDataNode.getDataObjectIdsForDataSets(outputSet);
            LinkedList<Integer> states = dbDataNode.getDataStatesForDataSets(outputSet);
            for(int i=0; i < dataObjects.size(); i++){
                scenarioInstance.setDataObjectInstanceToOnChange(dataObjects.get(i));
            }
        }
    }
}
