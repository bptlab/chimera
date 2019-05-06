package de.hpi.bpt.chimera.execution.exception;

import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

public class IllegalDataObjectCreationException extends IllegalArgumentException {
    private static final String ERROR_MESSAGE = "A data object of class %s cannot be created in state %s as it is not an initial state.";
    public IllegalDataObjectCreationException(DataClass dataClass, ObjectLifecycleState state) {
        super(String.format(ERROR_MESSAGE, dataClass.getName(), state.getName()));
    }

}
