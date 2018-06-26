package de.hpi.bpt.chimera.usermanagment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hpi.bpt.chimera.model.CaseModel;

public class Organization {

	private String id;
	private String name;
	private User owner;
	private Map<String, User> members;
	private Map<String, CaseModel> caseModels;

	public Organization(User owner, String name) {
		this.id = UUID.randomUUID().toString().replace("-", "");
		this.name = name;
		this.owner = owner;
		this.members = new HashMap<>();
		this.members.put(owner.getId(), owner);
		this.caseModels = new HashMap<>();
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

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
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
}
