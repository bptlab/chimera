package de.hpi.bpt.chimera.parser.fragment.bpmn;

/**
 * 
 * Exception for SequenceFlows which aren't resolved. So this exception is
 * thrown, when there is a SequenceFlow Object, which doesn't have references to
 * the 2 Activities/Events which are connected by the SequenceFlow.
 *
 */
@SuppressWarnings("serial")
public class UnresolvedSequenceFlowException extends RuntimeException {
	public UnresolvedSequenceFlowException() {
		super();
	}

	public UnresolvedSequenceFlowException(String message) {
		super(message);
	}
}
