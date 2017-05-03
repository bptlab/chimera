package de.hpi.bpt.chimera.parser.datamodel;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.datamodel.DataAttribute;

public class DataAttributeParser {

	private DataAttributeParser() {
	}

	public static List<DataAttribute> parseDataAttributes(JSONArray attributeJsonArray) {
		int arraySize = attributeJsonArray.length();
		List<DataAttribute> dataAttributes = new ArrayList<>();

		for (int i = 0; i < arraySize; i++) {
			JSONObject currentDataAttribute = attributeJsonArray.getJSONObject(i);
			String name = currentDataAttribute.getString("name");
			String type = currentDataAttribute.getString("type");
			dataAttributes.add(new DataAttribute(name, type));
		}

		return dataAttributes;
	}
}
