package de.hpi.bpt.chimera.usermanagment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.exception.IllegalUserIdException;

public class UserManager {
	private static Logger log = Logger.getLogger(UserManager.class);
	private static Map<String, User> users = new HashMap<>();

	private UserManager() {
	}

	/**
	 * Authenticate a user with a given email and a password.
	 * 
	 * @param email
	 *            - of the user
	 * @param password
	 *            - of the user
	 * @return the authenticated user
	 * @throws IllegalArgumentException
	 *             if the email is not assigned or the password is wrong.
	 */
	public static User authenticateUser(String email, String password) {
		if (users.isEmpty()) {
			User admin = createUser("admin", "admin", "admin");
			admin.getSystemRoles().add(SystemRole.ADMIN);
		}
		
		String hashedPassword = hashPassword(password);
		for (User user : users.values()) {
			if (user.getEmail().equals(email)) {
				if (user.getPassword().equals(hashedPassword)) {
					return user;
				} else {
					throw new IllegalArgumentException("Wrong password");
				}
			}
		}
		throw new IllegalArgumentException("Email not assigned");
	}

	/**
	 * Create a user with a specific name and assign it to the default
	 * organization.
	 * 
	 * @param name
	 * @param username
	 * @param password
	 */
	public static User createUser(String email, String password, String username) {
		// TODO: validate email, password, username
		User user = new User();
		user.setEmail(email);
		String hashedPassword = hashPassword(password);
		user.setPassword(hashedPassword);
		user.setName(username);
		String id = user.getId();
		users.put(id, user);
		OrganizationManager.assignMember(OrganizationManager.getDefaultOrganization(), user);
		log.info(String.format("Created user with id %s and name %s", id, user.getName()));
		return user;
	}

	/**
	 * Assign a new email and a new password to a a user.
	 * 
	 * @param user
	 * @param newEmail
	 * @param newPassword
	 */
	public static void updateUser(User user, String newEmail, String newPassword) {
		// TODO: validate email, password
		user.setEmail(newEmail);
		user.setPassword(hashPassword(newPassword));
	}

	private static String hashPassword(String password) {
		// TODO: think about salt for the hashing
		return String.valueOf(Objects.hashCode(password));
	}

	/**
	 * Get a {@link User} by its id.
	 * 
	 * @param userId
	 * @return User
	 * @throws IllegalArgumentException
	 *             if the user is not assigned.
	 */
	public static User getUserById(String userId) {
		if (users.containsKey(userId)) {
			return users.get(userId);
		}

		throw new IllegalUserIdException(userId);
	}

	/**
	 * Delete an user. Therefore, delete the memberships in the organizations.
	 * An user can only be deleted if it the only owner of one organization.
	 * 
	 * @param user
	 *            - the user to be deleted
	 */
	public static void deleteUser(User user) {
		try {
			String userId = user.getId();
			String name = user.getName();

			for (Organization org : user.getOrganizations()) {
				if (org.isSoleOwner(user)) {
					String message = String.format("The user with id %s is the last owner of the organiazion with id %s and can thus not be deleted", userId, org.getId());
					throw new IllegalArgumentException(message);
				}
			}

			for (Organization org : user.getOrganizations()) {
				OrganizationManager.removeMember(org, user);
			}

			users.remove(user.getId());
			log.info(String.format("Deleted user with id %s and name %s", user, name));
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Remove a specific {@link MemberRole} of a user in an organization.
	 * 
	 * @param user
	 *            - whose role will me removed
	 * @param org
	 *            - organization where the users role will be removed
	 * @param role
	 *            - to be removed
	 */
	public static void deleteRole(User user, Organization org, MemberRole role) {
		if (!org.isMember(user)) {
			throw new IllegalArgumentException("The specified user is not a member of the organization");
		}
		List<MemberRole> roles = org.getUserIdToRoles().get(user.getId());
		roles.remove(role);
	}

	public static List<User> getUsers() {
		return new ArrayList<>(users.values());
	}
}
