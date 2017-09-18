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
import de.hpi.bpt.chimera.model.fragment.bpmn.DataNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.SequenceFlowAssociation;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.Activity;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.StartEvent;

public class FragmentInstance {
	private String id;
	private Fragment fragment;
	private Case caze;
	// TODO: think about whether it is needed to store terminated events, etc.
	// Activities should be recorded for the history.
	/**
	 * Map of Id of ControlNodeInstance to ControlNodeInstance.
	 */
	private Map<String, ControlNodeInstance> controlNodeInstances;

	// TODO: re-implement the other functions which are given in
	// ...core.FragmentInstance, like all the initialize functions

	public FragmentInstance(Fragment fragment, Case caze) {
		this.id = UUID.randomUUID().toString().replace("-", "");
		this.fragment = fragment;
		this.caze = caze;
		this.controlNodeInstances = new HashMap<>();
		// TODO: implement the whole thing with the controlNodeInstances ...
	}

	/**
	 * Start the Case by creating an Instance of the StartEvent and enable the
	 * ControlFlow of that Instance.
	 */
	public void start() {
		StartEvent startEvent = fragment.getBpmnFragment().getStartEvent();
		StartEventInstance startEventInstance = (StartEventInstance) ControlNodeInstanceFactory.createControlNodeInstance(startEvent, this);
		controlNodeInstances.put(startEventInstance.getId(), startEventInstance);
		startEventInstance.enableControlFlow();
	}

	/**
	 * Update DataFlow of all ActivityInstances.
	 */
	public void updateDataFlow() {
		// TODO: implement
		for (ControlNodeInstance nodeInstance : controlNodeInstances.values()) { 
			if (nodeInstance instanceof AbstractActivityInstance) {
				((AbstractActivityInstance) nodeInstance).checkDataFlow();
			}
		}
		 
		// checkTerminationCondition()
	}

	/**
	 * Create Instances of the following ControlNodes of controlNode and enable
	 * the ControlFlow of them.
	 * 
	 * @param node
	 */
	public void createFollowing(AbstractControlNode controlNode) {
		for (AbstractControlNode following : controlNode.getOutgoingControlNodes()) {
			ControlNodeInstance nodeInstance = ControlNodeInstanceFactory.createControlNodeInstance(following, this);
			controlNodeInstances.put(nodeInstance.getId(), nodeInstance);
			nodeInstance.enableControlFlow();
		}
	}

	/**
	 * 
	 * @param controlNode
	 * @return true if the ControlNode is instantiated
	 */
	public boolean isInstantiated(AbstractControlNode incommingControlNode) {
		// TODO: implement
		return controlNodeInstances.containsKey(incommingControlNode.getId());
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
