package de.hpi.bpt.chimera.execution.exception;

@SuppressWarnings("serial")
public class IllegalDataClassNameException extends IllegalArgumentException {
	private static final String ERROR_MESSAGE = "The data class name: %s does not exist";

	public IllegalDataClassNameException(String dataClassName) {
		super(String.format(ERROR_MESSAGE, dataClassName));
	}
}
