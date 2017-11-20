package de.hpi.bpt.chimera.parser.condition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.TerminationCondition;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.parser.CaseModelParserHelper;
import de.hpi.bpt.chimera.parser.IllegalCaseModelException;

public class TerminationConditionParser {
	private static final Logger log = Logger.getLogger((TerminationConditionParser.class).getName());

	private TerminationConditionParser() {
	}

	/**
	 * Parse TerminationCondition out of jsonArray with help of given dataModel.
	 * 
	 * @param jsonArray
	 * @param dataModel
	 * @return TerminationCondition
	 */
	public static TerminationCondition parseTerminationCondition(JSONArray jsonArray, CaseModelParserHelper parserHelper) {
		TerminationCondition terminationCondition = new TerminationCondition();

		int arraySize = jsonArray.length();
		for (int i = 0; i < arraySize; i++) {
			try {
			String conditionComponentString = jsonArray.getString(i);
			terminationCondition.addConditionComponent(parseConditionComponent(conditionComponentString, parserHelper));
			} catch (JSONException | IllegalArgumentException e) {
				log.error(e);
				throw new IllegalCaseModelException("Invalid TerminationCondition - " + e.getMessage());
			}
		}

		return terminationCondition;
	}

	/**
	 * Parse a ConditionString into single ConditionComponent and make a big
	 * ConditionComponent out of it. Therefore use CaseModelParserHelper to get DataClass and ObjectLifecycleState.
	 * 
	 * @param conditionString
	 * @param dataModel
	 * @return TerminationConditionComponent
	 */
	private static ConditionSet parseConditionComponent(String conditionString, CaseModelParserHelper parserHelper) {
		ConditionSet conditionComponent = new ConditionSet();

		for (String component : conditionString.split(",")) {
			Pattern pattern = Pattern.compile("\\s?(.*)\\[(.*?)\\]");
			Matcher match = pattern.matcher(component);
			if (!match.find()) {
				throw new IllegalCaseModelException(String.format("%s is invalid termination String", component));
			}
			String dataClassName = match.group(1);
			String objectLifecycleStateName = match.group(2);

			AtomicDataStateCondition condition = new AtomicDataStateCondition();
			
			DataClass dataClass = parserHelper.getNameToDataClass(dataClassName);
			condition.setDataClass(dataClass);
			
			ObjectLifecycleState objectLifecycleState = parserHelper.getNameToObjectLifecycleState(dataClass, objectLifecycleStateName);
			condition.setObjectLifecycleState(objectLifecycleState);
					
			conditionComponent.addCondition(condition);
		}

		return conditionComponent;
	}
}
