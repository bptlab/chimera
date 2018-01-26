package de.hpi.bpt.chimera.execution.controlnodes.event.behavior;

import javax.persistence.Entity;

import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling.EventSpawner;

@Entity
public class MessageSendEventBehavior extends EventBehavior {


	/**
	 * for JPA only
	 */
	public MessageSendEventBehavior() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}


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
