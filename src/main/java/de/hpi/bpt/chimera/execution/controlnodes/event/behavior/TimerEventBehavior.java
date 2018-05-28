package de.hpi.bpt.chimera.execution.controlnodes.event.behavior;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.apache.log4j.Logger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.behavior.TimerDefinition;

@Entity
public class TimerEventBehavior extends EventBehavior {
	private static final Logger log = Logger.getLogger(AbstractEventInstance.class);
	// TODO how to persist this?
	private org.quartz.JobKey jobKey = null;


	/**
	 * for JPA only
	 */
	public TimerEventBehavior() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}


	public TimerEventBehavior(AbstractEventInstance eventInstance) {
		super(eventInstance);
		eventInstance.setState(State.INIT);
	}
	
	/**
	 * Since timer events are not registered at a Event platform, but are
	 * handled internally, calls specific method for timer events.
	 */
	@Override
	public void begin() {
		log.info("Controlflow of an TimerEvent enabled");
		EventDispatcher.registerTimerEvent(this);
		log.info("Timerevent has registered itself at EventDispatcher");
		getEventInstance().setState(State.REGISTERED);
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
				log.warn(e.getMessage());
			}
			log.info("Timerevent skipped");
		}
		 
		getEventInstance().setState(State.SKIPPED);
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
		// TODO replace P10S with the TimerDefinition
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
