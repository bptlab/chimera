package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlFlow;


public abstract class IncomingBehavior {
    protected DbControlFlow dbControlFlow = new DbControlFlow();
    protected ScenarioInstance scenarioInstance;
    protected ControlNodeInstance controlNodeInstance;
    protected StateMachine stateMachine;

    /**
     * Enable the control flow for the control node instance.
     */
    public abstract void enableControlFlow();
}
