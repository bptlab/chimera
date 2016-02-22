package de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.AbstractControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.ExclusiveGatewaySplitBehavior;

import java.util.LinkedList;

/**
 * Behavior for EventBased Gateways
 * Enable following events. Wait for first event to receive queried event. Then cancel others.
 */
public class EventBasedGatewaySplitBehavior extends ExclusiveGatewaySplitBehavior {
    /**
     * Initializes and creates an ExclusiveGatewaySplitBehavior.
     *
     * @param gatewayId          The id of the gateway.
     * @param scenarioInstance   An instance from the class ScenarioInstance.
     * @param fragmentInstanceId The id of the fragment instance.
     */
    public EventBasedGatewaySplitBehavior(int gatewayId, ScenarioInstance scenarioInstance, int fragmentInstanceId) {
        super(gatewayId, scenarioInstance, fragmentInstanceId);
    }

    @Override
    public void execute() {
        //TODO event based behaviour
        LinkedList<Integer> followingControlNodeIds = this.getDbControlFlow()
                .getFollowingControlNodes(this.getControlNodeId());
        for (int followingControlNodeId : followingControlNodeIds) {
            AbstractControlNodeInstance followingControlNodeInstance =
                    createFollowingNodeInstance(followingControlNodeId);
            //enable following instances
            followingControlNodeInstance.enableControlFlow();
        }
    }
}
