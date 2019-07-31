package de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling;

public class DBTypeJsonObject {
    private String schemaName;
    private String attributes;

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributeString) { this.attributes = attributeString; }

}
