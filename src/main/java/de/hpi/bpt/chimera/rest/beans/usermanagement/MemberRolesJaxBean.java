package de.hpi.bpt.chimera.rest.beans.usermanagement;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.usermanagment.MemberRole;

@XmlRootElement
public class MemberRolesJaxBean {
	private String[] roles;

	public MemberRolesJaxBean(List<MemberRole> memberRoles) {
		roles = (String[]) memberRoles.stream().map(MemberRole::getName).toArray();
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

}
