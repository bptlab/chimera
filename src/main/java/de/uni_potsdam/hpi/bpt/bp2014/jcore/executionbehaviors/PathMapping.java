package de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors;

import com.jayway.jsonpath.JsonPath;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbPathMapping;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.DataAttributeInstance;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class represents a mapping between the data attributes of a data object
 * and the given json path for retrieving the value of the response json object,
 * which is received either from a webservice or from an event.
 */

public class PathMapping {

    DbPathMapping dbPathMapping;
    Map<Integer, String> attributeIdToJsonPath;

    public PathMapping() {
        dbPathMapping = new DbPathMapping();
    }

    public PathMapping(int controlNodeId) {
        dbPathMapping = new DbPathMapping();
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
        this.attributeIdToJsonPath = dbPathMapping.getPathsForAttributesOfControlNode(controlNodeId);
    }
}
