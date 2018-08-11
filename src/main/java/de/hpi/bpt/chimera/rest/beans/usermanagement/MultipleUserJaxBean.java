package de.hpi.bpt.chimera.rest.beans.usermanagement;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MultipleUserJaxBean {
	private List<UserOverviewJaxBean> users;

	public MultipleUserJaxBean() {
		// Necessary for parsing from json string to java representation
	}

	public MultipleUserJaxBean(List<UserOverviewJaxBean> users) {
		this.users = users;
	}

	public List<UserOverviewJaxBean> getUsers() {
		return users;
	}

	public void setUsers(List<UserOverviewJaxBean> users) {
		this.users = users;
	}
}
