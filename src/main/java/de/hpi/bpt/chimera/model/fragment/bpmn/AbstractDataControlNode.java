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
import de.hpi.bpt.chimera.model.condition.MetaCondition;

@Entity
public abstract class AbstractDataControlNode extends AbstractControlNode {
	private String name = "";

	@OneToOne(cascade = CascadeType.ALL)
	private MetaCondition preCondition;
	@OneToOne(cascade = CascadeType.ALL)
	private MetaCondition postCondition;

	public AbstractDataControlNode() {
		this.preCondition = new MetaCondition();
		this.postCondition = new MetaCondition();
	}

	// GETTER & SETTER
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MetaCondition getPreCondition() {
		return preCondition;
	}

	public void setPreCondition(MetaCondition preCondition) {
		this.preCondition = preCondition;
	}

	public MetaCondition getPostCondition() {
		return postCondition;
	}

	public void setPostCondition(MetaCondition postCondition) {
		this.postCondition = postCondition;
	}
}
