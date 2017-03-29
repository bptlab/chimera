/**
 * 
 */
package de.hpi.bpt.chimera.choreography;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author adriatik.nikaj
 *
 */
public abstract class FlowNode {

	protected UUID Id;
	protected String name;
	protected List<SequenceFlow> incomingFlows = new ArrayList<SequenceFlow>();
	protected List<SequenceFlow> outgoingFlows = new ArrayList<SequenceFlow>();

	public void setId(UUID id) {
		this.Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UUID getId() {
		return Id;
	}

}
