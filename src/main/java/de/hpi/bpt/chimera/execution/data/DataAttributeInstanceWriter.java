package de.hpi.bpt.chimera.execution.data;

import java.util.Map;

import org.apache.log4j.Logger;

import com.jayway.jsonpath.JsonPath;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;


/**
 * I fill the attributes of data objects with values.
 * TODO: think about exception handling
 */
public final class DataAttributeInstanceWriter {
	private static final Logger log = Logger.getLogger(CaseExecutioner.class);

	private DataAttributeInstanceWriter() {
	}

	/**
	 * Given a data object, some data represented as Json-String, and a map [attribute -> JsonPath expression]
	 * this method will evaluate the JsonPath expression against the data and write the resulting values into 
	 * the attributes of the data object.
	 *  
	 * @param dataObject - the {@link DataObject} to be written
	 * @param dataAttributeToJsonPath - a map with a JsonPath expression for each attribute
	 * @param json - the data as a Json-String
	 */
	public static void writeDataAttributeInstances(DataObject dataObject, Map<DataAttribute, String> dataAttributeToJsonPath, String json) {
		log.info(String.format("Writing attributes for DO <%s> based on data%n%s", dataObject, json));
		if (dataAttributeToJsonPath == null) {
			log.error("No map [Attribute -> JsonPath Expression] provided");
			return;
		}
		for (DataAttributeInstance dataAttributeInstance : dataObject.getDataAttributeInstances()) {
			
			if (!dataAttributeToJsonPath.containsKey(dataAttributeInstance.getDataAttribute())) {
				continue;
			}
			String jsonPath = dataAttributeToJsonPath.get(dataAttributeInstance.getDataAttribute());
			try {
				if (jsonPath != null && !jsonPath.isEmpty()) {
					String value = JsonPath.read(json, jsonPath).toString();
					dataAttributeInstance.setValue(value);
				}
			} catch (Exception e) {
				log.error("Error while evaluating JsonPath expression against Json data. Check the JsonPath expression and data.", e);
				dataAttributeInstance.setValue("ERROR");
			}
		}
	}
}
