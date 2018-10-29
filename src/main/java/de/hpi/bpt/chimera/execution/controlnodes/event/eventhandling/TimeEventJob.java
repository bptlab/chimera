package de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 */
public class TimeEventJob implements Job {
	private static final Logger logger = Logger.getLogger(TimeEventJob.class);

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		logger.info("TimerEventJob executing");
		JobDataMap data = jobExecutionContext.getJobDetail().getJobDataMap();
		String caseModelId = data.getString("CaseModelId");
		String caseId = data.getString("CaseId");
		String controlNodeInstanceId = data.getString("ControlNodeInstanceId");
		if (!ExecutionService.isExistingCase(caseId)) {
			return;
		}
		CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(caseModelId, caseId);
		AbstractEventInstance timerEvent = (AbstractEventInstance) caseExecutioner.getControlNodeInstance(controlNodeInstanceId);
		
		caseExecutioner.terminateDataControlNodeInstance(timerEvent);
		SseNotifier.notifyRefresh();
	}
}
