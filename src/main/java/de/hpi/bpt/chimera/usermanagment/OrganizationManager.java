package de.hpi.bpt.chimera.usermanagment;

import java.util.HashMap;
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
	 * Create an organization with a specific name.
	 * 
	 * @param user
	 *            - User that will own the organization
	 * @param name
	 *            - for organization that will be created
	 */
	public static Organization createOrganization(User user, String name) {
		Organization organization = new Organization(user, name);
		String id = organization.getId();
		organizations.put(id, organization);
		user.getOrganizations().add(organization);
		log.info(String.format("User with id %s and name %s created organization with id %s and name %s", user.getId(), user.getName(), id, organization.getName()));
		return organization;
	}

	public static Organization getOrganizationById(String orgId) {
		if (organizations.containsKey(orgId)) {
			return organizations.get(orgId);
		}
		throw new IllegalArgumentException(String.format("Organization id %s is not assigned.", orgId));
	}

	/**
	 * Delete an organization. It is not possible to delete the default
	 * organization.
	 * 
	 * @param org
	 *            the organization that will be deleted
	 */
	public static void deleteOrganization(Organization org) {
		if (org == defaultOrganization) {
			throw new IllegalArgumentException("The default organization cannot be deleted.");
		}
		try {
			for (User member : org.getMembers().values()) {
				member.getOrganizations().remove(org);
			}
			organizations.remove(org.getId());
		} catch (Exception e) {
			throw e;
		}
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
	 * Assign a specific user to an organization.
	 * 
	 * @param user
	 *            who will be member of the organization
	 * @param organization
	 */
	public static void assignMember(User user, Organization organization) {
		Map<String, User> members = organization.getMembers();
		if (members.containsKey(user.getId())) {
			throw new IllegalArgumentException(String.format("User with id %s and name %s is already a assigned to the organization with id %s and name %s", user.getId(), user.getName(), organization.getId(), organization.getName()));
		}

		members.put(user.getId(), user);
		user.getOrganizations().add(organization);
	}

	public static void deleteUser(Organization org, User user) {
		// TODO: think about how to the deletion if the user is the owner of the
		// organization
		String userId = user.getId();
		if (org.getMembers().containsKey(userId)) {
			org.getMembers().remove(userId);
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

	public static void addRole(Organization org, String name) {
		if (org.getRoles().contains(name)) {
			throw new IllegalArgumentException(String.format("The role name %s already exists.", name));
		}
		
		org.getRoles().add(name);
	}
	
	public static void addOwner(Organization organization, User newOwner) {
		if (organization.getOwners().containsKey(newOwner.getId())) {
			throw new IllegalArgumentException(String.format("The user %s is already an owner of the organization", newOwner.getName()));
		}

		organization.getOwners().put(newOwner.getId(), newOwner);
	}

	public static void setDefaultOrganization(Organization defaultOrganization) {
		OrganizationManager.defaultOrganization = defaultOrganization;
	}
}
