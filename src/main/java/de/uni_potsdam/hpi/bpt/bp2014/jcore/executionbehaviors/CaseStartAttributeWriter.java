package de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors;

import com.jayway.jsonpath.JsonPath;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbStartQuery;
import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbLogEntry;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.DataAttributeInstance;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public class CaseStartAttributeWriter {

    static final Logger LOGGER = Logger.getLogger(CaseStartAttributeWriter.class);
    Map<Integer, String> attributeIdToJsonPath;

    public CaseStartAttributeWriter(int scenarioId, String queryId) {
        this.attributeIdToJsonPath = new DbStartQuery().getPathMappings(scenarioId).get(queryId);
    }

    public void writeDataAttributesFromJson(
            String json, List<DataAttributeInstance> dataAttributes, int scenarioInstanceId) {
        Map<Integer, DataAttributeInstance> idToDataAttributeInstance = dataAttributes
                .stream().collect(Collectors.toMap(DataAttributeInstance::getDataAttributeInstanceId, x -> x));

        for (Map.Entry<Integer, String> idToPathEntry : attributeIdToJsonPath.entrySet()) {
            int dataAttributeInstanceId = idToPathEntry.getKey();
            DataAttributeInstance instance = idToDataAttributeInstance.get(dataAttributeInstanceId);
            String jsonPath = idToPathEntry.getValue();
            Object value = JsonPath.read(json, jsonPath);
            if (instance.isValueAllowed(value)) {
                instance.setValue(value);
                new DbLogEntry().logDataattributeCreation(
                        instance.getDataAttributeInstanceId(), value, scenarioInstanceId);
            } else {
                LOGGER.error("Attribute value could not be set because it has the wrong data type.");
            }
        }
    }

    public boolean hasMapping() {
        return this.attributeIdToJsonPath.size() > 0;
    }
}

