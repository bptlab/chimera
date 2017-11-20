package de.hpi.bpt.chimera.execution.exception;

@SuppressWarnings("serial")
public class IllegalStateNameException extends IllegalArgumentException {
	private static final String ERROR_MESSAGE = "The state name: %s does not exist";

	public IllegalStateNameException(String stateName) {
		super(String.format(ERROR_MESSAGE, stateName));
	}
}
