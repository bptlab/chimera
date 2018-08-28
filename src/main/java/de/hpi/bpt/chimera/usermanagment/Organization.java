package de.hpi.bpt.chimera.usermanagment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.model.CaseModel;

@Entity
@NamedQuery(name = "Organization.getAll", query = "SELECT o FROM Organization o")
public class Organization {
	private static final Logger log = Logger.getLogger(Organization.class);

	@Id
	// @GeneratedValue(strategy = GenerationType.TABLE)
	private String id;
	private String name;
	private String description;

	@OneToMany
	@JoinTable(name = "Organization_Owners")
	@MapKey(name = "id")
	private Map<String, User> owners;

	// TODO: theoretically it is possible to put a CascadeType.MERGE here and it
	// should not be necessary to save Users extra because every User is in the
	// Default Organization. But does it take longer?
	@ManyToMany
	@JoinTable(name = "Organization_Members")
	@MapKey(name = "id")
	private Map<String, User> members;

	@OneToMany
	@MapKey(name = "cmId")
	private Map<String, CaseModel> caseModels;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "Organization_AvailableRoles")
	private List<MemberRole> roles;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "Organization_UserRoles")
	private Map<String, MemberDetails> userIdToMemberDetails;

	public Organization() {
		this.owners = new HashMap<>();
		this.members = new HashMap<>();
		this.caseModels = new HashMap<>();
		this.roles = new ArrayList<>();
		this.userIdToMemberDetails = new HashMap<>();
	}

	public Organization(User owner, String name) {
		this.id = UUID.randomUUID().toString().replace("-", "");
		this.name = name;
		this.description = "";

		this.owners = new HashMap<>();
		owners.put(owner.getId(), owner);

		this.members = new HashMap<>();
		members.put(owner.getId(), owner);

		this.caseModels = new HashMap<>();
		this.roles = new ArrayList<>();
		this.userIdToMemberDetails = new HashMap<>();
		userIdToMemberDetails.put(owner.getId(), new MemberDetails(owner));
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

	public boolean isSoleOwner(User user) {
		return owners.size() == 1 && owners.containsKey(user.getId());
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

	public Map<String, MemberDetails> getUserIdToMemberDetails() {
		return userIdToMemberDetails;
	}

	public void setUserIdToMemberDetails(Map<String, MemberDetails> userIdToMemberDetails) {
		this.userIdToMemberDetails = userIdToMemberDetails;
	}

	public List<MemberRole> getMemberRoles(User user) {
		return userIdToMemberDetails.get(user.getId()).getRoles();
	}

	public void addMember(User user) {
		members.put(user.getId(), user);
		log.info(members.size());
		userIdToMemberDetails.put(user.getId(), new MemberDetails(user));
	}
}
