package de.hpi.bpt.chimera.execution.controlnodes.event.behavior;

import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling.EventSpawner;
import org.apache.log4j.Logger;

import javax.persistence.Entity;

@Entity
public class SignalSendEventBehavior extends EventBehavior {
	private static final Logger logger = Logger.getLogger(SignalSendEventBehavior.class);

	/**
	 * for JPA only
	 */
	public SignalSendEventBehavior() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}

	public SignalSendEventBehavior(AbstractEventInstance eventInstance) {
		super(eventInstance);
	}

	@Override
	public void begin() {
		if (!getEventInstance().getState().equals(State.READY)) {
			logger.info("Can't begin ThrowEvent, not in State ready.");
			return;
		}
		EventSpawner.spawnEvent(getEventInstance());
		getEventInstance().getFragmentInstance().activate();
		getEventInstance().getCaseExecutioner().terminateDataControlNodeInstance(getEventInstance());
	}
}
