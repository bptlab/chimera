package de.hpi.bpt.chimera.execution.data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	public static void writeDataAttributeInstances(DataObject outputDataObject, Map<DataAttribute, String> dataAttributeToKeys, String json, List<DataObject> inputDataObjects) {
		Map<DataAttribute, String> dataAttributeToJsonPath =
				dataAttributeToKeys.entrySet().stream().filter(map -> map.getValue().startsWith("$")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		Map<DataAttribute, String> dataAttributeToProcessVariable =
				dataAttributeToKeys.entrySet().stream().filter(map -> map.getValue().startsWith("#")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		DataAttributeInstanceWriter.writeDataAttributeInstancesFromJson(outputDataObject, dataAttributeToJsonPath, json);
		DataAttributeInstanceWriter.writeDataAttributeInstancesFromDataObject(outputDataObject, dataAttributeToProcessVariable, inputDataObjects);
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
	public static void writeDataAttributeInstancesFromJson(DataObject dataObject, Map<DataAttribute, String> dataAttributeToJsonPath, String json) {
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

	public static void writeDataAttributeInstancesFromDataObject(DataObject outputDataObject, Map<DataAttribute, String> dataAttributeToProcessVariable, List<DataObject> inputDataObjects) {
		log.info(String.format("Writing attributes for DO <%s> based on data...", outputDataObject));
		if (dataAttributeToProcessVariable == null) {
			log.error("No map [Attribute -> Process Variable Expression] provided");
			return;
		}
		for (DataAttributeInstance dataAttributeInstance : outputDataObject.getDataAttributeInstances()) {

			if (!dataAttributeToProcessVariable.containsKey(dataAttributeInstance.getDataAttribute())) {
				continue;
			}
			String processVariable = dataAttributeToProcessVariable.get(dataAttributeInstance.getDataAttribute());
			try {
				if (processVariable != null && !processVariable.isEmpty()) {
					processVariable = processVariable.substring(1);
					String dcReference = processVariable.split("\\.")[0];
					String attrReference = processVariable.split("\\.")[1];
					DataObject inputDataObject =
							inputDataObjects
									.stream()
									.filter(x -> x.getDataClass().getName() == dcReference)
									.findFirst()
									.get();
					String value = inputDataObject.getDataAttributeInstanceByName(attrReference).getValue().toString();
					dataAttributeInstance.setValue(value);
				}
			} catch (Exception e) {
				log.error("Error while evaluating ProcessVariable expression against Data Object. Check the Process Variable and data.", e);
				dataAttributeInstance.setValue("ERROR");
			}
		}
	}
}
