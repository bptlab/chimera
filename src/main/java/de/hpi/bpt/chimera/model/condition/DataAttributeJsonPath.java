package de.hpi.bpt.chimera.model.condition;

import org.json.JSONObject;

import de.hpi.bpt.chimera.model.datamodel.DataAttribute;

public class DataAttributeJsonPath {
	private DataAttribute dataAttribute;
	private JSONObject jsonPath;

	public DataAttribute getDataAttribute() {
		return dataAttribute;
	}

	public void setDataAttribute(DataAttribute dataAttribute) {
		this.dataAttribute = dataAttribute;
	}

	public JSONObject getJsonPath() {
		return jsonPath;
	}

	public void setJsonPath(JSONObject jsonPath) {
		this.jsonPath = jsonPath;
	}
}
