package de.uni_potsdam.hpi.bpt.bp2014.jcore;


public class ParallelGatewaySplitBehavior extends ParallelOutgoingBehavior {
    GatewayInstance gatewayInstance;

    /**
     * Initializes the ParallelGatewaySplitBehavior.
     *
     * @param gateway_id          This is the database id from the gateway.
     * @param scenarioInstance    This is an instance from the class ScenarioInstance.
     * @param fragmentInstance_id This is the database id from the fragment instance.
     * @param gatewayInstance     This is an instance from the class GatewayInstance.
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
            if (controlNode_id == controlNodeInstance.controlNode_id && !controlNodeInstance.getClass().equals(ActivityInstance.class) && !controlNodeInstance.getStateMachine().state.equals("terminated")) {
                return controlNodeInstance;
            }
        }
        String type = dbControlNode.getType(controlNode_id);
        ControlNodeInstance controlNodeInstance = createControlNode(type, controlNode_id);
        this.setAutomaticExecutionToFalse(type, controlNodeInstance);
        return controlNodeInstance;
    }

    /**
     * @param type
     * @param controlNodeInstance
     */
    private void setAutomaticExecutionToFalse(String type, ControlNodeInstance controlNodeInstance) {
        switch (type) {
            case "Activity":
            case "EmailTask":
            case "WebServiceTask":
                if (!gatewayInstance.isAutomaticExecution()) {
                    ((ActivityInstance) controlNodeInstance).setAutomaticExecution(false);
                }
                break;
            case "AND":
                if (!gatewayInstance.isAutomaticExecution()) {
                    ((GatewayInstance) controlNodeInstance).setAutomaticExecution(false);
                }
                break;
        }
    }
}
