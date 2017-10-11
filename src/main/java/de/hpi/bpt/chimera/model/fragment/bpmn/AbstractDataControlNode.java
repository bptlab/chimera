package de.hpi.bpt.chimera.model.fragment.bpmn;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.ExtendedDataStateCondition;

@Entity
public abstract class AbstractDataControlNode extends AbstractControlNode {
	private String name = "";

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "dataflow_incomming")
	private List<ConditionSet> preCondition = new ArrayList<>();
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "dataflow_outgoing")
	private List<ConditionSet> postCondition = new ArrayList<>();

	// GETTER & SETTER
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ConditionSet> getPreCondition() {
		return preCondition;
	}

	public void setPreCondition(List<ConditionSet> preCondition) {
		this.preCondition = preCondition;
	}

	public List<ConditionSet> getPostCondition() {
		return postCondition;
	}

	public void setPostCondition(List<ConditionSet> postCondition) {
		this.postCondition = postCondition;
	}
}
