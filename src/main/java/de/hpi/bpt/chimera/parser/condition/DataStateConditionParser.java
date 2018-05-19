package de.hpi.bpt.chimera.parser.condition;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.TerminationCondition;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.FragmentPreCondition;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.parser.CaseModelParserHelper;
import de.hpi.bpt.chimera.parser.IllegalCaseModelException;

public class DataStateConditionParser {
	private static final Logger log = Logger.getLogger((DataStateConditionParser.class).getName());

	private DataStateConditionParser() {
	}

	/**
	 * Parse a {@link TerminationCondition} out of jsonArray with help of a
	 * given {@link CaseModelParserHelper}.
	 * 
	 * @param jsonArray
	 *            - JSONArray that contains the condition sets as strings
	 * @param parserHelper
	 *            - CaseModelParserHelper that contains information about the
	 *            CaseModel the condition sets are in
	 * @return the parsed termination precondition
	 */
	public static TerminationCondition parseTerminationCondition(JSONArray jsonArray, CaseModelParserHelper parserHelper) {
		TerminationCondition terminationCondition = new TerminationCondition();

		try {
			List<ConditionSet> conditionSets = parseConditionSets(jsonArray, parserHelper);
			terminationCondition.setConditionSets(conditionSets);
		} catch (JSONException | IllegalArgumentException e) {
			log.error(e);
			throw new IllegalCaseModelException("Invalid TerminationCondition - " + e.getMessage());
		}

		return terminationCondition;
	}

	/**
	 * Parse a {@link FragmentPreCondition} out of jsonArray with help of a
	 * given {@link CaseModelParserHelper}.
	 * 
	 * @param jsonArray
	 *            - JSONArray that contains the condition sets as strings
	 * @param parserHelper
	 *            - CaseModelParserHelper that contains information about the
	 *            CaseModel the condition sets are in
	 * @return the parsed fragment precondition
	 */
	public static FragmentPreCondition parseFragmentPreCondition(JSONArray jsonArray, CaseModelParserHelper parserHelper) {
		FragmentPreCondition fragmentPreCondition = new FragmentPreCondition();

		try {
			List<ConditionSet> conditionSets = parseConditionSets(jsonArray, parserHelper);
			fragmentPreCondition.setConditionSets(conditionSets);
		} catch (JSONException | IllegalArgumentException e) {
			log.error(e);
			throw new IllegalCaseModelException("Invalid FragmentPreCondition - " + e.getMessage());
		}

		return fragmentPreCondition;
	}

	/**
	 * Parse the {@link ConditionSet}s for a DataStateCondition out of an json
	 * array.
	 * 
	 * @param jsonArray
	 *            - JSONArray that contains the condition sets as strings
	 * @param parserHelper
	 *            - CaseModelParserHelper that contains information about the
	 *            CaseModel the condition sets are in
	 * @return List of parsed condition sets
	 */
	private static List<ConditionSet> parseConditionSets(JSONArray jsonArray, CaseModelParserHelper parserHelper) {
		List<ConditionSet> conditionSets = new ArrayList<>();
		int arraySize = jsonArray.length();
		for (int i = 0; i < arraySize; i++) {
			try {
				String conditionSetString = jsonArray.getString(i);
				conditionSets.add(parseConditionSet(conditionSetString, parserHelper));
			} catch (JSONException | IllegalArgumentException e) {
				throw e;
			}
		}
		return conditionSets;
	}

	/**
	 * Divide a condition set as string into single
	 * {@link AtomicDataStateCondition} and make a {@link ConditionSet} out of
	 * them. Therefore use CaseModelParserHelper to get DataClass and
	 * ObjectLifecycleState.
	 * 
	 * @param conditionSetString
	 *            - the condition set as a string
	 * @param parserHelper
	 *            - CaseModelParserHelper that contains information about the
	 *            CaseModel the condition set is in
	 * @return the parsed ConditionSet
	 */
	private static ConditionSet parseConditionSet(String conditionSetString, CaseModelParserHelper parserHelper) {
		ConditionSet conditionComponent = new ConditionSet();

		for (String component : conditionSetString.split(",")) {
			Pattern pattern = Pattern.compile("\\s?(.*)\\[(.*?)\\]");
			Matcher match = pattern.matcher(component);
			if (!match.find()) {
				throw new IllegalCaseModelException(String.format("%s is invalid termination String", component));
			}
			String dataClassName = match.group(1);
			String objectLifecycleStateName = match.group(2);

			AtomicDataStateCondition condition = new AtomicDataStateCondition();
			
			DataClass dataClass = parserHelper.getDataClassByName(dataClassName);
			condition.setDataClass(dataClass);
			
			ObjectLifecycleState objectLifecycleState = parserHelper.getObjectLifecycleStateByName(dataClass, objectLifecycleStateName);
			condition.setObjectLifecycleState(objectLifecycleState);
					
			conditionComponent.addCondition(condition);
		}

		return conditionComponent;
	}
}
