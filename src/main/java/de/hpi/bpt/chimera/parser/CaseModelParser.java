package de.hpi.bpt.chimera.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.datamodel.DataModel;
import de.hpi.bpt.chimera.model.fragment.Fragment;
import de.hpi.bpt.chimera.parser.datamodel.DataModelParser;
import de.hpi.bpt.chimera.parser.fragment.FragmentParser;

public class CaseModelParser {

	private CaseModelParser() {
	}

	public static CaseModel parseCaseModel(final String jsonString) {
		JSONObject caseModelJson = new JSONObject(jsonString);
		CaseModel caseModel = new CaseModel();

		String name = caseModelJson.getString("name");
		caseModel.setName(name);

		int versionNumber = caseModelJson.getInt("revision");
		caseModel.setVersionNumber(versionNumber);

		DataModel dataModel = DataModelParser.parseDataModel(caseModelJson.getJSONObject("domainmodel"));
		caseModel.setDataModel(dataModel);

		List<Fragment> fragments = getFragments(caseModelJson.getJSONArray("fragments"));
		caseModel.setFragments(fragments);

		return caseModel;
	}

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
			throw new IllegalArgumentException("No fragments specified");
		}
	}
}
