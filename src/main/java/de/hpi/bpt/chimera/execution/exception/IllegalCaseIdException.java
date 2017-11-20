package de.hpi.bpt.chimera.execution.exception;

@SuppressWarnings("serial")
public class IllegalCaseIdException extends IllegalArgumentException {
	private static final String ERROR_MESSAGE = "The case id: %s is not assigned";

	public IllegalCaseIdException(String cmId) {
		super(String.format(ERROR_MESSAGE, cmId));
	}
}
