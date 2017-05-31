package de.hpi.bpt.chimera.parser.condition;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.condition.CaseStartTrigger;
import de.hpi.bpt.chimera.model.condition.CaseStartTriggerConsequence;
import de.hpi.bpt.chimera.model.condition.DataAttributeJsonPath;
import de.hpi.bpt.chimera.model.condition.DataObjectStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.parser.CaseModelParserHelper;
import de.hpi.bpt.chimera.parser.IllegalCaseModelException;

public class CaseStartTriggerParser {
	private static final Logger log = Logger.getLogger((CaseStartTriggerParser.class).getName());

	private CaseStartTriggerParser() {
	}

	public static CaseStartTrigger parseCaseStarterTrigger(JSONObject caseStartTriggerJson, CaseModelParserHelper parserHelper) {
		CaseStartTrigger caseStartTrigger = new CaseStartTrigger();
		try {
			String queryExecutionPlan = caseStartTriggerJson.getString("query");
			caseStartTrigger.setQueryExecutionPlan(queryExecutionPlan);

			List<CaseStartTriggerConsequence> triggerConsequences = parseTriggerConsequences(caseStartTriggerJson.getJSONArray("dataclasses"), parserHelper);
			caseStartTrigger.setTriggerConsequence(triggerConsequences);

		} catch (JSONException | IllegalArgumentException e) {
			log.error(e);
			throw new IllegalCaseModelException("Invalid CaseStartTrigger - " + e.getMessage());
		}

		return caseStartTrigger;
	}

	private static List<CaseStartTriggerConsequence> parseTriggerConsequences(JSONArray triggerConsequenceJsonArray, CaseModelParserHelper parserHelper) {
		int arraySize = triggerConsequenceJsonArray.length();
		List<CaseStartTriggerConsequence> triggerConsequences = new ArrayList<>();

		for (int i = 0; i < arraySize; i++) {
			JSONObject triggerConsequenceJson = triggerConsequenceJsonArray.getJSONObject(i);
			CaseStartTriggerConsequence triggerConsequence = new CaseStartTriggerConsequence();
			
			DataObjectStateCondition dataObjectStateCondition = new DataObjectStateCondition();

			String dataClassName = triggerConsequenceJson.getString("classname");
			DataClass dataClass = parserHelper.getNameToDataClass(dataClassName);
			dataObjectStateCondition.setDataClass(dataClass);

			String objectLifecycleStateName = triggerConsequenceJson.getString("state");
			ObjectLifecycleState objectLifecycleState = parserHelper.getNameToObjectLifecycleState(dataClass, objectLifecycleStateName);
			dataObjectStateCondition.setState(objectLifecycleState);

			List<DataAttributeJsonPath> dataAttributeJsonPath = parseDataAttributeJsonPaths(triggerConsequenceJson.getJSONArray("mapping"), parserHelper, dataClass);
			triggerConsequence.setMapping(dataAttributeJsonPath);

			triggerConsequences.add(triggerConsequence);
		}

		return triggerConsequences;
	}

	private static List<DataAttributeJsonPath> parseDataAttributeJsonPaths(JSONArray mappingJsonArray, CaseModelParserHelper parserHelper, DataClass dataClass) {
		int arraySize = mappingJsonArray.length();
		validateMappingAmount(arraySize);

		List<DataAttributeJsonPath> mapping = new ArrayList<>();
		for (int i = 0; i < arraySize; i++) {
			JSONObject mappingJson = mappingJsonArray.getJSONObject(i);

			DataAttributeJsonPath dataAttributeJsonPath = new DataAttributeJsonPath();

			String dataAttributeName = mappingJson.getString("attr");
			DataAttribute dataAttribute = parserHelper.getNameToDataAttribute(dataClass, dataAttributeName);
			dataAttributeJsonPath.setDataAttribute(dataAttribute);
			
			String jsonPathString = mappingJson.getString("path");
			JSONObject jsonPath = new JSONObject(jsonPathString);
			dataAttributeJsonPath.setJsonPath(jsonPath);

			mapping.add(dataAttributeJsonPath);
		}

		return mapping;
	}

	// TODO: put this in validation
	private static void validateMappingAmount(int arraySize) {
		if (arraySize == 0) {
			throw new IllegalArgumentException("no mapping specified");
		}

	}
}
