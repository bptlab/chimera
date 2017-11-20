package de.hpi.bpt.chimera.execution.exception;

import de.hpi.bpt.chimera.execution.ControlNodeInstance;

@SuppressWarnings("serial")
public class IllegalControlNodeInstanceTypeException extends IllegalArgumentException {

	private static final String ERROR_MESSAGE = "The control node instance with id: %s is of type: %s but not of the expected type: %s";

	public IllegalControlNodeInstanceTypeException(ControlNodeInstance controlNodeInstance, Class<?> expectedClass) {
		super(String.format(ERROR_MESSAGE, controlNodeInstance.getId(), controlNodeInstance.getClass().getSimpleName(), expectedClass.getSimpleName()));

	}
}
