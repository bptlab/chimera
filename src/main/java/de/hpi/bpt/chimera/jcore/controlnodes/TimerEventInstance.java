package de.hpi.bpt.chimera.jcore.controlnodes;

import de.hpi.bpt.chimera.database.controlnodes.events.DbTimerEvent;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.jcore.executionbehaviors.TimeCalculator;

import java.util.Date;

/**
 * Represent timer events. Currently only time spans are supported (e.g. one day).
 */
public class TimerEventInstance extends AbstractEvent {

	public TimerEventInstance(int controlNodeId, int fragmentInstanceId, ScenarioInstance scenarioInstance) {
		super(controlNodeId, fragmentInstanceId, scenarioInstance);
	}

	public TimerEventInstance(int controlNodeId, int fragmentInstanceId, ScenarioInstance scenarioInstance, int controlNodeInstanceId) {
		super(controlNodeId, fragmentInstanceId, scenarioInstance, controlNodeInstanceId);
	}

	@Override
	public String getType() {
		return "TimerEvent";
	}

	@Override
	public void enableControlFlow() {
		if (this.getState().equals(State.INIT)) {
			this.setState(State.REGISTERED);
			this.registerEvent();
		}
	}

	/**
	 * Since timer events are not registered at a Event platform, but are handled internally,
	 * calls specific method for timer events.
	 */
	public void registerEvent() {
		EventDispatcher.registerTimerEvent(this, this.getFragmentInstanceId(), this.getScenarioInstance().getId(), this.getScenarioInstance().getScenarioId());
	}

	/**
	 * Calculate the termination of the timer from the current time and the
	 * specified time span.
	 *
	 * @return the Date when the timer should be triggered.
	 */
	public Date getTerminationDate() {
		DbTimerEvent dbTimerEvent = new DbTimerEvent();
		String timerDefinition = dbTimerEvent.retrieveTimerDefinition(this.getControlNodeId());
		TimeCalculator calculator = new TimeCalculator();
		Date now = new Date();
		return calculator.getDatePlusInterval(now, timerDefinition);
	}
}
