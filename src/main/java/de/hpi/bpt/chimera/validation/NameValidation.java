package de.hpi.bpt.chimera.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import de.hpi.bpt.chimera.model.Nameable;

public class NameValidation {
	private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9](?![a-zA-Z0-9 _]*?[ _]{2}.*)[a-zA-Z0-9 _]*?[a-zA-Z0-9]$");
	
	private NameValidation() {
	}

	/**
	 * Check if name is valid. 
	 * Valid names start and end with an alpha-numeric character and can contain alpha-numeric characters, spaces and
	 * underscores. However, no two spaces or underscores can follow each other.
	 * Examples:
	 * "a_b_c" valid
	 * "a__z" invalid
	 * "äµ€" invalid (no unicode support)
	 * 
	 * @param name
	 */
	public static void validateName(String name) {
		if (! NAME_PATTERN.matcher(name).matches() ) {
			throw new IllegalArgumentException(String.format("%s is not a valid name", name));
		}
	}

	/**
	 * Check if name of an Nameable Object occurs more than once in a List of
	 * Nameable Objects.
	 * 
	 * @param nameables
	 *            List of nameable Objects
	 */
	public static void validateNameFrequency(List<? extends Nameable> nameables) {
		List<String> names = new ArrayList<>();

		for (Nameable nameable : nameables) {
			String name = nameable.getName();
			if (names.contains(name)) {
				throw new IllegalArgumentException(String.format("%s occurs more than once", name));
			}
			names.add(name);
		}
	}
}
