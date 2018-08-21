package de.hpi.bpt.chimera.execution.exception;

@SuppressWarnings("serial")
public class IllegalControlNodeInstanceIdException extends IllegalIdentifierException {
	
	private static final String ERROR_MESSAGE = "The control node instance id: %s is not assigned";
	
	public IllegalControlNodeInstanceIdException(String activityInstanceId) {
		super(String.format(ERROR_MESSAGE, activityInstanceId));
	}
}
