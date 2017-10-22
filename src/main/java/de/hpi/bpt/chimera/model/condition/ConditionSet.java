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

/**
 * List of {@link DataStateCondition}{@code s} and can only be used if all
 * conditions are represented in the {@link DataManager}
 */
@Entity
public class ConditionSet {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int dbId;
	@OneToMany(cascade = CascadeType.ALL)
	List<DataStateCondition> conditions;

	public ConditionSet() {
		this.conditions = new ArrayList<>();
	}

	public ConditionSet(List<DataStateCondition> conditions) {
		this.conditions = conditions;
	}

	public void setConditions(List<DataStateCondition> conditions) {
		this.conditions = conditions;
	}

	public List<DataStateCondition> getConditions() {
		return this.conditions;
	}

	public void addCondition(DataStateCondition condition) {
		this.conditions.add(condition);
	}

	/**
	 * Check whether the ConditionSet fulfills the existing
	 * DataObjectStateCoditions. Therefore all Conditions in the ConditionSet
	 * have to exist (And-Behavior).
	 * 
	 * @param existingConditions
	 * @return boolean
	 */
	public boolean isFulfilled(List<DataStateCondition> existingConditions) {
		for (DataStateCondition condition : conditions) {
			if (!existingConditions.contains(condition))
				return false;
		}
		return true;
	}
}
