package de.hpi.bpt.chimera.rest.beans.usermanagement;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.usermanagement.User;

@XmlRootElement
public class UserOverviewJaxBean {
	private String id;
	private String name;
	private String email;

	public UserOverviewJaxBean(User user) {
		setId(user.getId());
		setName(user.getName());
		setEmail(user.getEmail());
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
