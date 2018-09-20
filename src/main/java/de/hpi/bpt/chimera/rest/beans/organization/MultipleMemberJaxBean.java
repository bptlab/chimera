package de.hpi.bpt.chimera.rest.beans.organization;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.rest.beans.usermanagement.UserOverviewJaxBean;

@XmlRootElement
public class MultipleMemberJaxBean {
	private List<UserOverviewJaxBean> members;

	public MultipleMemberJaxBean(List<UserOverviewJaxBean> members) {
		this.members = members;
	}

	public List<UserOverviewJaxBean> getMembers() {
		return members;
	}

	public void setMembers(List<UserOverviewJaxBean> members) {
		this.members = members;
	}
}
