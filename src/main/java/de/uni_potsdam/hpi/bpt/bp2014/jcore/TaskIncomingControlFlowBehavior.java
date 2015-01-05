package de.uni_potsdam.hpi.bpt.bp2014.jcore;

public class TaskIncomingControlFlowBehavior extends IncomingBehavior{

    public TaskIncomingControlFlowBehavior(ControlNodeInstance controlNodeInstance, ScenarioInstance scenarioInstance, StateMachine stateMachine){
        this.controlNodeInstance = controlNodeInstance;
        this.scenarioInstance = scenarioInstance;
        this.stateMachine = stateMachine;

        //TODO: Data Objects HERE
        ((ActivityStateMachine)stateMachine).enableData();
    }

    @Override
    public void enableControlFlow(){
        ((ActivityStateMachine)stateMachine).enableControlFlow();
    }
}
