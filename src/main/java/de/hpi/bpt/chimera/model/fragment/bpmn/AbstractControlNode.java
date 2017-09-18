package de.hpi.bpt.chimera.model.fragment.bpmn;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public abstract class AbstractControlNode {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int dbId;

	private String id;
	@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "targetRef")
	@JoinColumn(name = "targetRef")
	private List<SequenceFlowAssociation> incomingSequenceFlows = new ArrayList<>();

	@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "sourceRef")
	@JoinColumn(name = "sourceRef")
	private List<SequenceFlowAssociation> outgoingSequenceFlows = new ArrayList<>();

	// GETTER & SETTER
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<AbstractControlNode> getIncomingControlNodes() {
		return incomingSequenceFlows.stream().map(x -> x.getSourceRef()).collect(Collectors.toList());
	}

	public List<AbstractControlNode> getOutgoingControlNodes() {
		return outgoingSequenceFlows.stream().map(x -> x.getTargetRef()).collect(Collectors.toList());
	}

	public List<SequenceFlowAssociation> getIncommingSequenceFlows() {
		return incomingSequenceFlows;
	}

	public void setIncomingSequenceFlows(List<SequenceFlowAssociation> incomingSequenceFlows) {
		this.incomingSequenceFlows = incomingSequenceFlows;
	}

	public List<SequenceFlowAssociation> getOutgoingSequenceFlows() {
		return outgoingSequenceFlows;
	}

	public void setOutgoingSequenceFlows(List<SequenceFlowAssociation> outgoingSequenceFlows) {
		this.outgoingSequenceFlows = outgoingSequenceFlows;
	}
}
