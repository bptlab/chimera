package de.hpi.bpt.chimera.parser.datamodel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.datamodel.DataModelClass;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.EventClass;

public class DataModelClassParser {

	private DataModelClassParser() {
	}

	/*
	 * creates DataModelClass by parsing certain JSON-String
	 */
	public static DataModelClass parseDataModelClass(final JSONObject dataModelClassJson) {
		DataModelClass dataModelClass;

		if (dataModelClassJson.getBoolean("is_event")) {
			dataModelClass = new EventClass();
		} else {
			dataModelClass = new DataClass();
		}

		String name = dataModelClassJson.getString("name");
		validateName(name);
		dataModelClass.setName(name);

		List<DataAttribute> dataAttributes = getDataAttributes(dataModelClassJson.getJSONArray("attributes"));
		dataModelClass.setAttributes(dataAttributes);

		// TODO: validate Olcs

		return dataModelClass;
	}

	/*
	 * creates list of DataAttributes with DataAttributeParser
	 */
	public static List<DataAttribute> getDataAttributes(JSONArray attributeJsonArray) {
		int arraySize = attributeJsonArray.length();
		List<DataAttribute> dataAttributes = new ArrayList<>();

		for (int i = 0; i < arraySize; i++) {
			JSONObject dataAttributeJson = attributeJsonArray.getJSONObject(i);
			DataAttribute dataAttribute = DataAttributeParser.parseDataAttribute(dataAttributeJson);
			dataAttributes.add(dataAttribute);
		}

		return dataAttributes;
	}


	// TODO: put this in validator
	/*
	 * checks if name of DataClass contains only unicode letters, numbers or
	 * space (' ')
	 */
	private static void validateName(String name) {
		if (!StringUtils.isAlphanumericSpace(name)) {
			throw new IllegalArgumentException(String.format("%s is not a valid class name", name));
		}
	}
}
