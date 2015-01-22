package de.uni_potsdam.hpi.bpt.bp2014.jcore;

public class EventInstance extends ControlNodeInstance {
    private ScenarioInstance scenarioInstance;
    private String type;
    //Only support Event is an End Event
    //Don't writes anything in the database
    public EventInstance(int fragmentInstance_id, ScenarioInstance scenarioInstance,String type){
        this.scenarioInstance = scenarioInstance;
        this.fragmentInstance_id = fragmentInstance_id;
        this.type = type;
        this.incomingBehavior = new EventIncomingControlFlowBehavior(this, scenarioInstance, type);
    }
}
