package de.hpi.bpt.chimera.model.fragment.BpmnFragment;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class AbstractControlNode {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int dbId;


	private String id;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "targetRef")
	@JoinColumn(name = "tragetRef")
	private List<SequenceFlowAssociation> incoming = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "sourceRef")
	@JoinColumn(name = "tragetRef")
	private List<SequenceFlowAssociation> outgoing = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<SequenceFlowAssociation> getIncoming() {
		return incoming;
	}

	public void setIncoming(List<SequenceFlowAssociation> incoming) {
		this.incoming = incoming;
	}

	public List<SequenceFlowAssociation> getOutgoing() {
		return outgoing;
	}

	public void setOutgoing(List<SequenceFlowAssociation> outgoing) {
		this.outgoing = outgoing;
	}

}
