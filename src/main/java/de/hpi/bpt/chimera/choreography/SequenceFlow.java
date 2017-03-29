/**
 * 
 */
package de.hpi.bpt.chimera.choreography;

import java.util.UUID;

/**
 * @author adriatik.nikaj
 *
 */
public class SequenceFlow {
	

	protected UUID sequenceFlowId;
	
	protected FlowNode target;
	
	protected FlowNode source;
	
	protected String name;

	public void setName(String value) {
		this.name = value;
	}

	public void setId(UUID id) {
		sequenceFlowId = id;
	}

	public FlowNode getTarget() {
		return target;
	}
	
	public void setTarget(FlowNode target) {
		this.target = target;
	}
	
	public FlowNode getSource() {
		return source;
	}
	
	public void setSource(FlowNode source) {
		this.source = source;
	}
	
	public UUID getSequenceFlowId() {
		return sequenceFlowId;
	}
	
	public String getName() {
		return name;
	}
}
