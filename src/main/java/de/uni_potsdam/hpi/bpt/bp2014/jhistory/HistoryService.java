package de.uni_potsdam.hpi.bpt.bp2014.jhistory;

import de.uni_potsdam.hpi.bpt.bp2014.database.history.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Access point for logs of a specific Scenario Instance.
 */
public class HistoryService {
	/**
	 * Database Connection objects
	 */

	/**
	 * This method returns the DataObjectInstances log entries for a ScenarioInstance.
	 *
	 * @param scenarioInstanceId ID of the ScenarioInstance for which the
	 *                           DataObjectInstance log entries shall be returned.
	 * @return a Map with a Map of the log entries' attribute names
	 * 			as keys and their respective values.
	 */
	public List<LogEntry> getDataObjectEntries(int scenarioInstanceId) {
		return new DbLogEntry().getLogEntriesForScenarioInstance(
                scenarioInstanceId, LogEntry.LogType.DATA_OBJECT);
	}

	/**
	 * This method returns the Activity log entries for a ScenarioInstance.
	 *
	 * @param scenarioInstanceId ID of the ScenarioInstance for which the
	 *                           activity log entries shall be returned.
	 * @return a Map with a Map of the log entries' attribute names
	 * 			as keys and their respective values.
	 */
	public List<LogEntry> getActivityInstanceEntries(int scenarioInstanceId) {
		return new DbLogEntry().getLogEntriesForScenarioInstance(
                scenarioInstanceId, LogEntry.LogType.ACTIVITY);
	}

    public Map<Integer, Map<String, Object>> getEventEntries(int scenarioInstanceId) {
        return new DbHistoryEvent().getLogEntries(scenarioInstanceId);
    }

	/**
	 * This method returns the terminated Activity log entries for a ScenarioInstance.
	 *
	 * @param scenarioInstanceId ID of the ScenarioInstance for which the
	 *                           activity log entries shall be returned.
	 * @return a Map with a Map of the log entries' attribute names
	 * 			as keys and their respective values.
	 */
	public List<LogEntry> getTerminatedActivities(int scenarioInstanceId) {
		List<LogEntry> logEntries = new DbLogEntry().getLogEntriesForScenarioInstance(
                scenarioInstanceId, LogEntry.LogType.ACTIVITY);
        return logEntries.stream().filter(
                x -> "terminated".equals(x.getNewValue())).collect(Collectors.toList());
	}

	/**
	 * This method returns the DataAttributeInstance log entries for a ScenarioInstance.
	 *
	 * @param scenarioInstanceId ID of the ScenarioInstance for which the
	 *                           DataAttributeInstance log entries shall be returned.
	 * @return a.
	 */
	public List<LogEntry> getDataattributeEntries(int scenarioInstanceId) {
		return new DbLogEntry().getLogEntriesForScenarioInstance(
                scenarioInstanceId, LogEntry.LogType.DATA_ATTRIBUTE);
	}
}
