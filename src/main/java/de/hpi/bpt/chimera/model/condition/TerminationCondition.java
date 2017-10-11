package de.hpi.bpt.chimera.model.condition;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class TerminationCondition {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int dbId;
	@OneToMany(cascade = CascadeType.ALL)
	List<ConditionSet> conditions;

	public TerminationCondition() {
		this.conditions = new ArrayList<>();
	}

	public List<ConditionSet> getConditions() {
		return conditions;
	}

	public void setConditions(List<ConditionSet> conditions) {
		this.conditions = conditions;
	}

	public void addConditionComponent(ConditionSet condition) {
		this.conditions.add(condition);
	}

	/**
	 * Check whether the TerminationCondition fulfills the
	 * DataObjectStateCoditions. Therefore one ConditionSet has to be fulfilled
	 * (Or-Behavior).
	 * 
	 * @param existingConditions
	 * @return boolean
	 */
	public boolean isFulfilled(List<DataStateCondition> existingConditions) {
		if (conditions.isEmpty())
			return true;
		for (ConditionSet component : conditions) {
			if (component.isFulfilled(existingConditions)) {
				return true;
			}
		}
		return false;
	}
}
