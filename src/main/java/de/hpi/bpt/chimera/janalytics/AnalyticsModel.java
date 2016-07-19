package de.hpi.bpt.chimera.janalytics;

import java.util.ArrayList;
import java.util.Map;

/**
 * This class represents the Model within the analytics framework.
 */
public class AnalyticsModel extends MetaAnalyticsModel {

	/**
	 * @param scenarioInstanceId The ID of the scenario instance.
	 * @return an ArrayList of Maps with Maps containing log information about the scenario.
	 */
	public static ArrayList<Map<Integer, Map<String, Object>>> exampleAlgorithm1(
			int scenarioInstanceId) {
		ArrayList<Map<Integer, Map<String, Object>>> result = new ArrayList<>();

		Map<Integer, Map<String, Object>> activityLog =
				getLogEntriesForScenarioInstanceWithinActivity(scenarioInstanceId);
		Map<Integer, Map<String, Object>> dataAttributeLog =
				getLogEntriesForScenarioInstanceWithinDataAttribute(
						scenarioInstanceId);
		Map<Integer, Map<String, Object>> dataObjectLog =
				getLogEntriesForScenarioInstanceWithinDataObject(
						scenarioInstanceId);
		result.add(activityLog);
		result.add(dataAttributeLog);
		result.add(dataObjectLog);

		return result;
	}

	/**
	 * @param scenarioInstanceId The ID of the scenario instance.
	 * @return an ArrayList of Maps with Maps containing timestamps from the scenario.
	 */
	public static ArrayList<Map<
			Integer, Map<String, Object>>> calculateScenarioInstanceRunTime(
			int scenarioInstanceId) {
		ArrayList<Map<Integer, Map<String, Object>>> result = new ArrayList<>();

		Map<Integer, Map<String, Object>> timestampsForScenarioInstance =
				getLogTimestampsForScenarioInstance(
				scenarioInstanceId);
		result.add(timestampsForScenarioInstance);

		return result;
	}

}
