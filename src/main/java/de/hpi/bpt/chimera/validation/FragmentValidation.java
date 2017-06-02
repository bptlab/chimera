package de.hpi.bpt.chimera.validation;

public class FragmentValidation {

	private FragmentValidation() {
	}

	/**
	 * Validate that the amount of Fragments in a CaseModel is not zero.
	 * 
	 * @param fragmentAmount
	 */
	public static void validateFragmentAmount(int fragmentAmount) {
		if (fragmentAmount == 0) {
			throw new IllegalArgumentException("no fragments specified");
		}
	}
}
