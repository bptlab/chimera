package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlFlow;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class EventBasedGatewayInstance extends GatewayInstance {

    public EventBasedGatewayInstance(int controlNodeId, int fragmentInstanceId, ScenarioInstance scenarioInstance) {
        super(controlNodeId, fragmentInstanceId, scenarioInstance);
        this.type = GatewayType.EVENT_BASED;
        this.setControlNodeInstanceId(dbControlNodeInstance
                .createNewControlNodeInstance(controlNodeId, "EVENT_BASED", fragmentInstanceId));
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
        this.setOutgoingBehavior(new EventBasedGatewaySplitBehavior(
                getControlNodeId(), scenarioInstance,
                getFragmentInstanceId()));
        }

    @Override
    public void enableControlFlow() {
        this.terminate();
    }

    @Override
    public boolean terminate() {
        DbControlFlow controlFlow = new DbControlFlow();
        List<Integer> followingControlNodes =
                controlFlow.getFollowingControlNodes(this.getControlNodeId());
        EventFactory factory = new EventFactory(this.scenarioInstance);
        List<AbstractEvent> followingEvents = followingControlNodes.stream().map(
                x -> factory.getEventForControlNodeId(x, this.getFragmentInstanceId()))
                .collect(Collectors.toList());
        EventDispatcher.registerExclusiveEvents(followingEvents);
        followingEvents.forEach(AbstractEvent::enableControlFlow);
        return true;
    }
}
