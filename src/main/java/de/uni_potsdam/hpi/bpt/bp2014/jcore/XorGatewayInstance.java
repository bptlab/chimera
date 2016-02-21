package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.ExclusiveGatewayJoinBehavior;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.ExclusiveGatewaySplitBehavior;

/**
 * Created by jonas on 21.02.16.
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
                this, scenarioInstance, getStateMachine()));
    }
}
