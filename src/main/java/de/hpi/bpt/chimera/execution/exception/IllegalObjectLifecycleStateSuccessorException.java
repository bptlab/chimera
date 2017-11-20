package de.hpi.bpt.chimera.execution.exception;

import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

@SuppressWarnings("serial")
public class IllegalObjectLifecycleStateSuccessorException extends IllegalArgumentException {
	private static final String ERROR_MESSAGE = "The objectlifecycle state: %s is not a valid successor of: %s in the objectlifecycle of data class: %s";

	public IllegalObjectLifecycleStateSuccessorException(DataClass dataClass, ObjectLifecycleState predecessor, ObjectLifecycleState invalidSuccesor) {
		super(String.format(ERROR_MESSAGE, invalidSuccesor.getName(), predecessor.getName(), dataClass.getName()));
	}
}
