package de.hpi.bpt.chimera.rest.beans.usermanagement;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.usermanagment.User;

@XmlRootElement
public class MemberOverviewJaxBean {
	private String id;
	private String name;

	public MemberOverviewJaxBean(User user) {
		setId(user.getId());
		setName(user.getName());
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
