package de.hpi.bpt.chimera.execution.controlnodes.event.behavior;

import javax.persistence.Entity;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling.EventSpawner;

@Entity
public class MessageSendEventBehavior extends EventBehavior {
	private static final Logger logger = Logger.getLogger(MessageSendEventBehavior.class);

	/**
	 * for JPA only
	 */
	public MessageSendEventBehavior() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}

	@Override
	public void enableControlFlow() {
		super.enableControlFlow();
	}
	public MessageSendEventBehavior(AbstractEventInstance eventInstance) {
		super(eventInstance);
	}

	@Override
	public void begin() {
		if (!getEventInstance().getState().equals(State.READY)) {
			logger.info("Can't begin ThrowEvent, not in State ready.");
			return;
		}
		EventSpawner.spawnEvent(getEventInstance());
		getEventInstance().terminate();
	}
}
