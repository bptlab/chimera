package de.hpi.bpt.chimera.model.condition;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class CaseStartTrigger {
	@Id
	@GeneratedValue
	private int dbId;
	private String id = UUID.randomUUID().toString().replaceAll("\\-", "");
	private String queryExecutionPlan;
	@OneToMany(cascade = CascadeType.ALL)
	private List<CaseStartTriggerConsequence> triggerConsequences;

	// Not in the Gryphon JSON data.
	// Will be set during parsing from the EventDipetcher which gets the data
	// from unicorn while registering the event.
	private String eventKeyId; // The id under which the event is registered at
								// unicorn.
	private String notificationRuleId;

	public String getQueryExecutionPlan() {
		return queryExecutionPlan;
	}

	public void setQueryExecutionPlan(String queryExecutionPlan) {
		this.queryExecutionPlan = queryExecutionPlan;
	}

	public void setEventKeyId(String eventKeyId) {
		this.eventKeyId = eventKeyId;
	}

	public String getNotificationRuleId() {
		return notificationRuleId;
	}

	public void setNotificationRuleId(String notificationRuleId) {
		this.notificationRuleId = notificationRuleId;
	}

	public String getId() {
		return id;
	}

	public List<CaseStartTriggerConsequence> getTriggerConsequences() {
		return triggerConsequences;
	}

	public void setTriggerConsequence(List<CaseStartTriggerConsequence> triggerConsequences) {
		this.triggerConsequences = triggerConsequences;
	}

	public boolean hasMapping() {
		return getTriggerConsequences().stream().filter(x -> !x.getDataAttributeToJsonPath().isEmpty()).findAny().isPresent();
	}
}
