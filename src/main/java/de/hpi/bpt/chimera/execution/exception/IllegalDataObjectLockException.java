package de.hpi.bpt.chimera.execution.exception;

import de.hpi.bpt.chimera.execution.DataObject;

@SuppressWarnings("serial")
public class IllegalDataObjectLockException extends IllegalArgumentException {
	private static final String ERROR_MESSAGE = "Try to lock the data object with id: %s, that is already locked.";

	public IllegalDataObjectLockException(DataObject dataObject) {
		super(String.format(ERROR_MESSAGE, dataObject.getId()));
	}

}
