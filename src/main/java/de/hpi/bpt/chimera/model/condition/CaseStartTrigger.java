package de.hpi.bpt.chimera.model.condition;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

public class CaseStartTrigger {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int dbId;
	private String queryExecutionPlan;
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<CaseStartTriggerConsequence> triggerConsequences;

	public String getQueryExecutionPlan() {
		return queryExecutionPlan;
	}

	public void setQueryExecutionPlan(String queryExecutionPlan) {
		this.queryExecutionPlan = queryExecutionPlan;
	}

	public List<CaseStartTriggerConsequence> getTriggerConsequences() {
		return triggerConsequences;
	}

	public void setTriggerConsequence(List<CaseStartTriggerConsequence> triggerConsequences) {
		this.triggerConsequences = triggerConsequences;
	}
}
