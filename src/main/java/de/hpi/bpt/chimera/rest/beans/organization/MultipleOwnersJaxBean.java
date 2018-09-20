package de.hpi.bpt.chimera.rest.beans.organization;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.rest.beans.usermanagement.UserOverviewJaxBean;

@XmlRootElement
public class MultipleOwnersJaxBean {
	private List<UserOverviewJaxBean> owners;

	public MultipleOwnersJaxBean(List<UserOverviewJaxBean> owners) {
		this.owners = owners;
	}

	public List<UserOverviewJaxBean> getOwners() {
		return owners;
	}

	public void setOwners(List<UserOverviewJaxBean> owners) {
		this.owners = owners;
	}
}
