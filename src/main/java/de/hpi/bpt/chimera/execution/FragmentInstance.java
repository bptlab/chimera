package de.hpi.bpt.chimera.execution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.event.StartEventInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractControlNodeInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.model.fragment.Fragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.Activity;
import de.hpi.bpt.chimera.model.fragment.bpmn.DataNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.SequenceFlowAssociation;
import de.hpi.bpt.chimera.model.fragment.bpmn.StartEvent;

public class FragmentInstance {
	private static Logger log = Logger.getLogger(FragmentInstance.class);

	private String id;
	private Fragment fragment;
	private Case caze;
	// TODO: think about whether it is needed to store terminated events, etc.
	// Activities should be recorded for the history.
	/**
	 * Map of Id of ControlNodeInstance to ControlNodeInstance.
	 */
	private Map<String, ControlNodeInstance> controlNodeInstances;
	/**
	 * Map of Id of ControlNode to the corresponding ControlNodeInstanc.
	 */
	private Map<String, ControlNodeInstance> controlNodeToInstanceMap;

	// TODO: re-implement the other functions which are given in
	// ...core.FragmentInstance, like all the initialize functions

	public FragmentInstance(Fragment fragment, Case caze) {
		this.id = UUID.randomUUID().toString();
		this.fragment = fragment;
		this.caze = caze;
		this.controlNodeInstances = new HashMap<>();
		this.controlNodeToInstanceMap = new HashMap<>();
		// TODO: implement the whole thing with the controlNodeInstances ...
	}

	/**
	 * Start the Case by creating an Instance of the StartEvent and enable the
	 * ControlFlow of that Instance.
	 */
	public void start() {
		StartEvent startEvent = fragment.getBpmnFragment().getStartEvent();
		if (startEvent == null) {
			log.info("no startEvent specified");
		}
		else {
			log.info("startEvent exists");
		}
		StartEventInstance startEventInstance = (StartEventInstance) ControlNodeInstanceFactory.createControlNodeInstance(startEvent, this);
		log.info("Created StartEventInstance");
		controlNodeInstances.put(startEventInstance.getId(), startEventInstance);
		controlNodeToInstanceMap.put(startEventInstance.getControlNode().getId(), startEventInstance);
		startEventInstance.enableControlFlow();
		log.info("ControlFlow of StartEventInstanceEnabled");
	}

	/**
	 * Update DataFlow of all ActivityInstances.
	 */
	public void updateDataFlow() {
		// TODO: implement
		/*
		 * for (ControlNodeInstance nodeInstance :
		 * controlNodeInstances.values()) { if (nodeInstance instanceof
		 * AbstractActivityInstance)) { ((AbstractActivityInstance)
		 * nodeInstance).checkDataFlowEnabled(); }
		 * 
		 * }
		 */
		// checkTerminationCondition()
	}

	/**
	 * Create Instances of the following ControlNodes of controlNode and enable
	 * the ControlFlow of them.
	 * 
	 * @param node
	 */
	public void enableFollowing(AbstractControlNode controlNode) {
		for (SequenceFlowAssociation sequenceFlow : controlNode.getOutgoingControlNodes()) {
			AbstractControlNode following = sequenceFlow.getTargetRef();
			ControlNodeInstance nodeInstance;
			if (controlNodeToInstanceMap.containsKey(following.getId())) {
				log.info("Following ControlNodeInstance already in cache.");
				nodeInstance = controlNodeToInstanceMap.get(following.getId());
			} else {
				log.info("Created Following ControlNodeInstance.");
				nodeInstance = ControlNodeInstanceFactory.createControlNodeInstance(following, this);
				controlNodeInstances.put(nodeInstance.getId(), nodeInstance);
				controlNodeToInstanceMap.put(following.getId(), nodeInstance);
			}
			nodeInstance.enableControlFlow();
			log.info("Enabled Following ControlNodeInstance");
		}
	}

	/**
	 * @return all enabled ControlNodeInstances
	 */
	public List<ControlNodeInstance> getEnabledControlNodeInstances() {
		return controlNodeInstances.values().stream().filter(x -> x.getState().equals(State.READY)).collect(Collectors.toList());
	}

	// GETTER & SETTER
	public String getId() {
		return id;
	}

	public Fragment getFragment() {
		return fragment;
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	public Map<String, ControlNodeInstance> getControlNodeInstances() {
		return controlNodeInstances;
	}

	public void setControlNodeInstances(Map<String, ControlNodeInstance> controlNodeInstances) {
		this.controlNodeInstances = controlNodeInstances;
	}

	public Case getCase() {
		return caze;
	}

	public void setCase(Case caze) {
		this.caze = caze;
	}
}
