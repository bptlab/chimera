package de.hpi.bpt.chimera.parser.fragment;

import org.json.JSONObject;

import de.hpi.bpt.chimera.model.fragment.Fragment;

public class FragmentParser {

	private FragmentParser() {
	}

	public static Fragment parseFragment(JSONObject fragmentJson) {
		Fragment fragment = new Fragment();

		String name = fragmentJson.getString("name");
		fragment.setName(name);

		int versionNumber = fragmentJson.getInt("revision");
		fragment.setVersionNumber(versionNumber);

		String contentXML = fragmentJson.getString("content");
		fragment.setContentXML(contentXML);

		// TODO: validate fragment

		// TODO: add bpmn elements

		return fragment;
	}
}
