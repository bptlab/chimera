package de.hpi.bpt.chimera.model.condition;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import de.hpi.bpt.chimera.execution.DataObject;

@Entity
public class TerminationCondition {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int dbId;
	@OneToMany(cascade = CascadeType.ALL)
	List<TerminationConditionComponent> conditions;

	public TerminationCondition() {
		this.conditions = new ArrayList<>();
	}

	public List<TerminationConditionComponent> getConditions() {
		return conditions;
	}

	public void setConditions(List<TerminationConditionComponent> conditions) {
		this.conditions = conditions;
	}

	public void addConditionComponent(TerminationConditionComponent condition) {
		this.conditions.add(condition);
	}

	/**
	 * Check whether the TerminationCondition fulfills the
	 * DataObjectStateCoditions.
	 * 
	 * @param existingConditions
	 * @return boolean
	 */
	public boolean isFulfilled(List<DataObjectStateCondition> existingConditions) {
		if (conditions.isEmpty())
			return true;
		for (TerminationConditionComponent component : conditions) {
			if (component.isFulfilled(existingConditions)) {
				return true;
			}
		}
		return false;
	}
}
