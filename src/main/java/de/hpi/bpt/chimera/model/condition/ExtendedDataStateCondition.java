package de.hpi.bpt.chimera.model.condition;

public class ExtendedDataStateCondition extends AtomicDataStateCondition {
	private String jsonPath = "";

	public String getJsonPath() {
		return jsonPath;
	}

	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}
}
