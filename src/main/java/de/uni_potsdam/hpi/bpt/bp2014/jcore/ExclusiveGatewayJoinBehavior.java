package de.uni_potsdam.hpi.bpt.bp2014.jcore;


import java.util.Collection;


public class ExclusiveGatewayJoinBehavior extends IncomingBehavior {

    /**
     * Initializes and creates an ExclusiveGatewayJoinBehavior
     *
     * @param gatewayInstance  An instance from the class GatewayInstance.
     * @param scenarioInstance An instance from the class ScenarioInstance.
     * @param stateMachine     An instance from the class StateMachine.
     */
    public ExclusiveGatewayJoinBehavior(GatewayInstance gatewayInstance, ScenarioInstance scenarioInstance, StateMachine stateMachine) {
        this.scenarioInstance = scenarioInstance;
        this.controlNodeInstance = gatewayInstance;
        this.stateMachine = stateMachine;
    }


    @Override
    public void enableControlFlow() {
        Collection conditions = dbControlFlow.getConditions(controlNodeInstance.getControlNode_id()).values();
        boolean condition = true;
        if (conditions.size() > 0 && !conditions.iterator().next().equals("")) {
            condition = false;
        }
        if (condition) {
            ((GatewayStateMachine) stateMachine).execute();
            ((ExclusiveGatewaySplitBehavior) controlNodeInstance.getOutgoingBehavior()).execute();
        } else {
            ((ExclusiveGatewaySplitBehavior) controlNodeInstance.getOutgoingBehavior()).evaluateConditions();
            controlNodeInstance.terminate();
        }

    }
}
