package de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;
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
        EventDispatcher.unregisterEvent(
                EventDispatcher.findEvent(mappingKey, scenarioId, scenarioInstanceId));
    }
}
