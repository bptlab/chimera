package de.hpi.bpt.chimera.execution.controlnodes.event.behavior;

import de.hpi.bpt.chimera.execution.controlnodes.Behaving;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;

public class AbstractEventBehavior implements Behaving {
	private AbstractEventInstance eventInstance;

	public AbstractEventBehavior(AbstractEventInstance eventInstance) {
		setEventInstance(eventInstance);
	}

	/**
	 * Automatic begin.
	 */
	@Override
	public void enableControlFlow() {
		getEventInstance().begin();
	}

	/**
	 * Automatic termination.
	 */
	@Override
	public void begin() {
		// no special behavior
		getEventInstance().terminate();
	}

	@Override
	public void terminate() {
		// no special behavior
	}

	@Override
	public void skip() {
		// no special behavior
	}

	public AbstractEventInstance getEventInstance() {
		return eventInstance;
	}

	public void setEventInstance(AbstractEventInstance eventInstance) {
		this.eventInstance = eventInstance;
	}
}
