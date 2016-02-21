package de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.ExclusiveGatewaySplitBehavior;

/**
 * Created by jonas on 21.02.16.
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
    public void checkAfterTermination() {
        //TODO
    }

    public void runAfterTermination() {
        //TODO
    }
}
