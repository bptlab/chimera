package de.hpi.bpt.chimera.execution.exception;

@SuppressWarnings("serial")
public class IllegalFragmentPolicyNameException extends IllegalArgumentException {
	private static final String ERROR_MESSAGE = "The fragment policy name: %s does not exist";

	public IllegalFragmentPolicyNameException(String stateName) {
		super(String.format(ERROR_MESSAGE, stateName));
	}
}