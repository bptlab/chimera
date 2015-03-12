package de.uni_potsdam.hpi.bpt.bp2014.jcore;


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


public class ParallelGatewaySplitBehavior extends ParallelOutgoingBehavior {
    GatewayInstance gatewayInstance;

    /**
     * Initializes the ParallelGatewaySplitBehavior.
     *
     * @param gateway_id          This is the database id from the gateway.
     * @param scenarioInstance    This is an instance from the class ScenarioInstance.
     * @param fragmentInstance_id This is the database id from the fragment instance.
     * @param gatewayInstance    This is an instance from the class GatewayInstance.
     */
    ParallelGatewaySplitBehavior(int gateway_id, ScenarioInstance scenarioInstance, int fragmentInstance_id, GatewayInstance gatewayInstance) {
        this.controlNode_id = gateway_id;
        this.scenarioInstance = scenarioInstance;
        this.fragmentInstance_id = fragmentInstance_id;
        this.gatewayInstance = gatewayInstance;
    }

    @Override
    public void terminate() {
        this.checkAfterTermination();
        this.enableFollowing();
        this.runAfterTermination();
    }

    @Override
    protected ControlNodeInstance createFollowingNodeInstance(int controlNode_id) {
        for (ControlNodeInstance controlNodeInstance : scenarioInstance.getControlNodeInstances()) {
            if (controlNode_id == controlNodeInstance.controlNode_id) {
                return controlNodeInstance;
            }
        }
        String type = dbControlNode.getType(controlNode_id);
        ControlNodeInstance controlNodeInstance = null;
        //TODO type
        switch (type) {
            case "Activity":
            case "EmailTask":
                controlNodeInstance = new ActivityInstance(controlNode_id, fragmentInstance_id, scenarioInstance);
                if (!gatewayInstance.isAutomaticExecution()){
                    ((ActivityInstance)controlNodeInstance).setAutomaticExecution(false);
                }
                break;
            case "Endevent":
                controlNodeInstance = new EventInstance(fragmentInstance_id, scenarioInstance, "Endevent");
                break;
            case "XOR":
                controlNodeInstance = new GatewayInstance(controlNode_id, fragmentInstance_id, scenarioInstance);
                break;
            case "AND":
                controlNodeInstance = new GatewayInstance(controlNode_id, fragmentInstance_id, scenarioInstance);
                if (!gatewayInstance.isAutomaticExecution()){
                    ((GatewayInstance)controlNodeInstance).setAutomaticExecution(false);
                }
                break;
        }
        return controlNodeInstance;
    }
}
