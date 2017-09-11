package de.hpi.bpt.chimera.model.fragment.bpmn;

import de.hpi.bpt.chimera.model.condition.DataObjectStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataClass;

public class DataNode {
	private String id;
	private String name;
	private DataObjectStateCondition dataObjectState;
	private String jsonPath;

	// GETTER & SETTER
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DataObjectStateCondition getDataObjectState() {
		return dataObjectState;
	}
	public void setDataObjectState(DataObjectStateCondition dataObjectState) {
		this.dataObjectState = dataObjectState;
	}

	public String getJsonPath() {
		return jsonPath;
	}

	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}


}
