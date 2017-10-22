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
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractDataControlNode;

// TODO: think of a better name
/**
 * List of {@link ConditionSet}s that simulates disjunctive behavior between
 * those ConditionSets. This means that the MetaCondition is fulfilled if there
 * isn't any ConditionSet or one of the ConditionSets fulfills the
 * DataStateCondititions that exist at the moment in the running {@link Case}.
 */
@Entity
public class MetaCondition {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int dbId;
	@OneToMany(cascade = CascadeType.ALL)
	List<ConditionSet> conditionSets;

	public MetaCondition() {
		this.conditionSets = new ArrayList<>();
	}

	public MetaCondition(List<ConditionSet> conditionSets) {
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
	public boolean isFulfilled(List<DataStateCondition> existingConditions) {
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
	public List<ConditionSet> getFulfilledConditions(List<DataStateCondition> existingConditions) {
		List<ConditionSet> availableCondititionSets = new ArrayList<>();
		for (ConditionSet conditionSet : conditionSets) {
			if (conditionSet.isFulfilled(existingConditions)) {
				availableCondititionSets.add(conditionSet);
			}
		}
		return availableCondititionSets;
	}
}
