package de.hpi.bpt.chimera.usermanagment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hpi.bpt.chimera.model.CaseModel;

public class Organization {
	private String id;
	private String name;
	private String description;
	private Map<String, User> owners;
	private Map<String, User> members;
	private Map<String, CaseModel> caseModels;
	private List<MemberRole> roles;
	private Map<String, List<MemberRole>> userIdToRole;

	public Organization(User owner, String name) {
		this.id = UUID.randomUUID().toString().replace("-", "");
		this.name = name;

		this.owners = new HashMap<>();
		owners.put(owner.getId(), owner);

		this.members = new HashMap<>();
		members.put(owner.getId(), owner);

		this.caseModels = new HashMap<>();
		this.roles = new ArrayList<>();
		this.userIdToRole = new HashMap<>();
	}

	public boolean isOwner(User user) {
		return owners.containsKey(user.getId());
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, User> getOwners() {
		return owners;
	}

	public void setOwners(Map<String, User> owners) {
		this.owners = owners;
	}

	public Map<String, User> getMembers() {
		return members;
	}

	public void setMembers(Map<String, User> members) {
		this.members = members;
	}

	public Map<String, CaseModel> getCaseModels() {
		return caseModels;
	}

	public void setCaseModels(Map<String, CaseModel> caseModels) {
		this.caseModels = caseModels;
	}

	public boolean isMember(User user) {
		return members.containsKey(user.getId());
	}

	public List<MemberRole> getRoles() {
		return roles;
	}

	public void setRoles(List<MemberRole> roles) {
		this.roles = roles;
	}

	public MemberRole getRole(String roleName) {
		MemberRole role = null;
		for (MemberRole orgRole : getRoles()) {
			if (orgRole.getName().equals(roleName)) {
				role = orgRole;
				break;
			}
		}
		if (role == null) {
			throw new IllegalArgumentException(String.format("The organization does not have a role with name %s", roleName));
		}

		return role;
	}

	public Map<String, List<MemberRole>> getUserIdToRole() {
		return userIdToRole;
	}

	public void setUserIdToRole(Map<String, List<MemberRole>> userIdToRole) {
		this.userIdToRole = userIdToRole;
	}
}
