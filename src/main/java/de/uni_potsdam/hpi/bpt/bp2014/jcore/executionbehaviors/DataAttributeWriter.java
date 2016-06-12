package de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors;

import com.jayway.jsonpath.JsonPath;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbPathMapping;
import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbLogEntry;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ExecutionService;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataAttributeInstance;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * Creates
     * @param controlNodeId The id of the webservice task or event, writes the data attributes.
     * @param scenarioInstance
     */
    public DataAttributeWriter(int controlNodeId, int controlNodeInstanceId, ScenarioInstance scenarioInstance) {
        DbPathMapping pathMapping = new DbPathMapping();
        this.attributeIdToJsonPath = pathMapping.getPathsForAttributesOfControlNode(controlNodeId);
        this.controlNodeInstanceId = controlNodeInstanceId;
        this.scenarioInstance = scenarioInstance;
    }

    public void writeDataAttributesFromJson(String json, List<DataAttributeInstance> dataAttributeInstances) {
        Map<Integer, String> idToValue = new HashMap<>();
        for (Map.Entry<Integer, String> idToPathEntry : attributeIdToJsonPath.entrySet()) {
            int dataAttributeId = idToPathEntry.getKey();
            // TODO safety check
            int dataAttributeInstanceId = dataAttributeInstances.stream()
                    .filter(x -> x.getDataAttributeId() == dataAttributeId)
                    .map(x -> x.getId())
                    .findFirst().get();
            String jsonPath = idToPathEntry.getValue();
            String value = JsonPath.read(json, jsonPath).toString();
            idToValue.put(dataAttributeInstanceId, value);
        }

        scenarioInstance.getDataManager().setAttributeValues(this.controlNodeInstanceId, idToValue);
    }

    public void setAttributeIdToJsonPath(Map<Integer, String> attributeIdToJsonPath) {
        this.attributeIdToJsonPath = attributeIdToJsonPath;
    }
}
