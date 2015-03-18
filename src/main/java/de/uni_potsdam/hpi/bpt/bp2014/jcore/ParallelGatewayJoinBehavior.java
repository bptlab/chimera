package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;

import java.util.LinkedList;

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
            ((GatewayInstance) controlNodeInstance).terminate();
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
