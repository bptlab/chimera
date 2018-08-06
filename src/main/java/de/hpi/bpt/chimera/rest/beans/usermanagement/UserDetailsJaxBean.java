package de.hpi.bpt.chimera.rest.beans.usermanagement;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.usermanagment.MemberRole;
import de.hpi.bpt.chimera.usermanagment.Organization;
import de.hpi.bpt.chimera.usermanagment.User;

@XmlRootElement
public class UserDetailsJaxBean {
	private String name;
	private String id;
	private String[] roles;

	public UserDetailsJaxBean(Organization org, User user) {
		setName(user.getName());
		setId(user.getId());
		List<MemberRole> memberRoles = org.getUserIdToRoles().get(user.getId());
		setRoles(memberRoles.stream().map(MemberRole::getName).toArray(String[]::new));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}
}
