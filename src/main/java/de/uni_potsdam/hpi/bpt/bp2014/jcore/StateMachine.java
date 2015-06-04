package de.uni_potsdam.hpi.bpt.bp2014.jcore;


/**
 * This is a abstract class for all state machines.
 */
public abstract class StateMachine {
    protected ScenarioInstance scenarioInstance;
    protected int controlNodeInstance_id;
    protected ControlNodeInstance controlNodeInstance;
    protected String state;

    public abstract boolean skip();

    public abstract boolean terminate();

}
