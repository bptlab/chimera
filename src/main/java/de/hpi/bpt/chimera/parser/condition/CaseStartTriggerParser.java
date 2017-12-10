package de.hpi.bpt.chimera.parser.condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.JsonPath;
import de.hpi.bpt.chimera.model.condition.CaseStartTrigger;
import de.hpi.bpt.chimera.model.condition.CaseStartTriggerConsequence;
import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.parser.CaseModelParserHelper;
import de.hpi.bpt.chimera.parser.IllegalCaseModelException;
import de.hpi.bpt.chimera.validation.CaseStartTriggerValidation;

public class CaseStartTriggerParser {
	private static final Logger log = Logger.getLogger((CaseStartTriggerParser.class).getName());

	private CaseStartTriggerParser() {
	}

	/**
	 * Parse a CaseStartTrigger out of a JsonString with help of
	 * CaseModelParserHelper.
	 * 
	 * @param caseStartTriggerJson
	 * @param parserHelper
	 * @return CaseStartTrigger
	 */
	public static CaseStartTrigger parseCaseStarterTrigger(JSONObject caseStartTriggerJson, CaseModelParserHelper parserHelper, CaseModel parentCaseModel) {
		CaseStartTrigger caseStartTrigger = new CaseStartTrigger();
		try {
			String queryExecutionPlan = caseStartTriggerJson.getString("query");
			caseStartTrigger.setQueryExecutionPlan(queryExecutionPlan);

			List<CaseStartTriggerConsequence> triggerConsequences = parseTriggerConsequences(caseStartTriggerJson.getJSONArray("dataclasses"), parserHelper);
			caseStartTrigger.setTriggerConsequence(triggerConsequences);

			caseStartTrigger.setParentCaseModel(parentCaseModel);

		} catch (JSONException | IllegalArgumentException e) {
			log.error(e);
			throw new IllegalCaseModelException("Invalid CaseStartTrigger - " + e.getMessage());
		} catch (IllegalCaseModelException e) {
			throw e;
		}

		return caseStartTrigger;
	}

	/**
	 * Parse the consequences of a CaseStartTrigger. Every
	 * DataObjectStateCondition exists only once. That means if two queries
	 * refer to the same DataObjectStateCondition they will be put together.
	 * 
	 * @param triggerConsequenceJsonArray
	 * @param parserHelper
	 * @return List of CaseStartTriggerConsequence
	 */
	private static List<CaseStartTriggerConsequence> parseTriggerConsequences(JSONArray triggerConsequenceJsonArray, CaseModelParserHelper parserHelper) {
		int arraySize = triggerConsequenceJsonArray.length();
		// for easier handling the CaseStartTriggerConsequence are stored in a
		// HashMap referred to their DataObjectStateConditions
		Map<AtomicDataStateCondition, CaseStartTriggerConsequence> conditionToTrigger = new HashMap<>();

		for (int i = 0; i < arraySize; i++) {
			try {
				JSONObject triggerConsequenceJson = triggerConsequenceJsonArray.getJSONObject(i);
				AtomicDataStateCondition dataObjectStateCondition = new AtomicDataStateCondition();

				String dataClassName = triggerConsequenceJson.getString("classname");
				DataClass dataClass = parserHelper.getNameToDataClass(dataClassName);
				dataObjectStateCondition.setDataClass(dataClass);

				String objectLifecycleStateName = triggerConsequenceJson.getString("state");
				ObjectLifecycleState objectLifecycleState = parserHelper.getNameToObjectLifecycleState(dataClass, objectLifecycleStateName);
				dataObjectStateCondition.setObjectLifecycleState(objectLifecycleState);

				Map<DataAttribute, JsonPath> dataAttributeToJsonPath = parseDataAttributeJsonPaths(triggerConsequenceJson.getJSONArray("mapping"), parserHelper, dataClass);
				
				if (conditionToTrigger.containsKey(dataObjectStateCondition)) {
					// If DataObjectStateCondition already occurred the
					// DataAttributeJsonPaths are just added.
					CaseStartTriggerConsequence triggerConsequence = conditionToTrigger.get(dataObjectStateCondition);
					triggerConsequence.getDataAttributeToJsonPath().putAll(dataAttributeToJsonPath);
				} else {
					// If DataObjectStateCondition does not already occurred a
					// new CaseStartTrigger gets created.
					CaseStartTriggerConsequence triggerConsequence = new CaseStartTriggerConsequence(dataObjectStateCondition, dataAttributeToJsonPath);

					conditionToTrigger.put(dataObjectStateCondition, triggerConsequence);
				}

			} catch (JSONException | IllegalArgumentException e) {
				log.error(e);
				throw new IllegalCaseModelException("Invalid CaseStartTriggerConseqeuence - " + e.getMessage());
			} catch (IllegalCaseModelException e) {
				throw e;
			}
		}
		List<CaseStartTriggerConsequence> caseStartTriggers = new ArrayList<>(conditionToTrigger.values());
		CaseStartTriggerValidation.validateCaseStartTriggers(caseStartTriggers);
		return caseStartTriggers;
	}

	/**
	 * Parse a DataAttributeJson. The DataAttribute belongs to the given
	 * DataClass.
	 * 
	 * @param mappingJsonArray
	 * @param parserHelper
	 * @param dataClass
	 * @return DataAttributeJsonPath
	 */
	private static Map<DataAttribute, JsonPath> parseDataAttributeJsonPaths(JSONArray mappingJsonArray, CaseModelParserHelper parserHelper, DataClass dataClass) {
		int arraySize = mappingJsonArray.length();
		CaseStartTriggerValidation.validateMappingAmount(arraySize);

		Map<DataAttribute, JsonPath> mapping = new HashMap<>();
		for (int i = 0; i < arraySize; i++) {
			try {
				JSONObject mappingJson = mappingJsonArray.getJSONObject(i);

				String dataAttributeName = mappingJson.getString("attr");
				DataAttribute dataAttribute = parserHelper.getNameToDataAttribute(dataClass, dataAttributeName);

				String jsonPathString = mappingJson.getString("path");

				mapping.put(dataAttribute, new JsonPath(jsonPathString));
			} catch (JSONException | IllegalArgumentException e) {
				log.error(e);
				throw new IllegalCaseModelException("Invalid CaseStartTriggerMapping - " + e.getMessage());
			}
		}

		return mapping;
	}
}