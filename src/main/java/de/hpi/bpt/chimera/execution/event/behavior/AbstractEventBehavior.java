package de.hpi.bpt.chimera.execution.event.behavior;

import de.hpi.bpt.chimera.execution.Behaving;
import de.hpi.bpt.chimera.execution.event.AbstractEventInstance;

public class AbstractEventBehavior implements Behaving {
	private AbstractEventInstance eventInstance;

	public AbstractEventBehavior(AbstractEventInstance eventInstance) {
		setEventInstance(eventInstance);
	}

	@Override
	public void enableControlFlow() {
		// no special behavior
	}

	@Override
	public void begin() {
		// no special behavior
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
