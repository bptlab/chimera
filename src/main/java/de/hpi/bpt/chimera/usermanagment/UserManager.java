package de.hpi.bpt.chimera.usermanagment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

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

		for (User user : users.values()) {
			if (user.getEmail().equals(email)) {
				if (user.getPassword().equals(password)) {
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
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setName(username);
		String id = user.getId();
		users.put(id, user);
		OrganizationManager.assignMember(OrganizationManager.getDefaultOrganization(), user);
		log.info(String.format("Created user with id %s and name %s", id, user.getName()));
		return user;
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
		String mess = String.format("The user with id %s does not exist", userId);
		throw new IllegalArgumentException(mess);
	}

	/**
	 * Delete a user. Therefore, delete the memberships in the organizations.
	 * 
	 * @param user
	 */
	public static void deleteUser(User user) {
		try {
			String name = users.get(user).getName();
			for (Organization org : user.getOrganizations()) {
				OrganizationManager.removeMember(org, user);
			}

			users.remove(user);
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
		List<MemberRole> roles = org.getUserIdToRole().get(user.getId());
		roles.remove(role);
	}

	public static List<User> getUsers() {
		return new ArrayList<>(users.values());
	}
}
