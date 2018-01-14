package de.hpi.bpt.chimera.execution.data;

import java.util.Map;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;

// TODO: think about exception handling
public class DataAttributeInstanceWriter {
	private static final Logger log = Logger.getLogger(CaseExecutioner.class);

	private DataAttributeInstanceWriter() {
	}

	public static void writeDataAttributeInstances(DataObject dataObject, Map<DataAttribute, String> dataAttributeToJsonPath, String json) {
		for (DataAttributeInstance dataAttributeInstance : dataObject.getDataAttributeInstances()) {
			if (!dataAttributeToJsonPath.containsKey(dataAttributeInstance.getDataAttribute())) {
				continue;
			}
			String jsonPath = dataAttributeToJsonPath.get(dataAttributeInstance.getDataAttribute());
			try {
				Object value = com.jayway.jsonpath.JsonPath.read(json, jsonPath);
				dataAttributeInstance.setValue(value);
			} catch (Exception e) {
				log.error("An Exception occured while parsing the given JSON according to the given JSON-Path. Maybe there is a mistake in the JSON-Path. " + e.getMessage());
				dataAttributeInstance.setValue("ERROR");
				throw e;
			}
		}
	}
}
