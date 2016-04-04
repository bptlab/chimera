package de.uni_potsdam.hpi.bpt.bp2014.jhistory;

import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbHistoryActivityTransition;
import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbHistoryDataAttributeTransition;
import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbHistoryDataObjectTransition;

import java.util.Map;

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
	public Map<Integer, Map<String, Object>> getDataObjectEntries(int scenarioInstanceId) {
		DbHistoryDataObjectTransition dbHistoryDataObjectTransition =
				new DbHistoryDataObjectTransition();
		return dbHistoryDataObjectTransition.getLogEntriesForScenarioInstance(
				scenarioInstanceId);
	}

	/**
	 * This method returns the Activity log entries for a ScenarioInstance.
	 *
	 * @param scenarioInstanceId ID of the ScenarioInstance for which the
	 *                           activity log entries shall be returned.
	 * @return a Map with a Map of the log entries' attribute names
	 * 			as keys and their respective values.
	 */
	public Map<Integer, Map<String, Object>> getActivityInstanceEntries(int scenarioInstanceId) {
		DbHistoryActivityTransition dbHistoryActivityTransition =
				new DbHistoryActivityTransition();
		return dbHistoryActivityTransition.getLogEntriesForScenarioInstance(
				scenarioInstanceId);
	}

	/**
	 * This method returns the terminated Activity log entries for a ScenarioInstance.
	 *
	 * @param scenarioInstanceId ID of the ScenarioInstance for which the
	 *                           activity log entries shall be returned.
	 * @return a Map with a Map of the log entries' attribute names
	 * 			as keys and their respective values.
	 */
	public Map<Integer, Map<String, Object>>
    getTerminatedActivities(int scenarioInstanceId) {
		DbHistoryActivityTransition dbHistoryActivityTransition = new DbHistoryActivityTransition();
		return dbHistoryActivityTransition
                .getterminatedLogEntriesForScenarioInstance(scenarioInstanceId);
	}

	/**
	 * This method returns the DataAttributeInstance log entries for a ScenarioInstance.
	 *
	 * @param scenarioInstanceId ID of the ScenarioInstance for which the
	 *                           DataAttributeInstance log entries shall be returned.
	 * @return a Map with a Map of the log entries' attribute names
	 * 			as keys and their respective values.
	 */
	public Map<Integer, Map<String, Object>> getDataattributeEntries(
			int scenarioInstanceId) {
		DbHistoryDataAttributeTransition dbHistoryDataAttributeInstance =
				new DbHistoryDataAttributeTransition();
		return dbHistoryDataAttributeInstance.getLogEntriesForScenarioInstance(
				scenarioInstanceId);
	}
}
