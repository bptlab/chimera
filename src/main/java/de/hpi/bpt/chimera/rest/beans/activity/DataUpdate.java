package de.hpi.bpt.chimera.rest.beans.activity;

import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

import java.util.List;

public class DataUpdate {
    private DataClass dataClass;
    private ObjectLifecycleState objectLifecycleState;
    private List<DataAttributeValue> dataAttributeValues;

    public DataClass getDataClass() {
        return dataClass;
    }

    public void setDataClass(DataClass dataClass) {
        this.dataClass = dataClass;
    }

    public ObjectLifecycleState getObjectLifecycleState() {
        return objectLifecycleState;
    }

    public void setObjectLifecycleState(ObjectLifecycleState objectLifecycleState) {
        this.objectLifecycleState = objectLifecycleState;
    }

    public List<DataAttributeValue> getDataAttributeValues() {
        return dataAttributeValues;
    }

    public void setDataAttributeValues(List<DataAttributeValue> dataAttributeValues) {
        this.dataAttributeValues = dataAttributeValues;
    }
}
