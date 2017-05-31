package de.hpi.bpt.chimera.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.datamodel.DataModel;
import de.hpi.bpt.chimera.model.condition.CaseStartTrigger;
import de.hpi.bpt.chimera.model.condition.TerminationCondition;
import de.hpi.bpt.chimera.model.fragment.Fragment;
import de.hpi.bpt.chimera.parser.datamodel.DataModelParser;
import de.hpi.bpt.chimera.parser.condition.CaseStartTriggerParser;
import de.hpi.bpt.chimera.parser.condition.TerminationConditionParser;
import de.hpi.bpt.chimera.parser.fragment.FragmentParser;
import de.hpi.bpt.chimera.validation.NameValidator;

public class CaseModelParser {
	private static final Logger log = Logger.getLogger((CaseModelParser.class).getName());

	private CaseModelParser() {
	}

	/**
	 * Parse CaseModel out of jsonString. Uses DataModelParser.
	 * 
	 * @param jsonString
	 * @return CaseModel
	 */
	public static CaseModel parseCaseModel(final String jsonString) {
		CaseModel caseModel = new CaseModel();
		try {
			JSONObject caseModelJson = new JSONObject(jsonString);

			String id = caseModelJson.getString("_id");
			caseModel.setId(id);

			String name = caseModelJson.getString("name");
			NameValidator.validateName(name);
			caseModel.setName(name);

			int versionNumber = caseModelJson.getInt("revision");
			caseModel.setVersionNumber(versionNumber);

			DataModel dataModel = DataModelParser.parseDataModel(caseModelJson.getJSONObject("domainmodel"));
			caseModel.setDataModel(dataModel);

			// DataModel has to be parsed, before TerminationCondition and
			// CaseStartTrigger can be parsed. Both parser get help by
			// CaseModelParserHelper
			CaseModelParserHelper parserHelper = new CaseModelParserHelper(dataModel);

			List<CaseStartTrigger> caseStartTrigger = getCaseStartTrigger(caseModelJson.getJSONArray("startconditions"), parserHelper);
			caseModel.setStartCaseTrigger(caseStartTrigger);

			TerminationCondition terminationCondition = TerminationConditionParser.parseTerminationCondition(caseModelJson.getJSONArray("terminationconditions"), parserHelper);
			caseModel.setTerminationCondition(terminationCondition);
			
			List<Fragment> fragments = getFragments(caseModelJson.getJSONArray("fragments"));
			caseModel.setFragments(fragments);
		} catch (JSONException e) {
			log.error(e);
			throw new IllegalCaseModelException("Invalid CaseModel: " + e.getMessage());
		} catch (IllegalCaseModelException e) {
			throw e;
		}
		return caseModel;
	}

	/**
	 * Create list of CaseStartTrigger out of caseStartTriggerjsonArray. Uses
	 * CaseStartTriggerParser with help of parserHelper.
	 * 
	 * @param caseStartTriggerjsonArray
	 * @param parserHelper
	 * @return List<CaseStartTrigger>
	 */
	private static List<CaseStartTrigger> getCaseStartTrigger(JSONArray caseStartTriggerjsonArray, CaseModelParserHelper parserHelper) {
		int arraySize = caseStartTriggerjsonArray.length();
		List<CaseStartTrigger> caseStartTrigger = new ArrayList<>();

		for (int i = 0; i < arraySize; i++) {
			JSONObject caseStartTriggerJson = caseStartTriggerjsonArray.getJSONObject(i);

			CaseStartTrigger startTrigger = CaseStartTriggerParser.parseCaseStarterTrigger(caseStartTriggerJson, parserHelper);
			caseStartTrigger.add(startTrigger);
		}

		return caseStartTrigger;
	}

	/**
	 * Create list of Fragments out of fragmentJsonArray. Uses FragmentParser.
	 * 
	 * @param fragmentJsonArray
	 * @return List<Fragment>
	 */
	private static List<Fragment> getFragments(JSONArray fragmentJsonArray) {
		int arraySize = fragmentJsonArray.length();
		validateFragmentAmount(arraySize);
		List<Fragment> fragments = new ArrayList<>();

		for (int i = 0; i < arraySize; i++) {
			JSONObject fragmentJson = fragmentJsonArray.getJSONObject(i);
			Fragment currentFragment = FragmentParser.parseFragment(fragmentJson);
			fragments.add(currentFragment);
		}
		return fragments;
	}

	// TODO: put this in validator
	private static void validateFragmentAmount(int fragmentAmount) {
		if (fragmentAmount == 0) {
			throw new IllegalCaseModelException("Invalid CaseModel - no fragments specified");
		}
	}
}
