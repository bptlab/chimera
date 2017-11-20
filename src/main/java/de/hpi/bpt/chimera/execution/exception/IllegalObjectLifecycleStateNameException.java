package de.hpi.bpt.chimera.execution.exception;

import de.hpi.bpt.chimera.model.datamodel.DataClass;

@SuppressWarnings("serial")
public class IllegalObjectLifecycleStateNameException extends IllegalArgumentException {
	private static final String ERROR_MESSAGE = "The objectlifecycle state name: %s does not exist in data class: %s";

	public IllegalObjectLifecycleStateNameException(DataClass dataClass, String olcName) {
		super(String.format(ERROR_MESSAGE, olcName, dataClass.getName()));
	}

}
