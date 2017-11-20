package de.hpi.bpt.chimera.model.fragment.bpmn;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.ExtendedDataStateCondition;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;

@Entity
public abstract class AbstractDataControlNode extends AbstractControlNode {
	private String name = "";

	@OneToOne(cascade = CascadeType.ALL)
	private DataStateCondition preCondition;
	@OneToOne(cascade = CascadeType.ALL)
	private DataStateCondition postCondition;

	public AbstractDataControlNode() {
		this.preCondition = new DataStateCondition();
		this.postCondition = new DataStateCondition();
	}

	// GETTER & SETTER
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataStateCondition getPreCondition() {
		return preCondition;
	}

	public void setPreCondition(DataStateCondition preCondition) {
		this.preCondition = preCondition;
	}

	public DataStateCondition getPostCondition() {
		return postCondition;
	}

	public void setPostCondition(DataStateCondition postCondition) {
		this.postCondition = postCondition;
	}
}
