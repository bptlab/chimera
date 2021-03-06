package de.hpi.bpt.chimera.rest.beans.organization;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.usermanagement.Organization;

@XmlRootElement
public class OrganizationOverviewJaxBean {
	private String id;
	private String name;
	private String description;

	public OrganizationOverviewJaxBean(Organization organization) {
		setId(organization.getId());
		setName(organization.getName());
		setDescription(organization.getDescription());
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
