package de.hpi.bpt.chimera.parser.fragment.bpmn;

@SuppressWarnings("serial")
public class UnresolvedSequenceFlowException extends RuntimeException {
	public UnresolvedSequenceFlowException() {
		super();
	}

	public UnresolvedSequenceFlowException(String message) {
		super(message);
	}
}
