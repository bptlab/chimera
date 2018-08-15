package de.hpi.bpt.chimera.usermanagment;

import java.util.Objects;

public class MemberRole {
	private String name;
	private Organization organization;

	public MemberRole(String name, Organization organization) {
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
