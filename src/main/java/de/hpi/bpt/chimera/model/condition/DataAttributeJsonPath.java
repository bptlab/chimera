package de.hpi.bpt.chimera.model.condition;

import de.hpi.bpt.chimera.model.datamodel.DataAttribute;

public class DataAttributeJsonPath {
	private DataAttribute dataAttribute;
	private String jsonPath;

	public DataAttribute getDataAttribute() {
		return dataAttribute;
	}

	public void setDataAttribute(DataAttribute dataAttribute) {
		this.dataAttribute = dataAttribute;
	}

	public String getJsonPath() {
		return jsonPath;
	}

	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}
}
