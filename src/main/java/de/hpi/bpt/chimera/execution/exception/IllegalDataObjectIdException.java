package de.hpi.bpt.chimera.execution.exception;

@SuppressWarnings("serial")
public class IllegalDataObjectIdException extends IllegalArgumentException {

	private static final String ERROR_MESSAGE = "The data object id: %s is not assigned";

	public IllegalDataObjectIdException(String dataObjectId) {
		super(String.format(ERROR_MESSAGE, dataObjectId));
	}
}
