package de.hpi.bpt.chimera.parser.datamodel;

import org.json.JSONObject;

import de.hpi.bpt.chimera.model.datamodel.DataAttribute;

public class DataAttributeParser {

	private DataAttributeParser() {
	}

	public static DataAttribute parseDataAttribute(JSONObject dataAttributeJson) {
		DataAttribute dataAttribute = new DataAttribute();

		String name = dataAttributeJson.getString("name");
		dataAttribute.setName(name);
		
		String type = dataAttributeJson.getString("type");
		dataAttribute.setType(type);

		return dataAttribute;
	}
}
