package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbGatewayInstance;

/**
 * ********************************************************************************
 * <p/>
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 * <p/>
 * ******************************************************************
 * <p/>
 * Copyright © All Rights Reserved 2014 - 2015
 * <p/>
 * Please be aware of the License. You may found it in the root directory.
 * <p/>
 * **********************************************************************************
 */


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

        } else if (state.equals("terminated")) {
            scenarioInstance.getTerminatedControlNodeInstances().add(controlNodeInstance);
        }
        scenarioInstance.getControlNodeInstances().add(controlNodeInstance);
    }


    public void execute() {
        state = "executing";
        dbGatewayInstance.setState(controlNodeInstance.controlNodeInstance_id, state);
        scenarioInstance.getExecutingGateways().add((GatewayInstance)controlNodeInstance);
    }
    /**
     * Terminates the gateway instance.
     * Sets the state for the gateway instance in the database to terminated.
     */
    public void terminate() {
        state = "terminated";
        dbGatewayInstance.setState(controlNodeInstance.controlNodeInstance_id, state);
        scenarioInstance.getExecutingGateways().remove(controlNodeInstance);
        scenarioInstance.getTerminatedControlNodeInstances().add(controlNodeInstance);
    }
}
