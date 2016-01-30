package de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;

/**
 *
 */
public class EventOutgoingBehavior extends AbstractParallelOutgoingBehavior {

    public EventOutgoingBehavior(int controlNodeId, ScenarioInstance scenarioInstance,
                                 int fragmentInstanceId) {
        this.setControlNodeId(controlNodeId);
        this.setFragmentInstanceId(fragmentInstanceId);
        this.setScenarioInstance(scenarioInstance);
    }

    @Override public void terminate() {
        this.checkAfterTermination();
        this.enableFollowing();
        this.runAfterTermination();
    }
}
