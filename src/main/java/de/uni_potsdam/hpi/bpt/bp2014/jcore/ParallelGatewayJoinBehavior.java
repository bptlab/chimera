package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;

import java.util.LinkedList;


public class ParallelGatewayJoinBehavior extends IncomingBehavior {
    /**
     * Database Connection objects
     */
    private final DbControlFlow dbControlFlow = new DbControlFlow();
    private final DbControlNode dbControlNode = new DbControlNode();

    /**
     * Initializes the ParallelGatewayJoinBehavior.
     *
     * @param gatewayInstance  This is an instance from the class GatewayInstance.
     * @param scenarioInstance This is an instance from the class ScenarioInstance.
     */
    ParallelGatewayJoinBehavior(GatewayInstance gatewayInstance, ScenarioInstance scenarioInstance) {
        this.scenarioInstance = scenarioInstance;
        this.controlNodeInstance = gatewayInstance;
    }

    @Override
    public void enableControlFlow() {
        if (checkEnabled()) {
            controlNodeInstance.terminate();
        }
    }

    /**
     * Checks if all control node instances before this gateway have been terminated
     *
     * @return true if this gateway can get enabled. false if not.
     */
    private Boolean checkEnabled() {
        LinkedList<Integer> predecessors = dbControlFlow.getPredecessorControlNodes(controlNodeInstance.controlNode_id);
        //if a start Event ist before this Gateway it is enabled
        if (predecessors.size() == 1 && dbControlNode.getType(predecessors.get(0)).equals("Startevent")) {
            return true;
        }
        //looks that all predecessors are terminated
        for (int controlNode : predecessors) {
            if (!scenarioInstance.terminatedControlNodeInstancesContainControlNodeID(controlNode) && !scenarioInstance.executingGatewaysContainControlNodeID(controlNode)) {
                return false;
            }
        }
        return true;
    }
}
