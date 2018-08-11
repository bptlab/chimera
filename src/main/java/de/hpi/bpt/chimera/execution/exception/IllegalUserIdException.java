package de.hpi.bpt.chimera.execution.exception;

@SuppressWarnings("serial")
public class IllegalUserIdException extends IllegalArgumentException {
	private static final String ERROR_MESSAGE = "The case id: %s is not assigned";

	public IllegalUserIdException(String userId) {
		super(String.format(ERROR_MESSAGE, userId));
	}
}
