package de.hpi.bpt.chimera.jcore.executionbehaviors;

import com.jayway.jsonpath.JsonPath;
import de.hpi.bpt.chimera.database.DbPathMapping;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.data.DataAttributeInstance;
import de.hpi.bpt.chimera.jcore.data.DataManager;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to update data attributes according to the result of a webservice task
 * or event.
 */
public class DataAttributeWriter {
	static final Logger LOGGER = Logger.getLogger(DataAttributeWriter.class);

	private final int controlNodeInstanceId;
	private final ScenarioInstance scenarioInstance;
	Map<Integer, String> attributeIdToJsonPath;

	/**
	 * @param controlNodeId    The id of the webservice task or event, writes the data attributes.
	 * @param scenarioInstance
	 */
	public DataAttributeWriter(int controlNodeId, int controlNodeInstanceId, ScenarioInstance scenarioInstance) {
		DbPathMapping pathMapping = new DbPathMapping();
		this.attributeIdToJsonPath = pathMapping.getPathsForAttributesOfControlNode(controlNodeId);
		this.controlNodeInstanceId = controlNodeInstanceId;
		this.scenarioInstance = scenarioInstance;
	}

	/**
	 * Writes data from the webservice response (in JSON) to the attributes of output data objects
	 * according to the JsonPath expressions of the attributes.
	 * @param json
	 * @param dataAttributeInstances
	 */
	public void writeDataAttributesFromJson(String json, List<DataAttributeInstance> dataAttributeInstances) {
		DataManager dataManager = scenarioInstance.getDataManager();
		for (DataAttributeInstance dai : dataAttributeInstances) {
			int attributeId = dai.getDataAttributeId();
			String jsonPath = attributeIdToJsonPath.get(attributeId);
			if (jsonPath != null && !jsonPath.isEmpty()) {
				String value = JsonPath.read(json, jsonPath).toString();
				dataManager.setAttributeValue(dai, value, controlNodeInstanceId);
			}
		}
	}

	public void setAttributeIdToJsonPath(Map<Integer, String> attributeIdToJsonPath) {
		this.attributeIdToJsonPath = attributeIdToJsonPath;
	}
}
