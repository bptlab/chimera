package de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling;


public class EventTypeJsonObject {
	private String xsd;
	private String schemaName;
	private String timestampName;

	public String getTimestampName() {
		return timestampName;
	}

	public void setTimestampName(String timestampName) {
		this.timestampName = timestampName;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getXsd() {
		return xsd;
	}

	public void setXsd(String xsd) {
		this.xsd = xsd;
	}
}

