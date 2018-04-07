package de.hpi.bpt.chimera.model.condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

// TODO: think of a better name
/**
 * List of {@link ConditionSet}s that simulates disjunctive behavior between
 * those ConditionSets. This means that the DataStateCondition is fulfilled if
 * there isn't any ConditionSet or one of the ConditionSets fulfills the
 * AtomicDataStateCondititions that exist at the moment in the running
 * {@link Case}.
 */
@Entity
public class DataStateCondition {
	@Id
	@GeneratedValue
	private int dbId;
	@OneToMany(cascade = CascadeType.ALL)
	List<ConditionSet> conditionSets;

	public DataStateCondition() {
		this.conditionSets = new ArrayList<>();
	}

	public DataStateCondition(List<ConditionSet> conditionSets) {
		this.conditionSets = conditionSets;
	}

	public List<ConditionSet> getConditionSets() {
		return conditionSets;
	}

	public void setConditionSets(List<ConditionSet> conditions) {
		this.conditionSets = conditions;
	}

	public void addConditionComponent(ConditionSet condition) {
		this.conditionSets.add(condition);
	}

	/**
	 * Check whether the MetaCondition fulfills the DataStateCoditions.
	 * Therefore one ConditionSet has to be fulfilled (Or-Behavior).
	 * 
	 * @param existingConditions
	 * @return boolean
	 */
	public boolean isFulfilled(List<AtomicDataStateCondition> existingConditions) {
		if (conditionSets.isEmpty())
			return true;
		for (ConditionSet component : conditionSets) {
			if (component.isFulfilled(existingConditions)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get all {@link ConditionSet} of the MetaCondition that fulfill the
	 * existing DataStateConditions.
	 * 
	 * @param existingConditions
	 *            - list of DataStateCondition that exist at the moment
	 * @return list - of ConditionSets that fulfill the existing
	 *         DataStateConditions
	 */
	public List<ConditionSet> getFulfilledConditions(List<AtomicDataStateCondition> existingConditions) {
		List<ConditionSet> availableCondititionSets = new ArrayList<>();
		for (ConditionSet conditionSet : conditionSets) {
			if (conditionSet.isFulfilled(existingConditions)) {
				availableCondititionSets.add(conditionSet);
			}
		}
		return availableCondititionSets;
	}

	/**
	 * Get all DataStateConditions that occur in the ConditionSets.
	 * 
	 * @return all DataStateConditions that occur in the ConditionSets
	 */
	public Set<AtomicDataStateCondition> getAtomicDataStateConditions() {
		Set<AtomicDataStateCondition> atomicDataStateConditions = new HashSet<>();
		for (ConditionSet conditionSet : conditionSets) {
			atomicDataStateConditions.addAll(conditionSet.getConditions());
		}
		return atomicDataStateConditions;
	}

	/**
	 * Get all possible ObjectLifecycleStates that occur in the ConditionSets
	 * associated to their {@link DataClass}.
	 * 
	 * @return Map from DataClass to all possible ObjectLifecycleStates
	 */
	public Map<DataClass, List<ObjectLifecycleState>> getDataClassToObjectLifecycleStates() {
		Map<DataClass, List<ObjectLifecycleState>> dataClassToObjectLifecycleStates = new HashMap<>();

		for (AtomicDataStateCondition condition : getAtomicDataStateConditions()) {
			DataClass dataClass = condition.getDataClass();
			ObjectLifecycleState olcState = condition.getObjectLifecycleState();
			if (dataClassToObjectLifecycleStates.containsKey(dataClass)) {
				dataClassToObjectLifecycleStates.get(dataClass).add(olcState);
			} else {
				dataClassToObjectLifecycleStates.put(dataClass, new ArrayList<>(Arrays.asList(olcState)));
			}
		}

		return dataClassToObjectLifecycleStates;
	}
	public boolean isEmpty() {
		return conditionSets.isEmpty();
	}
}
