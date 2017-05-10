package de.hpi.bpt.chimera.model.datamodel;

import java.util.List;

public class DataModelClass {
	private String name;
	private List<DataAttribute> attributes;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<DataAttribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<DataAttribute> attributes) {
		this.attributes = attributes;
	}

}
