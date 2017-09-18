package de.hpi.bpt.chimera.model.condition;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import de.hpi.bpt.chimera.execution.DataObjectInstance;

@Entity
public class TerminationConditionComponent {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int dbId;
	@OneToMany(cascade = CascadeType.PERSIST)
	List<DataObjectStateCondition> conditions;

	public TerminationConditionComponent() {
		this.conditions = new ArrayList<>();
	}

	public void setConditions(List<DataObjectStateCondition> conditions) {
		this.conditions = conditions;
	}

	public List<DataObjectStateCondition> getConditions() {
		return this.conditions;
	}

	public void addCondition(DataObjectStateCondition condition) {
		this.conditions.add(condition);
	}

	/**
	 * Check whether the TerminationConditionComponent fulfills the
	 * DataObjectStateCoditions.
	 * 
	 * @param existingConditions
	 * @return boolean
	 */
	public boolean isFulfilled(List<DataObjectStateCondition> existingConditions) {
		for (DataObjectStateCondition condition : conditions) {
			if (!existingConditions.contains(condition))
				return false;
		}
		return true;
	}
}
