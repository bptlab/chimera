package de.hpi.bpt.chimera.jcore.executionbehaviors;

import de.hpi.bpt.chimera.jcore.controlnodes.AbstractEvent;
import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 */
public class TimeEventJob implements Job {

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		JobDataMap data = jobExecutionContext.getJobDetail().getJobDataMap();
		int scenarioId = data.getIntValue("scenarioId");
		int scenarioInstanceId = data.getIntValue("scenarioInstanceId");
		String mappingKey = data.getString("mappingKey");
		AbstractEvent timerEvent = EventDispatcher.findEvent(mappingKey, scenarioId, scenarioInstanceId);
		EventDispatcher.unregisterEvent(timerEvent);
		timerEvent.terminate();
	}
}
