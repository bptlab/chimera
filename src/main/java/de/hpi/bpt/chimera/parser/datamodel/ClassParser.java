package de.hpi.bpt.chimera.parser.datamodel;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.datamodel.Class;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.EventClass;

public class ClassParser {

	private ClassParser() {
	}

	public static Class parseClass(final JSONObject classJson) {
		Class objectclass;

		if (classJson.getBoolean("is_event")) {
			objectclass = new DataClass();
		} else {
			objectclass = new EventClass();
		}

		String name = classJson.getString("name");
		validateName(name);
		objectclass.setName(name);

		List<DataAttribute> dataAttributes = DataAttributeParser.parseDataAttributes(classJson.getJSONArray("attributes"));
		objectclass.setAttributes(dataAttributes);

		// TODO: validate Olcs

		return objectclass;
	}

	// TODO: put this in validator
	private static void validateName(String name) {
		if (!StringUtils.isAlphanumericSpace(name)) {
			throw new IllegalArgumentException(String.format("%s is not a valid class name", name));
		}
	}
}
