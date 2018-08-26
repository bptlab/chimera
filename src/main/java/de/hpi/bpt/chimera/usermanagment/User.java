package de.hpi.bpt.chimera.usermanagment;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

@Entity
@NamedQuery(name = "User.getAll", query = "SELECT u FROM User u")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private String id;
	private String name;
	private String email;
	private String password;
	@ElementCollection(targetClass = SystemRole.class)
	@Enumerated(EnumType.STRING)
	private Set<SystemRole> systemRoles;
	@Transient
	private Set<Organization> organizations;

	public User() {
		// this.id = UUID.randomUUID().toString().replace("-", "");
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
