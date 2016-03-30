package de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors;

import com.jayway.jsonpath.JsonPath;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbPathMapping;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.DataAttributeInstance;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class is used to update data objects according to the result of a webservice task
 * or event.
 */
public class DataAttributeWriter {
    Map<Integer, String> attributeIdToJsonPath;

    /**
     * Creates
     * @param controlNodeId The id of the webservice task or event, writes the data attributes.
     */
    public DataAttributeWriter(int controlNodeId) {
        this.setAttributeIdToJsonPath(controlNodeId);
    }

    public void writeDataAttributesFromJson(String json, List<DataAttributeInstance> dataAttributeInstances) {
        Map<Integer, DataAttributeInstance> idToDataAttributeInstance = dataAttributeInstances
                .stream().collect(Collectors.toMap(DataAttributeInstance::getDataAttributeInstanceId, x -> x));
        for (Map.Entry<Integer, String> idToPathEntry : attributeIdToJsonPath.entrySet()) {
            int dataAttributeInstanceId = idToPathEntry.getKey();
            DataAttributeInstance instance = idToDataAttributeInstance.get(dataAttributeInstanceId);
            String jsonPath = idToPathEntry.getValue();
            Object value = JsonPath.read(json, jsonPath);
            instance.setValue(value);
        }
    }

    public void setAttributeIdToJsonPath(Map<Integer, String> attributeIdToJsonPath) {
        this.attributeIdToJsonPath = attributeIdToJsonPath;
    }

    public void setAttributeIdToJsonPath(int controlNodeId) {
        DbPathMapping pathMapping = new DbPathMapping();
        this.attributeIdToJsonPath = pathMapping.getPathsForAttributesOfControlNode(controlNodeId);
    }
}
