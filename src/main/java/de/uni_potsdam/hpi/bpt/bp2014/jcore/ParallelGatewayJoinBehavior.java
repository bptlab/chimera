package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;

import java.util.LinkedList;

public class ParallelGatewayJoinBehavior extends IncomingBehavior {
    private DbControlFlow dbControlFlow = new DbControlFlow();
    private DbControlNode dbControlNode = new DbControlNode();


    ParallelGatewayJoinBehavior(GatewayInstance gatewayInstance, ScenarioInstance scenarioInstance){
        this.scenarioInstance = scenarioInstance;
        this.controlNodeInstance = gatewayInstance;
    }
    @Override
    public void enableControlFlow(){
        if(checkEnabled()){
            ((GatewayInstance)controlNodeInstance).terminate();
        }
    }

    private Boolean checkEnabled(){
        LinkedList<Integer> predecessors = dbControlFlow.getPredecessorControlNodes(controlNodeInstance.controlNode_id);
        if(predecessors.size() == 1 && dbControlNode.getType(predecessors.get(0)).equals("Startevent")) return true;
        for(int controlNode: predecessors){
            if(!scenarioInstance.terminatedControlNodeInstancesContainControlNodeID(controlNode)){
                return false;
            }
        }
        return true;
    }
}
