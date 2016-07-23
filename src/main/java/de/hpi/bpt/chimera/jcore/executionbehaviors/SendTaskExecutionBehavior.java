package de.hpi.bpt.chimera.jcore.executionbehaviors;

import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.eventhandling.EventSpawner;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractControlNodeInstance;

public class SendTaskExecutionBehavior extends TaskExecutionBehavior {

    private EventSpawner eventSpawner;

    /**
     * Initializes.
     *
     * @param activityInstanceId  Id from the activity instance in the database.
     * @param scenarioInstance    This is an instance from the class ScenarioInstance.
     * @param controlNodeInstance This is an AbstractControlNodeInstance.
     */
    public SendTaskExecutionBehavior(int activityInstanceId, ScenarioInstance scenarioInstance,
                                     AbstractControlNodeInstance controlNodeInstance) {
        super(activityInstanceId, scenarioInstance, controlNodeInstance);
        eventSpawner = new EventSpawner(scenarioInstance);
    }

    @Override
    public void execute() {
        eventSpawner.spawnEvent(getControlNodeInstance().getControlNodeId());
        this.setCanTerminate(true);
    }
}
