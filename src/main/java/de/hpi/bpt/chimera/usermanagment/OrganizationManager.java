package de.hpi.bpt.chimera.usermanagment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.model.CaseModel;

public class OrganizationManager {
	private static Logger log = Logger.getLogger(OrganizationManager.class);
	private static Map<String, Organization> organizations = new HashMap<>();
	private static Organization defaultOrganization;

	private OrganizationManager() {
	}

	/**
	 * Create an organization with a specific name. The {@code user} will be the
	 * owner of the organization.
	 * 
	 * @param user
	 *            - User who creates the organization
	 * @param name
	 *            - for the organization that will be created
	 */
	public static Organization createOrganization(User user, String name) {
		Organization organization = new Organization(user, name);
		String id = organization.getId();
		organizations.put(id, organization);
		user.getOrganizations().add(organization);
		log.info(String.format("User with id %s and name %s created organization with id %s and name %s", user.getId(), user.getName(), id, organization.getName()));
		return organization;
	}

	/**
	 * Receive an {@link Organization} by its id.
	 * 
	 * @param orgId
	 *            - id of the organization
	 * @return the organization if the id is assigned.
	 * @throws IllegalArgumentException
	 *             if the id is not assigned.
	 */
	public static Organization getOrganizationById(String orgId) {
		if (organizations.containsKey(orgId)) {
			return organizations.get(orgId);
		}
		throw new IllegalArgumentException(String.format("Organization id %s is not assigned.", orgId));
	}

	/**
	 * Delete an organization. It is not possible to delete the default
	 * organization. All members are unassigned from this organization.
	 * 
	 * @param org
	 *            - the organization that will be deleted
	 * @throws IllegalArgumentException
	 *             if the organization that should be deleted is the
	 *             {@code defaultOrganization}.
	 */
	public static void deleteOrganization(Organization org) {
		if (org == defaultOrganization) {
			throw new IllegalArgumentException("The default organization cannot be deleted.");
		}
		for (User member : org.getMembers().values()) {
			member.getOrganizations().remove(org);
		}
		organizations.remove(org.getId());
	}

	/**
	 * Assign a specific case model to an organization.
	 * 
	 * @param organization
	 * @param cm
	 */
	public static void assignCaseModel(CaseModel cm, Organization organization) {
		Map<String, CaseModel> assignedCaseModels = organization.getCaseModels();
		if (assignedCaseModels.containsKey(cm.getId())) {
			throw new IllegalArgumentException(String.format("Casemodel %s is already assigned to organization %s", cm.getId(), organization.getId()));
		}

		assignedCaseModels.put(cm.getId(), cm);
		cm.setOrganization(organization);
	}

	/**
	 * Return only the {@link CaseModel casemodels} of the {@link Organization
	 * organization} that the user is allowed to see according to his
	 * {@link MemberRole member roles}.
	 * 
	 * @param org
	 * @param user
	 * @return List of casemodels that the {@code user} is allowed to access
	 */
	public static List<CaseModel> getCaseModels(Organization org, User user) {
		List<MemberRole> memberRoles = org.getUserIdToRole().get(user.getId());
		List<CaseModel> caseModels = new ArrayList<>();
		
		for (CaseModel cm : org.getCaseModels().values()) {
			for (MemberRole role : memberRoles) {
				if (cm.getAllowedRoles().contains(role)) {
					caseModels.add(cm);
					break;
				}
			}
		}

		return caseModels;
	}

	/**
	 * Assign a specific user to an organization.
	 *
	 * @param organization
	 * @param user
	 *            who will be member of the organization
	 *
	 */
	public static void assignMember(Organization organization, User user) {
		Map<String, User> members = organization.getMembers();
		String userId = user.getId();
		if (members.containsKey(userId)) {
			throw new IllegalArgumentException(String.format("User with id %s and name %s is already a assigned to the organization with id %s and name %s", userId, user.getName(), organization.getId(), organization.getName()));
		}

		members.put(userId, user);
		organization.getUserIdToRole().put(userId, new ArrayList<>());
		user.getOrganizations().add(organization);
	}

	/**
	 * Remove a member from an organization. Therefore, it will be verified if
	 * the specified user is a member of the organization. If the member is the
	 * last owner of the organization the membership cannot be deleted.
	 * 
	 * @param org
	 *            - Organization where the membership of the user will be
	 *            removed
	 * @param user
	 *            - User whose membership will be removed
	 * @throws IllegalArgumentException
	 *             if the {@code user} is the last owner or if the {@code user}
	 *             is not a member of the organization.
	 */
	public static void removeMember(Organization org, User user) {
		String userId = user.getId();
		if (org.getOwners().size() == 1 && org.getOwners().containsKey(userId)) {
			String message = String.format("The user with id %s is the last owner of the organiazion with id %s and can thus not be deleted", userId, org.getId());
			throw new IllegalArgumentException(message);
		}

		if (org.isMember(user)) {
			org.getMembers().remove(userId);
		} else if (org.isOwner(user)) {
			org.getOwners().remove(userId);
		} else {
			String message = String.format("User with id %s is not a member of organiazion with id %s", userId, org.getId());
			throw new IllegalArgumentException(message);
		}
	}

	public static Organization getDefaultOrganization() {
		if (defaultOrganization == null) {
			User chimera = new User();
			chimera.setEmail("email");
			chimera.setName("Chimera");
			chimera.setPassword("asdf");
			defaultOrganization = createOrganization(chimera, "Default");
		}
		return defaultOrganization;
	}

	/**
	 * Create a new organizational role for an organization. The name for
	 * organizational roles must be unique.
	 * 
	 * @param org
	 *            - organization that receives a new role
	 * @param name
	 *            - for the new organizational role
	 * @throws IllegalArgumentException
	 *             if the role name already exists.
	 */
	public static void addRole(Organization org, String name) {
		for (MemberRole role : org.getRoles()) {
			if (role.getName().equals(name)) {
				throw new IllegalArgumentException(String.format("The role name %s already exists.", name));
			}
		}
		
		org.getRoles().add(new MemberRole(name, org));
	}
	
	public static void deleteRole(Organization org, MemberRole role) {
		if (!role.getOrganization().equals(org)) {
			throw new IllegalArgumentException("The role does not belong to the organization");
		}

		for (String userId : org.getUserIdToRole().keySet()) {
			User user = UserManager.getUserById(userId);
			UserManager.deleteRole(user, org, role);
		}
	}

	public static void assignRole(Organization organization, User user, MemberRole role) {
		if (!organization.isMember(user)) {
			throw new IllegalArgumentException("The user is not a member of the organization");
		}

		List<MemberRole> assignedRoles = organization.getUserIdToRole().get(user.getId());
		if (assignedRoles.contains(role)) {
			throw new IllegalArgumentException(String.format("The user already has the role %s", role.getName()));
		}
		assignedRoles.add(role);
	}

	public static void addOwner(Organization organization, User newOwner) {
		if (organization.isOwner(newOwner)) {
			throw new IllegalArgumentException(String.format("The user %s is already an owner of the organization", newOwner.getName()));
		}
		if (!organization.isMember(newOwner)) {
			assignMember(organization, newOwner);
		}
		organization.getOwners().put(newOwner.getId(), newOwner);
	}

	public static void setDefaultOrganization(Organization defaultOrganization) {
		OrganizationManager.defaultOrganization = defaultOrganization;
	}
}
