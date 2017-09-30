package de.hpi.bpt.chimera.model.fragment.bpmn;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

@Entity
public abstract class AbstractDataControlNode extends AbstractControlNode {
	private String name = "";

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "dataflow_incomming")
	private List<DataNode> incomingDataNodes = new ArrayList<>();
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "dataflow_outgoing")
	private List<DataNode> outgoingDataNodes = new ArrayList<>();

	// GETTER & SETTER
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DataNode> getIncomingDataNodes() {
		return incomingDataNodes;
	}

	public void setIncomingDataNodes(List<DataNode> incomingDataNodes) {
		this.incomingDataNodes = incomingDataNodes;
	}

	public List<DataNode> getOutgoingDataNodes() {
		return outgoingDataNodes;
	}

	public void setOutgoingDataNodes(List<DataNode> outgoingDataNodes) {
		this.outgoingDataNodes = outgoingDataNodes;
	}
}
