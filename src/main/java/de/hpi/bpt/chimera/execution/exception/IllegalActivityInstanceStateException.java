package de.hpi.bpt.chimera.execution.exception;

import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;

@SuppressWarnings("serial")
public class IllegalActivityInstanceStateException extends IllegalArgumentException {

	private static final String ERROR_MESSAGE = "The activity instance with id: %s is in state: %s but not in the expected state: %s";

	public IllegalActivityInstanceStateException(AbstractActivityInstance activityInstance, State expectedState) {
		super(String.format(ERROR_MESSAGE, activityInstance.getId(), activityInstance.getState().toString(), expectedState.toString()));
	}
}
