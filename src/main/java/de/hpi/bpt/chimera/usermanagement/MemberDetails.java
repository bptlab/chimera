package de.hpi.bpt.chimera.usermanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class MemberDetails {
	@Id
	private String id;
	private User user;
	@OneToMany(cascade = CascadeType.ALL)
	private List<MemberRole> roles = new ArrayList<>();

	public MemberDetails() {
		// JPA
	}

	public MemberDetails(User user) {
		this.id = UUID.randomUUID().toString().replace("-", "");
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<MemberRole> getRoles() {
		return roles;
	}

	public void setRoles(List<MemberRole> roles) {
		this.roles = roles;
	}
}
