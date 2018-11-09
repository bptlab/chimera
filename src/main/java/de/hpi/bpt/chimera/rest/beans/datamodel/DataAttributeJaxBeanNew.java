package de.hpi.bpt.chimera.rest.beans.datamodel;

import de.hpi.bpt.chimera.execution.data.DataAttributeInstance;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;

public class DataAttributeJaxBeanNew {
    private String name;
    private String type;
    private String value;

    public DataAttributeJaxBeanNew(DataAttribute dataAttribute) {
        setName(dataAttribute.getName());
        setType(dataAttribute.getType());
        setValue("");
    }

    public DataAttributeJaxBeanNew(DataAttributeInstance dataAttributeInstance) {
        DataAttribute dataAttribute = dataAttributeInstance.getDataAttribute();
        setName(dataAttribute.getName());
        setType(dataAttribute.getType());
        Object value = dataAttributeInstance.getValue();
        if (value != null) {
            setValue(value.toString());
        } else {
            setValue("");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
