package de.hpi.bpt.chimera.execution.exception;

import de.hpi.bpt.chimera.execution.DataObject;

@SuppressWarnings("serial")
public class IllegalDataObjectUnlockException extends IllegalArgumentException {
	private static final String ERROR_MESSAGE = "Try to unlock the data object with id: %s, that is already unlocked.";

	public IllegalDataObjectUnlockException(DataObject dataObject) {
		super(String.format(ERROR_MESSAGE, dataObject.getId()));
	}

}
