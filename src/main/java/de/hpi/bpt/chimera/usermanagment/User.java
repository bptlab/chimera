package de.hpi.bpt.chimera.usermanagment;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User {
	private String id;
	private String name;
	private String email;
	private String password;
	private Set<Role> roles;
	private Set<SystemRole> systemRoles;
	private Set<Organization> organizations;

	public User() {
		this.id = UUID.randomUUID().toString().replace("-", "");
		this.roles = new HashSet<>();
		this.systemRoles = new HashSet<>();
		this.organizations = new HashSet<>();
	}

	public boolean isAdmin() {
		return systemRoles.contains(SystemRole.ADMIN);
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<SystemRole> getSystemRoles() {
		return systemRoles;
	}

	public void setSystemRoles(Set<SystemRole> systemRoles) {
		this.systemRoles = systemRoles;
	}

	public Set<Organization> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(Set<Organization> organizations) {
		this.organizations = organizations;
	}
}
