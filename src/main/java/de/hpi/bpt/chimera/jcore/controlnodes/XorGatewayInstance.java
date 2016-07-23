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
                        controlNodeId, "XOR", fragmentInstanceId));
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

    private void initGatewayInstance() {
        this.setOutgoingBehavior(new ExclusiveGatewaySplitBehavior(
                getControlNodeId(), scenarioInstance,
                getFragmentInstanceId()));
        this.setIncomingBehavior(new ExclusiveGatewayJoinBehavior(
                this, scenarioInstance));
    }
}
