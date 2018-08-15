package de.hpi.bpt.chimera.model.condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

/**
 * List of {@link AtomicDataStateCondition}{@code s} and can only be used if all
 * conditions are represented in the {@link DataManager}
 */
@Entity
public class ConditionSet {
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private int dbId;
	@OneToMany(cascade = CascadeType.ALL)
	List<AtomicDataStateCondition> conditions;

	public ConditionSet() {
		this.conditions = new ArrayList<>();
	}

	public ConditionSet(List<AtomicDataStateCondition> conditions) {
		this.conditions = conditions;
	}

	public void setConditions(List<AtomicDataStateCondition> conditions) {
		this.conditions = conditions;
	}

	public List<AtomicDataStateCondition> getConditions() {
		return this.conditions;
	}

	public void addCondition(AtomicDataStateCondition condition) {
		this.conditions.add(condition);
	}

	/**
	 * Check whether the ConditionSet fulfills the existing
	 * AtomicDataObjectStateCoditions. Therefore all Conditions in the
	 * ConditionSet have to exist (And-Behavior).
	 * 
	 * @param existingConditions
	 * @return boolean
	 */
	public boolean isFulfilled(List<AtomicDataStateCondition> existingConditions) {
		for (AtomicDataStateCondition condition : conditions) {
			if (!existingConditions.contains(condition))
				return false;
		}
		return true;
	}

	/**
	 * Make a Map from DataClass to ObjectLifecycleState of the
	 * DataStateConditions in this ConditionSet.
	 * 
	 * @return Map from DataClass to ObjectLifecycleState
	 */
	public Map<DataClass, ObjectLifecycleState> getDataClassToObjectLifecycleState() {
		Map<DataClass, ObjectLifecycleState> dataClassToObjectLifecycleState = new HashMap<>();
		for (AtomicDataStateCondition condition : conditions) {
			dataClassToObjectLifecycleState.put(condition.getDataClass(), condition.getObjectLifecycleState());
		}
		return dataClassToObjectLifecycleState;
	}
}
