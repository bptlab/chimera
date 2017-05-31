package de.hpi.bpt.chimera.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.hpi.bpt.chimera.model.Nameable;
import de.hpi.bpt.chimera.parser.IllegalCaseModelException;

public class NameValidator {
	private NameValidator() {
	}

	/**
	 * Check if name contains only unicode letters, numbers or spaces (' ').
	 * 
	 * @param name
	 */
	public static void validateName(String name) {
		if (!StringUtils.isAlphanumericSpace(name)) {
			throw new IllegalCaseModelException(String.format("%s is not a valid name", name));
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
				throw new IllegalCaseModelException(String.format("%s occurs more than once", name));
			}
			names.add(name);
		}
	}
}
