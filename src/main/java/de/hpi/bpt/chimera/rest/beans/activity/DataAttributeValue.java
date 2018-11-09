package de.hpi.bpt.chimera.rest.beans.activity;

import de.hpi.bpt.chimera.model.datamodel.DataAttribute;

public class DataAttributeValue {
    private DataAttribute dataAttribute;
    private String value;

    public DataAttributeValue(DataAttribute dataAttribute, String value) {
        setDataAttribute(dataAttribute);
        setValue(value);
    }

    public DataAttribute getDataAttribute() {
        return dataAttribute;
    }

    public void setDataAttribute(DataAttribute dataAttribute) {
        this.dataAttribute = dataAttribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
