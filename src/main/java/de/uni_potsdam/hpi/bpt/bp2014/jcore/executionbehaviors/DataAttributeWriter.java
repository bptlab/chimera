package de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors;

import com.jayway.jsonpath.JsonPath;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbPathMapping;
import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbLogEntry;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.DataAttributeInstance;
import org.apache.log4j.Logger;

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
    private final HistoryLogger attributeLogger = new aaHistoryLogger();
    private final int scenarioInstanceId;
    Map<Integer, String> attributeIdToJsonPath;

    /**
     * Creates
     * @param controlNodeId The id of the webservice task or event, writes the data attributes.
     * @param scenarioInstanceId
     */
    public DataAttributeWriter(int controlNodeId, int controlNodeInstanceId, int scenarioInstanceId) {
        DbPathMapping pathMapping = new DbPathMapping();
        this.attributeIdToJsonPath = pathMapping.getPathsForAttributesOfControlNode(controlNodeId);
        this.controlNodeInstanceId = controlNodeInstanceId;
        this.scenarioInstanceId = scenarioInstanceId;
    }

    public void writeDataAttributesFromJson(String json, List<DataAttributeInstance> dataAttributeInstances) {
        Map<Integer, DataAttributeInstance> idToDataAttributeInstance = dataAttributeInstances
                .stream().collect(Collectors.toMap(DataAttributeInstance::getDataAttributeInstanceId, x -> x));
        for (Map.Entry<Integer, String> idToPathEntry : attributeIdToJsonPath.entrySet()) {
            int dataAttributeInstanceId = idToPathEntry.getKey();
            DataAttributeInstance instance = idToDataAttributeInstance.get(dataAttributeInstanceId);
            String jsonPath = idToPathEntry.getValue();
            Object value = JsonPath.read(json, jsonPath);
            if (instance.isValueAllowed(value)) {
                new DbLogEntry().logDataAttributeTransition(instance.getDataAttributeInstanceId(),
                        value, controlNodeInstanceId, scenarioInstanceId);
                instance.setValue(value);
            } else {
                LOGGER.error("Attribute value could not be set because it has the wrong data type.");
            }
        }
    }

    public void setAttributeIdToJsonPath(Map<Integer, String> attributeIdToJsonPath) {
        this.attributeIdToJsonPath = attributeIdToJsonPath;
    }
}
