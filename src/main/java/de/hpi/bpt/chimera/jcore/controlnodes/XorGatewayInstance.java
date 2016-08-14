package de.hpi.bpt.chimera.jcore.controlnodes;

import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.executionbehaviors.GatewayStateMachine;
import de.hpi.bpt.chimera.jcore.flowbehaviors.ExclusiveGatewayJoinBehavior;
import de.hpi.bpt.chimera.jcore.flowbehaviors.ExclusiveGatewaySplitBehavior;

/**
 * Represents exclusive gateways.
 * In contrast to the BPMN specification, exclusive gateways are handled like event
 * based gateways. All following control nodes are enabled, and skipped when one of them
 * begins.
 */
public class XorGatewayInstance extends GatewayInstance {

    public XorGatewayInstance(int controlNodeId, int fragmentInstanceId, ScenarioInstance scenarioInstance) {
        super(controlNodeId, fragmentInstanceId, scenarioInstance);
        this.type = GatewayType.XOR;
        this.setControlNodeInstanceId(dbControlNodeInstance
                .createNewControlNodeInstance(
                        controlNodeId, "XOR", fragmentInstanceId, State.INIT));
        this.dbGatewayInstance.createNewGatewayInstance(
                getControlNodeInstanceId(), "XOR", "init");
        this.initGatewayInstance();
    }

    public XorGatewayInstance(int controlNodeId, int fragmentInstanceId,
                                     ScenarioInstance scenarioInstance, int instanceId) {
        super(controlNodeId, fragmentInstanceId,scenarioInstance, instanceId);
        this.type = GatewayType.XOR;
        this.initGatewayInstance();
    }

    @Override
    /**
     * Do not set state to terminated yet, since there is still influence on the
     * execution until one of the following control nodes is activated.
     */
    public void terminate() {
        getOutgoingBehavior().terminate();
    }

    private void initGatewayInstance() {
        this.setOutgoingBehavior(new ExclusiveGatewaySplitBehavior(
                getControlNodeId(), scenarioInstance,
                getFragmentInstanceId()));
        this.setIncomingBehavior(new ExclusiveGatewayJoinBehavior(
                this, scenarioInstance));
    }

    public boolean containsControlNodeInFollowing(int controlNodeId) {
        ExclusiveGatewaySplitBehavior outgoing = (ExclusiveGatewaySplitBehavior)
                this.getOutgoingBehavior();
        return outgoing.containsControlNodeInFollowing(controlNodeId);
    }

    public void skipAlternativeBranches(int controlNodeId) {
        ExclusiveGatewaySplitBehavior outgoing = (ExclusiveGatewaySplitBehavior)
                this.getOutgoingBehavior();
        outgoing.skipAlternativeBranches(controlNodeId);
        this.setState(State.TERMINATED);
        dbGatewayInstance.setState(this.getControlNodeInstanceId(), getState().toString());
    }
}
