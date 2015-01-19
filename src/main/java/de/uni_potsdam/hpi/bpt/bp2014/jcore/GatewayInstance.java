package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNodeInstance;

public class GatewayInstance extends ControlNodeInstance {
    public Boolean isOR;
    public Boolean isAND;
    public ScenarioInstance scenarioInstance;
    private DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();

    public GatewayInstance(int controlNode_id, int fragmentInstance_id, ScenarioInstance scenarioInstance){
        this.scenarioInstance = scenarioInstance;
        this.controlNode_id = controlNode_id;
        this.fragmentInstance_id = fragmentInstance_id;
        scenarioInstance.controlNodeInstances.add(this);
        if(dbControlNodeInstance.existControlNodeInstance(controlNode_id, fragmentInstance_id)) {
        }else{

        }
        this.outgoingBehavior = new ParallelGatewaySplitBehavior();
        this.incomingBehavior = new ParallelGatewayJoinBehavior(this);

        
    }
}
