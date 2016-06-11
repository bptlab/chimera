package de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the start query for one data object.
 */
public class StartQueryPart {
    private int startStateId;
    private int dataClassId;
    private Map<Integer, String> attributeIdToJsonPath;

    public StartQueryPart(int dataclassId, int stateId) {
        this.dataClassId = dataclassId;
        this.startStateId = stateId;
        this.attributeIdToJsonPath = new HashMap<>();
    }

    public int getDataClassId() {
        return dataClassId;
    }

    public void setDataClassId(int dataClassId) {
        this.dataClassId = dataClassId;
    }
    public int getStartStateId() {
        return startStateId;
    }

    public void setStartStateId(int startStateId) {
        this.startStateId = startStateId;
    }

    public Map<Integer, String> getAttributeIdToJsonPath() {
        return attributeIdToJsonPath;
    }

    public void addAttributeMapping(int attribute, String jsonPath) {
        this.attributeIdToJsonPath.put(attribute, jsonPath);
    }
}
