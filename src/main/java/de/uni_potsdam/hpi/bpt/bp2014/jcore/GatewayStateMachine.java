package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbGatewayInstance;

/**
 * Created by jaspar.mang on 19.01.15.
 */
public class GatewayStateMachine extends StateMachine {
    private DbGatewayInstance dbGatewayInstance = new DbGatewayInstance();
    public GatewayStateMachine(int gateway_id, ScenarioInstance scenarioInstance, ControlNodeInstance controlNodeInstance){
        this.scenarioInstance = scenarioInstance;
        this.controlNodeInstance_id = gateway_id;
        this.controlNodeInstance = controlNodeInstance;
        this.state = dbGatewayInstance.getState(controlNodeInstance.controlNodeInstance_id);
    }
    public void terminate(){
        state = "terminated";
        dbGatewayInstance.setState(controlNodeInstance.controlNodeInstance_id, state);
    }
}
