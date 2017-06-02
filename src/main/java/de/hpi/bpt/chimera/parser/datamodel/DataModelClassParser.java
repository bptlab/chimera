package de.hpi.bpt.chimera.parser.datamodel;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.datamodel.DataModelClass;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.EventClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycle;
import de.hpi.bpt.chimera.parser.IllegalCaseModelException;
import de.hpi.bpt.chimera.validation.NameValidation;

public class DataModelClassParser {
	private static final Logger log = Logger.getLogger((DataModelClassParser.class).getName());

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
			dataClass.setObjectLifecycle(objectLifecycle);

		} catch (JSONException e) {
			log.error(e);
			throw new IllegalCaseModelException("Invalid Dataclass - " + e.getMessage());
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
			NameValidation.validateName(name);
			dataModelClass.setName(name);

			List<DataAttribute> dataAttributes = getDataAttributes(dataModelClassJson.getJSONArray("attributes"));
			dataModelClass.setDataAttributes(dataAttributes);
		} catch (JSONException | IllegalArgumentException e) {
			log.error(e);
			throw new IllegalCaseModelException("Invalid DataModelClass - " + e.getMessage());
		} catch (IllegalCaseModelException e) {
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

		NameValidation.validateNameFrequency(dataAttributes);

		return dataAttributes;
	}
}
