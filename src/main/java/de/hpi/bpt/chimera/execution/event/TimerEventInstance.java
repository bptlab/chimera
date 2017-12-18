package de.hpi.bpt.chimera.execution.event;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.State;
import de.hpi.bpt.chimera.execution.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.TimerEvent;

public class TimerEventInstance extends AbstractEventInstance {

	private final static Logger logger = Logger.getLogger(AbstractEventInstance.class);
	private org.quartz.JobKey jobKey = null;

	public TimerEventInstance(TimerEvent event, FragmentInstance fragmentInstance) {
		super(event, fragmentInstance);
		setState(State.INIT);
	}
	
	@Override
	public void enableControlFlow() {
		logger.info("Controlflow of an TimerEventenabled");
		if (this.getState().equals(State.INIT)) {
			this.setState(State.REGISTERED);
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
		 
		this.setState(State.SKIPPED);
	}

	/**
	 * Since timer events are not registered at a Event platform, but are
	 * handled internally, calls specific method for timer events.
	 */
	public void registerEvent() {
		EventDispatcher.registerTimerEvent(this);
		logger.info("Timerevent has registered itself at EventDispatcher");
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

	public JobKey getJobKey() {
		return jobKey;
	}

	public void setJobKey(JobKey jobKey) {
		this.jobKey = jobKey;
	}

}