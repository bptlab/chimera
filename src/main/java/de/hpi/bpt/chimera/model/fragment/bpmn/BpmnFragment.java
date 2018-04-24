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
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.BoundaryEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.EndEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.IntermediateCatchEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.IntermediateThrowEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.StartEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.EventBasedGateway;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.ExclusiveGateway;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.ParallelGateway;

@Entity
public class BpmnFragment {
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
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
	private List<IntermediateCatchEvent> intermediateCatchEvents = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL)
	private List<IntermediateThrowEvent> intermediateThrowEvents = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL)
	private List<BoundaryEvent> boundaryEvents = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL)
	private List<EventBasedGateway> eventBasedGateways = new ArrayList<>();

	public void addActivities(List<AbstractActivity> tasks) {
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

	public List<AbstractActivity> getActivities() {
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

	public List<EventBasedGateway> getEventBasedGateways() {
		return eventBasedGateways;
	}

	public void setEventBasedGateways(List<EventBasedGateway> eventBasedGateways) {
		this.eventBasedGateways = eventBasedGateways;
	}

	public List<IntermediateCatchEvent> getIntermediateCatchEvents() {
		return intermediateCatchEvents;
	}

	public void setIntermediateCatchEvents(List<IntermediateCatchEvent> intermediateCatchEvents) {
		this.intermediateCatchEvents = intermediateCatchEvents;
	}

	public List<IntermediateThrowEvent> getIntermediateThrowEvents() {
		return intermediateThrowEvents;
	}

	public void setIntermediateThrowEvents(List<IntermediateThrowEvent> intermediateThrowEvents) {
		this.intermediateThrowEvents = intermediateThrowEvents;
	}

	public List<BoundaryEvent> getBoundaryEvents() {
		return boundaryEvents;
	}

	public void setBoundaryEvents(List<BoundaryEvent> boundaryEvents) {
		this.boundaryEvents = boundaryEvents;
	}

	public List<AbstractEvent> getEvents() {
		List<AbstractEvent> allEvents = new ArrayList<>();
		if (startEvent != null) {
			allEvents.add(startEvent);
		}
		if (endEvent != null) {
			allEvents.add(endEvent);
		}
		allEvents.addAll(intermediateCatchEvents);
		allEvents.addAll(intermediateThrowEvents);
		allEvents.addAll(boundaryEvents);
		return allEvents;
	}
}
