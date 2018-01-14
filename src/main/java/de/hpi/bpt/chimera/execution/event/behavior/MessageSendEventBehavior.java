package de.hpi.bpt.chimera.execution.event.behavior;

import de.hpi.bpt.chimera.execution.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.eventhandling.EventSpawner;

public class MessageSendEventBehavior extends AbstractEventBehavior {

	public MessageSendEventBehavior(AbstractEventInstance eventInstance) {
		super(eventInstance);
	}

	@Override
	public void begin() {
		EventSpawner.spawnEvent(getEventInstance());
		getEventInstance().begin();
	}
}
