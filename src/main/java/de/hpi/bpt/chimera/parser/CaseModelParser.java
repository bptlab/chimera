package de.hpi.bpt.chimera.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.datamodel.DataModel;
import de.hpi.bpt.chimera.model.fragment.Fragment;
import de.hpi.bpt.chimera.parser.datamodel.DataModelParser;
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

			List<Fragment> fragments = getFragments(caseModelJson.getJSONArray("fragments"));
			caseModel.setFragments(fragments);
		} catch (JSONException e) {
			log.error(e);
			throw new JSONException("Invalid CaseModel");
		}
		return caseModel;
	}

	// TODO: put this in validator
	private static void validateFragmentAmount(int fragmentAmount) {
		if (fragmentAmount == 0) {
			throw new IllegalArgumentException("Invalid CaseModel - no fragments specified");
		}
	}

	/**
	 * Create List of Fragments out of fragmentJsonArray. Uses FragmentParser.
	 * 
	 * @param fragmentJsonArray
	 * @return List of Fragments
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
}
