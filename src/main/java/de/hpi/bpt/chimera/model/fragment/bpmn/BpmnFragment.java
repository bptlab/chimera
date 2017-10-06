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

import de.hpi.bpt.chimera.model.fragment.bpmn.activity.Activity;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.EndEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.StartEvent;
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
	private List<Activity> activities = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<ParallelGateway> parallelGateways = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<ExclusiveGateway> exclusiveGateways = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL)
	private List<SequenceFlowAssociation> sequenceFlowAssociations = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<DataNode> dataNodes = new ArrayList<>();
	
	@OneToOne(cascade = CascadeType.ALL)
	private EndEvent endEvent;

	
	
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

	public List<Activity> getTasks() {
		return activities;
	}

	public List<SequenceFlowAssociation> getSequenceFlowAssociations() {
		return sequenceFlowAssociations;
	}

	public void setSequenceFlowAssociations(List<SequenceFlowAssociation> sequenceFlowAssociations) {
		this.sequenceFlowAssociations = sequenceFlowAssociations;
	}

	public List<DataNode> getDataNodes() {
		return dataNodes;
	}

	public void setDataNodes(List<DataNode> dataNodes) {
		this.dataNodes = dataNodes;
	}

	public void setTasks(List<Activity> activities) {
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


}
