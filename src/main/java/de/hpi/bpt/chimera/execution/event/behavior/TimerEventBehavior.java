package de.hpi.bpt.chimera.execution.event.behavior;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import de.hpi.bpt.chimera.execution.State;
import de.hpi.bpt.chimera.execution.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.behavior.TimerDefinition;

public class TimerEventBehavior extends AbstractEventBehavior {
	private static final Logger logger = Logger.getLogger(AbstractEventInstance.class);
	private org.quartz.JobKey jobKey = null;

	public TimerEventBehavior(AbstractEventInstance eventInstance) {
		super(eventInstance);
		eventInstance.setState(State.INIT);
	}
	
	@Override
	public void enableControlFlow() {
		logger.info("Controlflow of an TimerEventenabled");
		if (getEventInstance().getState().equals(State.INIT)) {
			getEventInstance().setState(State.REGISTERED);
			this.registerEvent();
		}
	}

	@Override
	public void skip() {
		super.skip();
		if (this.getJobKey() != null) { 
			Scheduler scheduler;
			try {
				scheduler = new StdSchedulerFactory().getScheduler();
				scheduler.deleteJob(jobKey);
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("Timerevent skipped");
		}
		 
		getEventInstance().setState(State.SKIPPED);
	}

	/**
	 * Since timer events are not registered at a Event platform, but are
	 * handled internally, calls specific method for timer events.
	 */
	public void registerEvent() {
		EventDispatcher.registerTimerEvent(getEventInstance(), this);
		logger.info("Timerevent has registered itself at EventDispatcher");
	}

	/**
	 * Calculate the termination of the timer from the current time and the
	 * specified time span.
	 *
	 * @return the Date when the timer should be triggered.
	 */
	public Date getTerminationDate() {
		String timerDefinition = getTimerDefinition().getTimerDuration();
		TimeCalculator calculator = new TimeCalculator();
		Date now = new Date();
		// TODO replaxw P10S with the imerDefinition
		return calculator.getDatePlusInterval(now, timerDefinition);
	}

	public JobKey getJobKey() {
		return jobKey;
	}

	public void setJobKey(JobKey jobKey) {
		this.jobKey = jobKey;
	}

	public TimerDefinition getTimerDefinition() {
		return (TimerDefinition) getEventInstance().getControlNode().getSpecialEventDefinition();
	}
}
