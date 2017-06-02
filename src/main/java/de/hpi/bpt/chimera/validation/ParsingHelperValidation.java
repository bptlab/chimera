package de.hpi.bpt.chimera.validation;

import java.util.List;
import java.util.Map;

import de.hpi.bpt.chimera.model.Listable;
import de.hpi.bpt.chimera.model.Nameable;

public class ParsingHelperValidation {

	private ParsingHelperValidation() {
	}

	/**
	 * Validate whether a Map from String to a Listable Object contains String
	 * toCheck as a key.
	 * 
	 * @param mapToCheck
	 * @param toCheck
	 */
	public static void validateMap(Map<String, ? extends Listable> mapToCheck, String toCheck) {
		if (!mapToCheck.containsKey(toCheck)) {
			throw new IllegalArgumentException(String.format("%s does not exist", toCheck));
		}
	}

	/**
	 * Validate whether a List of Listable Objects contains objectToCheck.
	 * 
	 * @param listToCheck
	 * @param objectToCheck
	 */
	public static void validateList(List<? extends Nameable> listToCheck, Nameable objectToCheck) {
		if (!listToCheck.contains(objectToCheck)) {
			throw new IllegalArgumentException(String.format("%s does not exist", objectToCheck.toString()));
		}
	}

	/**
	 * Validate whether an Object is instance of certain Class.
	 * 
	 * @param object
	 * @param clazz
	 */
	public static void validateType(Nameable object, @SuppressWarnings("rawtypes") Class clazz) {
		if (!(clazz.isInstance(object))) {
			throw new IllegalArgumentException(String.format("%s is not of type %s", object.getName(), clazz.getSimpleName()));
		}
	}
}
