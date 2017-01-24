package de.hpi.bpt.chimera.jcore.executionbehaviors;

import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import de.hpi.bpt.chimera.jcore.eventhandling.EventSpawner;

public class SendTaskExecutionBehavior extends ActivityExecutionBehavior {

	private EventSpawner eventSpawner;

	public SendTaskExecutionBehavior(ActivityInstance activityInstance) {
		super(activityInstance);
		eventSpawner = new EventSpawner(getScenarioInstance());
	}

	@Override
	public void begin() {
		eventSpawner.spawnEvent(activityInstance.getControlNodeId());
	}
}
