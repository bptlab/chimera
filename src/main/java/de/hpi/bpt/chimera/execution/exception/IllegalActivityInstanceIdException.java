package de.hpi.bpt.chimera.execution.exception;

public class IllegalActivityInstanceIdException extends IllegalArgumentException {
	
	private static String errorMessage = "";
	
	public IllegalActivityInstanceIdException(String activityInstanceId) {
		super(String.format(errorMessage, activityInstanceId));
	}

	public static String getErrorMessage() {
		return errorMessage;
	}
}
