package de.hpi.bpt.chimera.rest.beans.organization;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UpdateOrganizationJaxBean {
	private String name;
	private String description;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
