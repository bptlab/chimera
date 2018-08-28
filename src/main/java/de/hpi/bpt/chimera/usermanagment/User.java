package de.hpi.bpt.chimera.usermanagment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "User.getAll", query = "SELECT u FROM User u")
public class User {

	@Id
	private String id;
	private String name;
	private String email;
	private String password;
	@ElementCollection(targetClass = SystemRole.class)
	@Enumerated(EnumType.STRING)
	private List<SystemRole> systemRoles;

	@ManyToMany(mappedBy = "members")
	private List<Organization> organizations;

	public User() {
		this.id = UUID.randomUUID().toString().replace("-", "");
		this.systemRoles = new ArrayList<>();
		this.organizations = new ArrayList<>();
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

	public List<SystemRole> getSystemRoles() {
		return systemRoles;
	}

	public void setSystemRoles(List<SystemRole> systemRoles) {
		this.systemRoles = systemRoles;
	}

	public List<Organization> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<Organization> organizations) {
		this.organizations = organizations;
	}

	public void addOrganization(Organization organization) {
		organizations.add(organization);
	}
}
