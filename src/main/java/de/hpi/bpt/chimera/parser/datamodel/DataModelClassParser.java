package de.hpi.bpt.chimera.parser.datamodel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.datamodel.DataModelClass;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.EventClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycle;

public class DataModelClassParser {

	private DataModelClassParser() {
	}

	/**
	 * Parse DataClass with dataClassJson. Uses ObjectLifecycleParser.
	 *
	 * @param dataClassJson
	 * @return DataClass
	 */
	public static DataClass parseDataClass(final JSONObject dataClassJson) {
		DataClass dataClass = new DataClass();
		parseDataModelClass(dataClassJson, dataClass);

		try {
			ObjectLifecycle objectLifecycle = ObjectLifecycleParser.parseObjectLifecylce(dataClassJson.getJSONObject("olc"));
			// TODO: validate objectLifecylce
			dataClass.setObjectLifecycle(objectLifecycle);
		} catch (JSONException e) {
			throw e;
		}

		return dataClass;
	}

	/**
	 * Parse EventClass with eventClassJson.
	 * 
	 * @param eventClassJson
	 * @return
	 */
	public static EventClass parseEventClass(final JSONObject eventClassJson) {
		EventClass eventClass = new EventClass();
		parseDataModelClass(eventClassJson, eventClass);

		return eventClass;
	}
	
	/**
	 * Parse DataModelClass out of dataModelClassJson.
	 * 
	 * @param dataModelClassJson
	 * @param DataModelClass
	 *            either DataClass or EventClass
	 */
	private static void parseDataModelClass(final JSONObject dataModelClassJson, DataModelClass dataModelClass) {
		try {
			String name = dataModelClassJson.getString("name");
			validateName(name);
			dataModelClass.setName(name);

			List<DataAttribute> dataAttributes = getDataAttributes(dataModelClassJson.getJSONArray("attributes"));
			dataModelClass.setAttributes(dataAttributes);
		} catch (JSONException e) {
			throw e;
		}
	}

	/**
	 * Create List of DataAttributes out of attributeJsonArray. Uses
	 * DataAttributeParser.
	 * 
	 * @param attributeJsonArray
	 * @return List of DataAttributes
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
	/**
	 * checks if name of DataClass contains only unicode letters, numbers or
	 * space (' ')
	 * 
	 * @param name
	 */
	private static void validateName(String name) {
		if (!StringUtils.isAlphanumericSpace(name)) {
			throw new IllegalArgumentException(String.format("%s is not a valid class name", name));
		}
	}
}
