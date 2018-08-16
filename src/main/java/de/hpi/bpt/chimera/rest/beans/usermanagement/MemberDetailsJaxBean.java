package de.hpi.bpt.chimera.rest.beans.usermanagement;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.usermanagment.MemberRole;
import de.hpi.bpt.chimera.usermanagment.Organization;
import de.hpi.bpt.chimera.usermanagment.User;

@XmlRootElement
public class MemberDetailsJaxBean {
	private String name;
	private String id;
	private List<String> roles;

	public MemberDetailsJaxBean(Organization org, User user) {
		setName(user.getName());
		setId(user.getId());
		List<String> memberRoles = org.getUserIdToRoles().get(user.getId()).stream()
									.map(MemberRole::getName)
									.collect(Collectors.toList());
		setRoles(memberRoles);
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

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
