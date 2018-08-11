package de.hpi.bpt.chimera.rest.beans.organization;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MultipleOrganizationsOverviewJaxBean {
	private List<OrganizationOverviewJaxBean> organizations;

	public MultipleOrganizationsOverviewJaxBean() {
		// Necessary for parsing from json string to java representation
	}

	public MultipleOrganizationsOverviewJaxBean(List<OrganizationOverviewJaxBean> organizations) {
		this.organizations = organizations;
	}

	public List<OrganizationOverviewJaxBean> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<OrganizationOverviewJaxBean> organizations) {
		this.organizations = organizations;
	}

}
