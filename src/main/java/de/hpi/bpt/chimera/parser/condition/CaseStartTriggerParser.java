package de.hpi.bpt.chimera.parser.condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		} catch (IllegalCaseModelException e) {
			throw e;
		}

		return caseStartTrigger;
	}

	/**
	 * Parse the consequences of a CaseStartTrigger
	 * 
	 * @param triggerConsequenceJsonArray
	 * @param parserHelper
	 * @return List of CaseStartTriggerConsequence
	 */
	private static List<CaseStartTriggerConsequence> parseTriggerConsequences(JSONArray triggerConsequenceJsonArray, CaseModelParserHelper parserHelper) {
		int arraySize = triggerConsequenceJsonArray.length();
		// for easier handling the CaseStartTriggerConsequence are stored in a
		// HashMap referred to their DataObjectStateConditions
		Map<DataObjectStateCondition, CaseStartTriggerConsequence> conditionToTrigger = new HashMap<>();

		for (int i = 0; i < arraySize; i++) {
			try {
				JSONObject triggerConsequenceJson = triggerConsequenceJsonArray.getJSONObject(i);
				DataObjectStateCondition dataObjectStateCondition = new DataObjectStateCondition();

				String dataClassName = triggerConsequenceJson.getString("classname");
				DataClass dataClass = parserHelper.getNameToDataClass(dataClassName);
				dataObjectStateCondition.setDataClass(dataClass);

				String objectLifecycleStateName = triggerConsequenceJson.getString("state");
				ObjectLifecycleState objectLifecycleState = parserHelper.getNameToObjectLifecycleState(dataClass, objectLifecycleStateName);
				dataObjectStateCondition.setState(objectLifecycleState);

				List<DataAttributeJsonPath> dataAttributeJsonPath = parseDataAttributeJsonPaths(triggerConsequenceJson.getJSONArray("mapping"), parserHelper, dataClass);
				
				if (conditionToTrigger.containsKey(dataObjectStateCondition)) {
					// TODO: think about whether a certain DataAttribute needs
					// more than one instantiations
					CaseStartTriggerConsequence triggerConsequence = conditionToTrigger.get(dataObjectStateCondition);
					triggerConsequence.addMapping(dataAttributeJsonPath);
				} else {
					CaseStartTriggerConsequence triggerConsequence = new CaseStartTriggerConsequence();
					
					triggerConsequence.setMapping(dataAttributeJsonPath);
					conditionToTrigger.put(dataObjectStateCondition, triggerConsequence);
				}

			} catch (JSONException | IllegalArgumentException e) {
				log.error(e);
				throw new IllegalCaseModelException("Invalid CaseStartTriggerConseqeuence - " + e.getMessage());
			} catch (IllegalCaseModelException e) {
				throw e;
			}
		}

		return new ArrayList<>(conditionToTrigger.values());
	}

	/**
	 * Parse a DataAttributeJson object where the DataAttribute belongs to the
	 * given DataClass.
	 * 
	 * @param mappingJsonArray
	 * @param parserHelper
	 * @param dataClass
	 * @return DataAttributeJsonPath
	 */
	private static List<DataAttributeJsonPath> parseDataAttributeJsonPaths(JSONArray mappingJsonArray, CaseModelParserHelper parserHelper, DataClass dataClass) {
		int arraySize = mappingJsonArray.length();
		CaseStartTriggerValidation.validateMappingAmount(arraySize);

		List<DataAttributeJsonPath> mapping = new ArrayList<>();
		for (int i = 0; i < arraySize; i++) {
			try {
				JSONObject mappingJson = mappingJsonArray.getJSONObject(i);

				DataAttributeJsonPath dataAttributeJsonPath = new DataAttributeJsonPath();

				String dataAttributeName = mappingJson.getString("attr");
				DataAttribute dataAttribute = parserHelper.getNameToDataAttribute(dataClass, dataAttributeName);
				dataAttributeJsonPath.setDataAttribute(dataAttribute);

				String jsonPathString = mappingJson.getString("path");
				dataAttributeJsonPath.setJsonPath(jsonPathString);

				mapping.add(dataAttributeJsonPath);
			} catch (JSONException | IllegalArgumentException e) {
				log.error(e);
				throw new IllegalCaseModelException("Invalid CaseStartTriggerMapping - " + e.getMessage());
			}
		}

		return mapping;
	}
}
