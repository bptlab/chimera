package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbGatewayInstance;


public class GatewayStateMachine extends StateMachine {
    /**
     * Database Connection objects
     */
    private final DbGatewayInstance dbGatewayInstance = new DbGatewayInstance();

    /**
     * Initializes the GatewayStateMachine
     *
     * @param gateway_id          This is the id of the gateway.
     * @param scenarioInstance    This is an instance from the class ScenarioInstance.
     * @param controlNodeInstance This is an instance from the class ControlNodeInstance.
     */
    public GatewayStateMachine(int gateway_id, ScenarioInstance scenarioInstance, ControlNodeInstance controlNodeInstance) {
        this.scenarioInstance = scenarioInstance;
        this.controlNodeInstance_id = gateway_id;
        this.controlNodeInstance = controlNodeInstance;
        this.state = dbGatewayInstance.getState(controlNodeInstance.controlNodeInstance_id);
        if (state.equals("executing")) {
            scenarioInstance.getExecutingGateways().add((GatewayInstance) controlNodeInstance);
        } else if (state.equals("terminated") || state.equals("skipped")) {
            scenarioInstance.getTerminatedControlNodeInstances().add(controlNodeInstance);
        }
        scenarioInstance.getControlNodeInstances().add(controlNodeInstance);
    }

    /**
     * Executes the gateway instance.
     * Sets the state for the gateway instance in the database to executing.
     */
    public void execute() {
        state = "executing";
        dbGatewayInstance.setState(controlNodeInstance.controlNodeInstance_id, state);
        scenarioInstance.getExecutingGateways().add((GatewayInstance) controlNodeInstance);
    }

    /**
     * Terminates the gateway instance.
     * Sets the state for the gateway instance in the database to terminated.
     */
    @Override
    public boolean terminate() {
        state = "terminated";
        dbGatewayInstance.setState(controlNodeInstance.controlNodeInstance_id, state);
        scenarioInstance.getExecutingGateways().remove(controlNodeInstance);
        scenarioInstance.getTerminatedControlNodeInstances().add(controlNodeInstance);
        return true;
    }

    /**
     * Skips the gateway instance.
     * Sets the state for the gateway instance in the database to skipped.
     */
    @Override
    public boolean skip() {
        state = "skipped";
        dbGatewayInstance.setState(controlNodeInstance.controlNodeInstance_id, state);
        scenarioInstance.getExecutingGateways().remove(controlNodeInstance);
        scenarioInstance.getTerminatedControlNodeInstances().add(controlNodeInstance);
        return true;
    }
}
