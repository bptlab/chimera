package de.hpi.bpt.chimera.jcore.flowbehaviors;

import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.eventhandling.EventSpawner;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractControlNodeInstance;

/**
 * Represents the incoming behavior of end events.
 */
public class EndEventIncomingBehavior extends AbstractIncomingBehavior {
    private final boolean isSendEvent;
    private EventSpawner eventSpawner;

    /**
     * Creates and initializes an event incoming control flow behavior.
     * This behavior is only for an end event.
     *
     * @param controlNodeInstance This is an instance of the class AbstractControlNodeInstance.
     * @param scenarioInstance    This is an instance of the class ScenarioInstance.
     */
    public EndEventIncomingBehavior(AbstractControlNodeInstance controlNodeInstance,
                                               ScenarioInstance scenarioInstance,
                                               boolean isSendEvent) {
        this.setControlNodeInstance(controlNodeInstance);
        this.setScenarioInstance(scenarioInstance);
        this.isSendEvent = isSendEvent;
        if (isSendEvent) {
            this.eventSpawner = new EventSpawner(scenarioInstance);
        }
    }

    @Override
    public void enableControlFlow() {
        if (isSendEvent) {
            eventSpawner.spawnEvent(getControlNodeInstance().getControlNodeId());
        }
        getScenarioInstance().restartFragment(this.getControlNodeInstance()
                .getFragmentInstanceId());
    }
}
