package de.hpi.bpt.chimera.parser.datamodel;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.validation.NameValidator;

public class DataAttributeParser {
	private static final Logger log = Logger.getLogger((DataAttributeParser.class).getName());

	private DataAttributeParser() {
	}

	public static DataAttribute parseDataAttribute(JSONObject dataAttributeJson) {
		DataAttribute dataAttribute = new DataAttribute();
		
		try {
			String name = dataAttributeJson.getString("name");
			NameValidator.validateName(name);
			dataAttribute.setName(name);

			String type = dataAttributeJson.getString("datatype");
			dataAttribute.setType(type);
		} catch (JSONException e) {
			log.error(e);
			throw new JSONException("Invalid DataAttribute");
		}
		return dataAttribute;
	}
}
