package de.hpi.bpt.chimera.execution.event;

import java.util.Date;

import de.hpi.bpt.chimera.database.controlnodes.events.DbTimerEvent;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.StartEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.TimerEvent;

public class TimerEventInstance extends AbstractEventInstance {

	public TimerEventInstance(AbstractEvent event, FragmentInstance fragmentInstance) {
		super(event, fragmentInstance);
		setState(State.INIT);
	}
	
	@Override
	public void enableControlFlow() {
		if (this.getState().equals(State.INIT)) {
			this.setState(State.REGISTERED);
			this.registerEvent();
		}
	}

	/**
	 * Since timer events are not registered at a Event platform, but are
	 * handled internally, calls specific method for timer events.
	 */
	public void registerEvent() {
		EventDispatcher.registerTimerEvent(this);
	}

	/**
	 * Calculate the termination of the timer from the current time and the
	 * specified time span.
	 *
	 * @return the Date when the timer should be triggered.
	 */
	public Date getTerminationDate() {
		String timerDefinition = this.getControlNode().getTimerDuration();
		TimeCalculator calculator = new TimeCalculator();
		Date now = new Date();
		// TODO replaxw P10S with the imerDefinition
		return calculator.getDatePlusInterval(now, timerDefinition);
	}

	@Override
	public TimerEvent getControlNode() {
		return (TimerEvent) super.getControlNode();
	}
}
