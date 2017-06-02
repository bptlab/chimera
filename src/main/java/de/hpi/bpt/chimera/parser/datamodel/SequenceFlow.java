package de.hpi.bpt.chimera.parser.datamodel;

/**
 * Assisting container to store source-id and target-id of a SequenceFlow.
 */
public class SequenceFlow {
	private String source;
	private String target;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
}
