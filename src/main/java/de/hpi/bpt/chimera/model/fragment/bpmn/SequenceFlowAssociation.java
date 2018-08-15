package de.hpi.bpt.chimera.model.fragment.bpmn;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class SequenceFlowAssociation {
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private int dbId;

	private String id;

	@OneToOne(cascade = CascadeType.ALL)
	private AbstractControlNode sourceRef = null;

	@OneToOne(cascade = CascadeType.ALL)
	private AbstractControlNode targetRef = null;

	@Column(name = "SequenceFlowCondition")
	private String condition;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AbstractControlNode getSourceRef() {
		return sourceRef;
	}

	public void setSourceRef(AbstractControlNode sourceRef) {
		this.sourceRef = sourceRef;
	}

	public AbstractControlNode getTargetRef() {
		return targetRef;
	}

	public void setTargetRef(AbstractControlNode targetRef) {
		this.targetRef = targetRef;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
}
