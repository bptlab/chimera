package de.hpi.bpt.chimera.rest.beans.activity;

import de.hpi.bpt.chimera.execution.data.DataManager;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycle;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataUpdateRaw {
    private String dataclassName;
    private String state;
    private List<DataAttributeValueRaw> attributes;

    public DataUpdate convert(DataManager dataManager) {
        DataUpdate dataUpdate = new DataUpdate();

        Optional<DataClass> dataClassOptional = dataManager.getDataModel().getDataClassByName(getDataclassName());
        if (!dataClassOptional.isPresent()) {
            throw new IllegalArgumentException(String.format("Dataclass name %s not assigned", getDataclassName()));
        }
        DataClass dataClass = dataClassOptional.get();
        dataUpdate.setDataClass(dataClass);

        Optional<ObjectLifecycleState> objectLifecycleStateOptional = dataClass.getObjectLifecycleStateByName(getState());
        if (!objectLifecycleStateOptional.isPresent()) {
            throw new IllegalArgumentException(String.format("Objectject Lifecycle State name %s not assigned", getState()));
        }
        dataUpdate.setObjectLifecycleState(objectLifecycleStateOptional.get());

        List<DataAttributeValue> dataAttributeValues = new ArrayList<>();
        for (DataAttributeValueRaw attribute : attributes) {
            String dataAttributeName = attribute.getName();
            Optional<DataAttribute> dataAttributeOptional = dataClass.getDataAttributeByName(dataAttributeName);
            if (!dataAttributeOptional.isPresent()) {
                throw new IllegalArgumentException(String.format("Attribute name %s not assigned", dataAttributeName));
            }
            DataAttributeValue dataAttributeValue = new DataAttributeValue(dataAttributeOptional.get(), attribute.getValue());
            dataAttributeValues.add(dataAttributeValue);
        }
        dataUpdate.setDataAttributeValues(dataAttributeValues);
        return dataUpdate;
    }

    public String getDataclassName() {
        return dataclassName;
    }

    public void setDataclassName(String dataclassName) {
        this.dataclassName = dataclassName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<DataAttributeValueRaw> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<DataAttributeValueRaw> attributes) {
        this.attributes = attributes;
    }
}
