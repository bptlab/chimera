package de.hpi.bpt.chimera.usermanagment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.exception.IllegalOrganizationIdException;
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
		throw new IllegalOrganizationIdException(orgId);
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
	 * {@link MemberRole member roles}. If a casemodels has no allowed roles
	 * specified every member is allowed to see it. The owners of an
	 * organizazion are allowed to see all casemodels. The casemodels will be
	 * ordered by the date of deployment.
	 * 
	 * @param org
	 * @param user
	 * @return List of casemodels that the {@code user} is allowed to access
	 */
	public static List<CaseModel> getCaseModels(Organization org, User user) {
		List<MemberRole> memberRoles = org.getUserIdToRoles().get(user.getId());
		List<CaseModel> caseModels = new ArrayList<>();

		if (org.isOwner(user)) {
			return new ArrayList<>(org.getCaseModels().values());
		}

		for (CaseModel cm : org.getCaseModels().values()) {
			if (cm.getAllowedRoles().isEmpty()) {
				caseModels.add(cm);
				continue;
			}
			for (MemberRole role : memberRoles) {
				if (cm.getAllowedRoles().contains(role)) {
					caseModels.add(cm);
				}
			}
		}

		caseModels.sort(Comparator.comparing(CaseModel::getDeployment));
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
		organization.getUserIdToRoles().put(userId, new ArrayList<>());
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
		if (org.isSoleOwner(user)) {
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

	/**
	 * Receive the default organization. If it does not exist, it will be
	 * created.
	 * 
	 * @return {@link Organization}
	 */
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
	
	/**
	 * Delete a role of an organization. The role will be removed from all
	 * members of the organization who have this role.
	 * 
	 * @param org
	 *            - {@link Organization} of which the role will be deleted
	 * @param role
	 *            - that will be deleted
	 * @throws IllegalArgumentException
	 *             if the role does not belong to the organization.
	 */
	public static void deleteRole(Organization org, MemberRole role) {
		if (!role.getOrganization().equals(org)) {
			throw new IllegalArgumentException("The role does not belong to the organization");
		}

		for (String userId : org.getUserIdToRoles().keySet()) {
			User user = UserManager.getUserById(userId);
			UserManager.deleteRole(user, org, role);
		}

		org.getRoles().remove(role);
	}

	/**
	 * Assign a role of an organization to a member of the organization.
	 * 
	 * @param organization
	 *            - in which the user is member in and where the organizational
	 *            role belongs to
	 * @param user
	 *            - who will receive the role
	 * @param role
	 *            - that will be assigned
	 * @throws IllegalArgumentException
	 *             if the user is not a member of the organization or the user
	 *             already has this role.
	 */
	public static void assignRole(Organization organization, User user, MemberRole role) {
		if (!organization.isMember(user)) {
			throw new IllegalArgumentException("The user is not a member of the organization");
		}

		List<MemberRole> assignedRoles = organization.getUserIdToRoles().get(user.getId());
		if (assignedRoles.contains(role)) {
			throw new IllegalArgumentException(String.format("The user already has the role %s", role.getName()));
		}
		assignedRoles.add(role);
	}

	/**
	 * Add a new owner for an organization. The owner will be also assigned as a
	 * member of the organization.
	 * 
	 * @param organization
	 *            - that will receive a new owner
	 * @param newOwner
	 *            - for the organization
	 * @throws IllegalArgumentException
	 *             if the user is already an owner of the organization.
	 */
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
