package de.hpi.bpt.chimera.execution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import de.hpi.bpt.chimera.execution.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.event.StartEventInstance;
import de.hpi.bpt.chimera.execution.gateway.AbstractGatewayInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.jcore.controlnodes.XorGatewayInstance;
import de.hpi.bpt.chimera.model.fragment.Fragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
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
	/**
	 * Map of Id of ControlNode to the corresponding ControlNodeInstanc.
	 */
	private Map<String, ControlNodeInstance> controlNodes;

	public FragmentInstance(Fragment fragment, Case caze) {
		this.id = UUID.randomUUID().toString().replace("-", "");
		this.fragment = fragment;
		this.caze = caze;
		this.controlNodeInstances = new HashMap<>();
		this.controlNodes = new HashMap<>();
	}

	/**
	 * Start the Case by creating an Instance of the StartEvent and enable the
	 * ControlFlow of that Instance.
	 */
	public void start() {
		StartEvent startEvent = fragment.getBpmnFragment().getStartEvent();
		StartEventInstance startEventInstance = (StartEventInstance) ControlNodeInstanceFactory.createControlNodeInstance(startEvent, this);
		addControlNodeInstance(startEventInstance);
		startEventInstance.enableControlFlow();
	}

	/**
	 * Update DataFlow of all ActivityInstances.
	 */
	public void updateDataFlow() {
		for (ControlNodeInstance nodeInstance : controlNodeInstances.values()) { 
			if (nodeInstance instanceof AbstractActivityInstance) {
				((AbstractActivityInstance) nodeInstance).checkDataFlow();
			}
		} 
	}

	/**
	 * Create Instances of the following ControlNodes of controlNode and enable
	 * the ControlFlow of them.
	 * 
	 * @param node
	 */
	public void createFollowing(AbstractControlNode controlNode) {
		for (AbstractControlNode following : controlNode.getOutgoingControlNodes()) {
			if (isInstantiated(following)) {
				ControlNodeInstance nodeInstance = controlNodes.get(following.getId());
				nodeInstance.enableControlFlow();
			} else {
				ControlNodeInstance nodeInstance = ControlNodeInstanceFactory.createControlNodeInstance(following, this);
				addControlNodeInstance(nodeInstance);
				nodeInstance.enableControlFlow();
			}
		}
	}

	/**
	 * 
	 * @param controlNodes
	 * @return
	 */
	public boolean checkExclusiveGatewayBehaviour(ControlNodeInstance instance, List<ControlNodeInstance> instancesToRemove) {
		return false;
	}

	// HELPER METHODS
	/**
	 * 
	 * @param nodeInstance
	 */
	private void addControlNodeInstance(ControlNodeInstance nodeInstance) {
		controlNodeInstances.put(nodeInstance.getId(), nodeInstance);
		controlNodes.put(nodeInstance.getControlNode().getId(), nodeInstance);
	}

	/**
	 * 
	 * @param node
	 * @return true if the ControlNode is instantiated.
	 */
	public boolean isInstantiated(AbstractControlNode node) {
		return controlNodes.containsKey(node.getId());
	}

	/**
	 * 
	 * @param nodes
	 * @return true if all ControlNodes are instantiated.
	 */
	public boolean areInstantiated(List<AbstractControlNode> nodes) {
		for (AbstractControlNode node : nodes) {
			if (!isInstantiated(node))
				return false;
		}
		return true;
	}


	/**
	 * 
	 * @param nodes
	 * @return true if the ControlNode is terminated.
	 */
	public boolean isTerminated(AbstractControlNode node) {
		if (controlNodes.containsKey(node.getId()) && controlNodes.get(node.getId()).getState() == State.TERMINATED) {
			return true;
		} else {
			return false;
		}
	}

	// TODO maybe not needed >>> maybe a separate map for executing (Xor-)
	// Gateways ist better
	/**
	 * @return a ArrayList of executing gateways.
	 */
	public Map<String, AbstractGatewayInstance> getExecutingGateways() {
		return this.controlNodeInstances.entrySet().stream().filter(x -> (x.getValue().getClass().equals(XorGatewayInstance.class) && x.getValue().getState() == State.EXECUTING)).collect(Collectors.toMap(p -> p.getKey(), p -> (AbstractGatewayInstance) p.getValue()));
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
