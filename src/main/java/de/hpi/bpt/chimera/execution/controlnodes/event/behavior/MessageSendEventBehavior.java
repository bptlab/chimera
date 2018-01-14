package de.hpi.bpt.chimera.execution.controlnodes.event.behavior;

import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling.EventSpawner;

public class MessageSendEventBehavior extends AbstractEventBehavior {

	public MessageSendEventBehavior(AbstractEventInstance eventInstance) {
		super(eventInstance);
	}

	@Override
	public void begin() {
		if (getEventInstance().getState().equals(State.READY)) {
			return;
		}
		EventSpawner.spawnEvent(getEventInstance());
		getEventInstance().terminate();
	}
}
