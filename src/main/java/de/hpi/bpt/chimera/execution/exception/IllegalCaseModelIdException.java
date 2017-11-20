package de.hpi.bpt.chimera.execution.exception;

@SuppressWarnings("serial")
public class IllegalCaseModelIdException extends IllegalArgumentException {

	private static final String ERROR_MESSAGE = "The casemodel id: %s is not assigned";

	public IllegalCaseModelIdException(String cmId) {
		super(String.format(ERROR_MESSAGE, cmId));
	}
}
