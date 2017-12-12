package de.hpi.bpt.chimera.model.fragment.bpmn;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.EndEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.StartEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.TimerEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.ExclusiveGateway;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.ParallelGateway;

@Entity
public class BpmnFragment {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int dbId;


	private String id;
	
	@OneToOne(cascade = CascadeType.ALL)
	private StartEvent startEvent;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<AbstractActivity> activities = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<ParallelGateway> parallelGateways = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<ExclusiveGateway> exclusiveGateways = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL)
	private List<SequenceFlowAssociation> sequenceFlowAssociations = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<AtomicDataStateCondition> conditions = new ArrayList<>();
	
	@OneToOne(cascade = CascadeType.ALL)
	private EndEvent endEvent;

	@OneToMany(cascade = CascadeType.ALL)
	private List<TimerEvent> timerEvents = new ArrayList<>();

	public void addTasks(List<AbstractActivity> tasks) {
		activities.addAll(tasks);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public StartEvent getStartEvent() {
		return startEvent;
	}

	public void setStartEvent(StartEvent startEvent) {
		this.startEvent = startEvent;
	}

	public List<AbstractActivity> getTasks() {
		return activities;
	}

	public List<SequenceFlowAssociation> getSequenceFlowAssociations() {
		return sequenceFlowAssociations;
	}

	public void setSequenceFlowAssociations(List<SequenceFlowAssociation> sequenceFlowAssociations) {
		this.sequenceFlowAssociations = sequenceFlowAssociations;
	}

	public List<AtomicDataStateCondition> getConditions() {
		return conditions;
	}

	public void setConditions(List<AtomicDataStateCondition> conditions) {
		this.conditions = conditions;
	}

	public void setTasks(List<AbstractActivity> activities) {
		this.activities = activities;
	}

	public EndEvent getEndEvent() {
		return endEvent;
	}

	public void setEndEvent(EndEvent endEvent) {
		this.endEvent = endEvent;
	}

	public List<ParallelGateway> getParallelGateways() {
		return parallelGateways;
	}

	public void setParallelGateways(List<ParallelGateway> parallelGateways) {
		this.parallelGateways = parallelGateways;
	}

	public List<ExclusiveGateway> getExclusiveGateways() {
		return exclusiveGateways;
	}

	public void setExclusiveGateways(List<ExclusiveGateway> exclusiveGateways) {
		this.exclusiveGateways = exclusiveGateways;
	}

	public List<TimerEvent> getTimerEvents() {
		return timerEvents;
	}

	public void setTimerEvents(List<TimerEvent> timerEvents) {
		this.timerEvents = timerEvents;
	}

}
