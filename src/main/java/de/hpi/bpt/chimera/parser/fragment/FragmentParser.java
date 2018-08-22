package de.hpi.bpt.chimera.parser.fragment;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.condition.FragmentPreCondition;
import de.hpi.bpt.chimera.model.fragment.Fragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.BpmnFragment;
import de.hpi.bpt.chimera.parser.CaseModelParserHelper;
import de.hpi.bpt.chimera.parser.IllegalCaseModelException;
import de.hpi.bpt.chimera.parser.condition.DataStateConditionParser;
import de.hpi.bpt.chimera.parser.fragment.bpmn.BpmnXmlFragmentParser;
import de.hpi.bpt.chimera.validation.FragmentValidator;
import de.hpi.bpt.chimera.validation.NameValidation;

public final class FragmentParser {
	private static final Logger log = Logger.getLogger((FragmentParser.class).getName());

	private FragmentParser() {
	}

	// TODO: validate fragment
	/**
	 * 
	 * Parses a Fragment given as a JSONObject. Therefore calls the
	 * BpmnXmlFragmentParser with the in the JSONObject given bpmn-xml-fragment,
	 * which then creates the BpmnFragment.
	 * 
	 * @param fragmentJson
	 * @param parserHelper
	 * @return the built up Fragment object
	 */
	public static Fragment parseFragment(JSONObject fragmentJson, CaseModelParserHelper parserHelper) {
		Fragment fragment = new Fragment();

		try {
			String id = fragmentJson.getString("_id");
			fragment.setId(id);

			String name = fragmentJson.getString("name");
			NameValidation.validateName(name);
			fragment.setName(name);

			int versionNumber = fragmentJson.getInt("revision");
			fragment.setVersionNumber(versionNumber);

			String contentXML = fragmentJson.getString("content");
			fragment.setContentXML(contentXML);

			if (fragmentJson.has("preconditions")) {
				FragmentPreCondition fragmentPreCondition = DataStateConditionParser.parseFragmentPreCondition(fragmentJson.getJSONArray("preconditions"), parserHelper);
				fragment.setFragmentPreCondition(fragmentPreCondition);
			} else {
				fragment.setFragmentPreCondition(new FragmentPreCondition());
			}


			BpmnFragment bmpnFragment = BpmnXmlFragmentParser.parseBpmnXmlFragment(contentXML, parserHelper);
			fragment.setBpmnFragment(bmpnFragment);

			FragmentValidator validator = new FragmentValidator(fragment);
			validator.validate();
		} catch (JSONException | IllegalArgumentException e) {
			log.error(e);
			throw new IllegalCaseModelException("Invalid Fragment: " + e.getMessage());
		}



		return fragment;
	}
}
