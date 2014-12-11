package de.uni_potsdam.hpi.bpt.bp2014.jengine;

/**
 * Created by jaspar.mang on 24.11.14.
 */
public class TaskIncomingControlFlowBehavior extends IncomingBehavior{

    public TaskIncomingControlFlowBehavior(ControlNodeInstance controlNodeInstance, ScenarioInstance scenarioInstance, StateMachine stateMachine){
        this.controlNodeInstance = controlNodeInstance;
        this.scenarioInstance = scenarioInstance;
        this.stateMachine = stateMachine;

        //TODO: Data Objects HERE
        ((ActivityStateMachine)stateMachine).enableData();
    }

    public void enableControlFlow(){
        ((ActivityStateMachine)stateMachine).enableControlFlow();
    }
}
