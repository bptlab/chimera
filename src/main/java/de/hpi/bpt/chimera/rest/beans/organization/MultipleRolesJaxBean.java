package de.hpi.bpt.chimera.rest.beans.organization;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.usermanagment.MemberRole;

@XmlRootElement
public class MultipleRolesJaxBean {
	private List<String> roles;

	public MultipleRolesJaxBean(List<MemberRole> memberRoles) {
		this.roles = memberRoles.stream()
				.map(MemberRole::getName)
				.collect(Collectors.toList());
	}
	
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}