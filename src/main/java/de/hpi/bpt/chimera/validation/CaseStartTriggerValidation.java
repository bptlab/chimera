package de.hpi.bpt.chimera.validation;

public class CaseStartTriggerValidation {

	private CaseStartTriggerValidation() {
	}

	/**
	 * Validate that a CaseStartTriggerConsequence has a specified mapping of
	 * DataAttribute and a JsonPath.
	 * 
	 * @param arraySize
	 */
	public static void validateMappingAmount(int arraySize) {
		if (arraySize == 0) {
			throw new IllegalArgumentException("no mapping specified");
		}
	}
}
