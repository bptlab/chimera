package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbGatewayInstance;

public class GatewayInstance extends ControlNodeInstance {
    public Boolean isXOR;
    public Boolean isAND;
    public ScenarioInstance scenarioInstance;
    private DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
    private DbControlNode dbControlNode = new DbControlNode();
    private DbGatewayInstance dbGatewayInstance = new DbGatewayInstance();

    public GatewayInstance(int controlNode_id, int fragmentInstance_id, ScenarioInstance scenarioInstance) {
        //looks if the Gateway Instance has already been initialized
        for (ControlNodeInstance controlNodeInstance : scenarioInstance.controlNodeInstances) {
            if (controlNodeInstance.fragmentInstance_id == controlNodeInstance_id && controlNodeInstance.controlNode_id == controlNode_id) {
                //if it exist, only checks the control flow
                controlNodeInstance.incomingBehavior.enableControlFlow();
                return;
            }
        }
        this.scenarioInstance = scenarioInstance;
        this.controlNode_id = controlNode_id;
        this.fragmentInstance_id = fragmentInstance_id;
        scenarioInstance.controlNodeInstances.add(this);
        if (dbControlNodeInstance.existControlNodeInstance(controlNode_id, fragmentInstance_id)) {
            //initializes all Gateway Instances in the database
            controlNodeInstance_id = dbControlNodeInstance.getControlNodeInstanceID(controlNode_id, fragmentInstance_id);
        } else {
            //creates a new Gateway Instance also in database
            if (isAND) {
                dbControlNodeInstance.createNewControlNodeInstance(controlNode_id, "AND", fragmentInstance_id);
            }//TODO: XOR Here
            controlNodeInstance_id = dbControlNodeInstance.getControlNodeInstanceID(controlNode_id, fragmentInstance_id);
            if (isAND) {
                dbGatewayInstance.createNewGatewayInstance(controlNodeInstance_id, "AND", "init");
            }//TODO: XOR Here
        }
        this.stateMachine = new GatewayStateMachine(controlNode_id, scenarioInstance, this);
        if (dbControlNode.getType(controlNode_id).equals("AND")) {
            this.isAND = true;
            this.isXOR = false;
            this.outgoingBehavior = new ParallelGatewaySplitBehavior(controlNode_id, scenarioInstance, fragmentInstance_id);
            this.incomingBehavior = new ParallelGatewayJoinBehavior(this, scenarioInstance);
        }//TODO: XOR Here
    }

    public void terminate(){
        ((GatewayStateMachine)stateMachine).terminate();
        outgoingBehavior.terminate();
    }
}
