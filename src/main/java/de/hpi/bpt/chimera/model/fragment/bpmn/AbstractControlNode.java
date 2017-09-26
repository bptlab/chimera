package de.hpi.bpt.chimera.model.fragment.bpmn;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractControlNode {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int dbId;

	private String id;
	@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "targetRef")
	@JoinColumn(name = "targetRef")
	private List<SequenceFlowAssociation> incomingControlNodes = new ArrayList<>();

	@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "sourceRef")
	@JoinColumn(name = "sourceRef")
	private List<SequenceFlowAssociation> outgoingControlNodes = new ArrayList<>();

	// GETTER & SETTER
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<SequenceFlowAssociation> getIncomingControlNodes() {
		return incomingControlNodes;
	}

	public void setIncomingControlNodes(List<SequenceFlowAssociation> incomingControlNodes) {
		this.incomingControlNodes = incomingControlNodes;
	}

	public List<SequenceFlowAssociation> getOutgoingControlNodes() {
		return outgoingControlNodes;
	}

	public void setOutgoingControlNodes(List<SequenceFlowAssociation> outgoingControlNodes) {
		this.outgoingControlNodes = outgoingControlNodes;
	}
}
