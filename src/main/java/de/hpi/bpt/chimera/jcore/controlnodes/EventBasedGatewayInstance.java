package de.hpi.bpt.chimera.jcore.controlnodes;

import de.hpi.bpt.chimera.database.DbControlFlow;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Representation for event based gateways.
 */
public class EventBasedGatewayInstance extends GatewayInstance {

    public EventBasedGatewayInstance(int controlNodeId, int fragmentInstanceId,
                                     ScenarioInstance scenarioInstance) {
        super(controlNodeId, fragmentInstanceId, scenarioInstance);
        this.type = GatewayType.EVENT_BASED;
        this.setControlNodeInstanceId(dbControlNodeInstance
                .createNewControlNodeInstance(controlNodeId, "EventBasedGateway", fragmentInstanceId));
        this.dbGatewayInstance.createNewGatewayInstance(
                getControlNodeInstanceId(), "EventBasedGateway", "init");
    }

    public EventBasedGatewayInstance(int controlNodeId, int fragmentInstanceId,
                                     ScenarioInstance scenarioInstance, int instanceId) {
        super(controlNodeId, fragmentInstanceId,scenarioInstance, instanceId);
        this.type = GatewayType.EVENT_BASED;
    }

    /**
     * Since event based gateways can not have data preconditions and the users should not
     * start them themselves, their enabling results in the immediate termination
     */
    @Override
    public void enableControlFlow() {
        this.terminate();
    }

    /**
     * Terminating an EventBasedGatewayInstance will register all following event control nodes
     * and save that they are alternative.
     *
     * @return true
     */
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
        return true;
    }
}
