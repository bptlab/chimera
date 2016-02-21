package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.*;

/**
 * Created by jonas on 21.02.16.
 */
public class EventBasedGatewayInstance extends GatewayInstance {

    public EventBasedGatewayInstance(int controlNodeId, int fragmentInstanceId, ScenarioInstance scenarioInstance) {
        super(controlNodeId, fragmentInstanceId, scenarioInstance);
        this.type = GatewayType.EVENT_BASED;
        this.setControlNodeInstanceId(dbControlNodeInstance
                .createNewControlNodeInstance(
                        controlNodeId, "EVENT_BASED", fragmentInstanceId));
        this.dbGatewayInstance.createNewGatewayInstance(
                getControlNodeInstanceId(), "EVENT_BASED", "init");
        this.initGatewayInstance();
    }

    public EventBasedGatewayInstance(int controlNodeId, int fragmentInstanceId,
                                     ScenarioInstance scenarioInstance, int instanceId) {
        super(controlNodeId, fragmentInstanceId,scenarioInstance, instanceId);
        this.type = GatewayType.EVENT_BASED;
        this.initGatewayInstance();
    }

    private void initGatewayInstance() {
        //TODO add event based behavior
        this.setOutgoingBehavior(new EventBasedGatewaySplitBehavior(
                getControlNodeId(), scenarioInstance,
                getFragmentInstanceId()));
        this.setIncomingBehavior(new ExclusiveGatewayJoinBehavior(
                this, scenarioInstance));
    }
}
