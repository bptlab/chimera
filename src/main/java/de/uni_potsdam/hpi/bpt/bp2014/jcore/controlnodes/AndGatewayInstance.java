package de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors.GatewayStateMachine;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.ParallelGatewayJoinBehavior;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.ParallelGatewaySplitBehavior;

/**
 *
 */
public class AndGatewayInstance extends GatewayInstance {

    public AndGatewayInstance(int controlNodeId, int fragmentInstanceId, ScenarioInstance scenarioInstance) {
        super(controlNodeId, fragmentInstanceId, scenarioInstance);
        this.type = GatewayType.AND;
        this.setControlNodeInstanceId(dbControlNodeInstance
                .createNewControlNodeInstance(controlNodeId, "AND", fragmentInstanceId));
        this.dbGatewayInstance.createNewGatewayInstance(
                getControlNodeInstanceId(), "AND", "init");
        this.setStateMachine(new GatewayStateMachine(this.getControlNodeId(),
                this.scenarioInstance, this));
        this.initGatewayInstance();
    }

    public AndGatewayInstance(int controlNodeId, int fragmentInstanceId,
                                     ScenarioInstance scenarioInstance, int instanceId) {
        super(controlNodeId, fragmentInstanceId,scenarioInstance, instanceId);
        this.type = GatewayType.EVENT_BASED;
        this.initGatewayInstance();
    }

    private void initGatewayInstance() {
        this.setOutgoingBehavior(new ParallelGatewaySplitBehavior(
                getControlNodeId(), scenarioInstance,
                getFragmentInstanceId(), this));
        this.setIncomingBehavior(new ParallelGatewayJoinBehavior(
                this, scenarioInstance));
    }
}
