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
	 * @param password
	 * @return
	 */
	public static User authenticateUser(String email, String password) {
		if (users.isEmpty()) {
			createUser("admin", "admin", "admin");
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
	public static void createUser(String email, String password, String username) {
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setName(username);
		String id = user.getId();
		users.put(id, user);
		OrganizationManager.assignMember(user, OrganizationManager.getDefaultOrganization());
		log.info(String.format("Created user with id %s and name %s", id, user.getName()));
	}

	/**
	 * Delete a user by its id.
	 * 
	 * @param id
	 */
	public static void deleteUser(String id) {
		if (users.containsKey(id)) {
			String name = users.get(id).getName();
			users.remove(id);
			log.info(String.format("Deleted user with id %s and name %s", id, name));
		} else {
			throw new IllegalArgumentException(String.format("User with id %s does not exist.", id));
		}
	}

	public static List<User> getUsers() {
		return new ArrayList<>(users.values());
	}
}
