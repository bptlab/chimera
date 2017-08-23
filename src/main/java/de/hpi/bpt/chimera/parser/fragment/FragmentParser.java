package de.hpi.bpt.chimera.parser.fragment;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.fragment.Fragment;
import de.hpi.bpt.chimera.parser.IllegalCaseModelException;
import de.hpi.bpt.chimera.parser.fragment.bpmnxml.BpmnXmlFragmentParser;
import de.hpi.bpt.chimera.validation.NameValidation;

public class FragmentParser {
	private static final Logger log = Logger.getLogger((FragmentParser.class).getName());

	private FragmentParser() {
	}

	public static Fragment parseFragment(JSONObject fragmentJson) {
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
		} catch (JSONException e) {
			log.error(e);
			throw new IllegalCaseModelException("Invalid Fragment->" + e.getMessage());
		}
		// TODO: validate fragment

		// TODO: add bpmn elements
		fragment.setBpmnFragment(BpmnXmlFragmentParser.parseBpmnXmlFragment(fragment.getContentXML()));

		return fragment;
	}
}
