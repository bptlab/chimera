package de.hpi.bpt.chimera.usermanagment;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class MemberRole {
	@Id
	// @GeneratedValue(strategy = GenerationType.TABLE)
	private String dbId;
	private String name;
	@OneToOne
	private Organization organization;

	public MemberRole() {
		// JPA
	}

	public MemberRole(String name, Organization organization) {
		this.dbId = UUID.randomUUID().toString().replace("-", "");
		this.name = name;
		this.organization = organization;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof MemberRole)) {
			return false;
		}

		MemberRole role = (MemberRole) o;
		return role.getOrganization().equals(organization) && role.getName().equals(name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(organization, name);
	}
}
