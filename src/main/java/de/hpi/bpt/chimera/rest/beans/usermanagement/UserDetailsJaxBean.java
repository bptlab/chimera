package de.hpi.bpt.chimera.rest.beans.usermanagement;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.usermanagment.MemberRole;
import de.hpi.bpt.chimera.usermanagment.Organization;
import de.hpi.bpt.chimera.usermanagment.User;

@XmlRootElement
public class UserDetailsJaxBean {
	private String name;
	private String[] roles;

	public UserDetailsJaxBean(Organization org, User user) {
		setName(user.getName());
		List<MemberRole> role = org.getUserIdToRole().get(user.getId());
		String[] roleNames = (String[]) role.stream()
										.map(MemberRole::getName)
										.toArray();
		setRoles(roleNames);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getRoles() {
		return roles;
	}
	public void setRoles(String[] roles) {
		this.roles = roles;
	}


}
