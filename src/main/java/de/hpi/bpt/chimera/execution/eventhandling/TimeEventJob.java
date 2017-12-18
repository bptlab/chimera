package de.hpi.bpt.chimera.execution.eventhandling;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.execution.event.TimerEventInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractEvent;
import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 */
public class TimeEventJob implements Job {
	private final static Logger logger = Logger.getLogger(TimeEventJob.class);

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		logger.info("TimerEventJob executing");
		JobDataMap data = jobExecutionContext.getJobDetail().getJobDataMap();
		String caseModelId = data.getString("CaseModelId");
		String caseId = data.getString("CaseId");
		String controlNodeInstanceId = data.getString("ControlNodeInstanceId");
		logger.info(String.format("cmId: %s, caseId: %s, ControlNodeInstanceId: %s", caseModelId, caseId, controlNodeInstanceId));
		if (!ExecutionService.isExistingCase(caseId)) {
			return;
		}
		CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(caseModelId, caseId);
		TimerEventInstance timerEvent = (TimerEventInstance) caseExecutioner.getControlNodeInstance(controlNodeInstanceId);
		timerEvent.terminate();
		// de.hpi.bpt.chimera.jcore.eventhandling.SseNotifier.notifyRefresh();
	}
}
