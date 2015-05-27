package de.uni_potsdam.hpi.bpt.bp2014.jcore;


/**
 * This class represents events.
 * This engine supports only end and starts event. But only end events get represented as instance.
 * So this class represents an end event.
 */

public class EventInstance extends ControlNodeInstance {
    private final ScenarioInstance scenarioInstance;
    private final String type;
    //Only support Event is an End Event
    //Don't writes anything in the database

    /**
     * Creates and initializes a new event instance.
     *
     * @param type                This is the type of the event.
     * @param fragmentInstance_id This is the database id from the fragment instance.
     * @param scenarioInstance    This is an instance from the class ScenarioInstance.
     */
    public EventInstance(int fragmentInstance_id, ScenarioInstance scenarioInstance, String type) {
        this.scenarioInstance = scenarioInstance;
        this.fragmentInstance_id = fragmentInstance_id;
        this.type = type;
        this.incomingBehavior = new EventIncomingControlFlowBehavior(this, scenarioInstance, type);
    }

    @Override
    public boolean skip() {
        return false;
    }

    @Override
    public boolean terminate() {
        return false;
    }
}
