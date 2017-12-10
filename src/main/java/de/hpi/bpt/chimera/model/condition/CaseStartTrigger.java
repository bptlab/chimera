package de.hpi.bpt.chimera.model.condition;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import de.hpi.bpt.chimera.model.CaseModel;

@Entity
public class CaseStartTrigger {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int dbId;
	@OneToOne(cascade = CascadeType.ALL)
	private CaseModel parentCaseModel;
	private String id = UUID.randomUUID().toString().replaceAll("\\-", "");
	private String queryExecutionPlan;
	@OneToMany(cascade = CascadeType.ALL)
	private List<CaseStartTriggerConsequence> triggerConsequences;

	// Not in the Gryphon JSON data.
	// Will be set during parsing from the EventDipetcher which gets the data
	// from unicorn while registering the event.
	private String eventKeyId; // The id under which the event is registered at
								// unicorn.
	private String NotificationRuleId;

	public String getQueryExecutionPlan() {
		return queryExecutionPlan;
	}

	public void setQueryExecutionPlan(String queryExecutionPlan) {
		this.queryExecutionPlan = queryExecutionPlan;
	}

	public String getEventKeyId() {
		return eventKeyId;
	}

	public void setEventKeyId(String eventKeyId) {
		this.eventKeyId = eventKeyId;
	}

	public String getNotificationRuleId() {
		return NotificationRuleId;
	}

	public void setNotificationRuleId(String notificationRuleId) {
		NotificationRuleId = notificationRuleId;
	}

	public String getId() {
		return id;
	}

	public CaseModel getParentCaseModel() {
		return parentCaseModel;
	}

	public void setParentCaseModel(CaseModel parentCaseModel) {
		this.parentCaseModel = parentCaseModel;
	}

	public List<CaseStartTriggerConsequence> getTriggerConsequences() {
		return triggerConsequences;
	}

	public void setTriggerConsequence(List<CaseStartTriggerConsequence> triggerConsequences) {
		this.triggerConsequences = triggerConsequences;
	}

}
