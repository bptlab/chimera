package de.uni_potsdam.hpi.bpt.bp2014.jcore;

public class EventIncomingControlFlowBehavior extends IncomingBehavior{
    String type;

    public EventIncomingControlFlowBehavior(ControlNodeInstance controlNodeInstance, ScenarioInstance scenarioInstance, String type){
        this.controlNodeInstance = controlNodeInstance;
        this.scenarioInstance = scenarioInstance;
        this.type = type;
    }

    @Override
    public void enableControlFlow(){
        scenarioInstance.restartFragment(this.controlNodeInstance.fragmentInstance_id);
    }
}
