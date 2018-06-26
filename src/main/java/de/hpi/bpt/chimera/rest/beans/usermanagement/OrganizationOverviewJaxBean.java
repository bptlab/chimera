package de.hpi.bpt.chimera.rest.beans.usermanagement;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.usermanagment.Organization;

@XmlRootElement
public class OrganizationOverviewJaxBean {
	private String id;
	private String name;

	public OrganizationOverviewJaxBean(Organization organization) {
		setId(organization.getId());
		setName(organization.getName());
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
}
