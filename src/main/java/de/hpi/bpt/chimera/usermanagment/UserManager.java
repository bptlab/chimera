package de.hpi.bpt.chimera.usermanagment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
	 * @param password
	 * @return
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
		OrganizationManager.assignMember(user, OrganizationManager.getDefaultOrganization());
		log.info(String.format("Created user with id %s and name %s", id, user.getName()));
		return user;
	}

	public static User getUserById(String userId) {
		if (users.containsKey(userId)) {
			return users.get(userId);
		}
		String mess = String.format("The user with id %s does not exist", userId);
		throw new IllegalArgumentException(mess);
	}

	/**
	 * Delete a user by its id.
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

	public static List<User> getUsers() {
		return new ArrayList<>(users.values());
	}
}
