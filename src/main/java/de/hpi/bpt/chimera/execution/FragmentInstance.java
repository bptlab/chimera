package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.controlnodes.ControlNodeInstance;
import de.hpi.bpt.chimera.execution.controlnodes.ControlNodeInstanceFactory;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.StartEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.gateway.AbstractGatewayInstance;
import de.hpi.bpt.chimera.execution.controlnodes.gateway.ExclusiveGatewayInstance;
import de.hpi.bpt.chimera.model.fragment.Fragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.StartEvent;

public class FragmentInstance {
	private static final Logger log = Logger.getLogger(FragmentInstance.class);

	private String id;
	private Fragment fragment;
	private Case caze;
	// TODO: think about whether it is needed to store terminated events, etc.
	// Activities should be recorded for the history.
	private Map<String, ControlNodeInstance> controlNodeInstanceIdToInstance;
	private Map<String, ControlNodeInstance> controlNodeIdToInstance;

	public FragmentInstance(Fragment fragment, Case caze) {
		this.id = UUID.randomUUID().toString().replace("-", "");
		this.fragment = fragment;
		this.caze = caze;
		this.controlNodeInstanceIdToInstance = new HashMap<>();
		this.controlNodeIdToInstance = new HashMap<>();
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
		for (ControlNodeInstance nodeInstance : controlNodeInstanceIdToInstance.values()) { 
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
	public List<ControlNodeInstance> createFollowing(AbstractControlNode controlNode) {
		List<ControlNodeInstance> createdFollowingControlNodeInstances = new ArrayList<>();
		for (AbstractControlNode following : controlNode.getOutgoingControlNodes()) {
			ControlNodeInstance nodeInstance;
			if (isInstantiated(following)) {
				nodeInstance = controlNodeIdToInstance.get(following.getId());
				nodeInstance.enableControlFlow();
			} else {
				nodeInstance = ControlNodeInstanceFactory.createControlNodeInstance(following, this);
				addControlNodeInstance(nodeInstance);
				nodeInstance.enableControlFlow();
			}
			createdFollowingControlNodeInstances.add(nodeInstance);
		}
		return createdFollowingControlNodeInstances;
	}

	public ControlNodeInstance createControlNodeInstance(AbstractControlNode controlNode) {
		if (isInstantiated(controlNode)) {
			return controlNodeIdToInstance.get(controlNode.getId());
		} else {
			ControlNodeInstance nodeInstance = ControlNodeInstanceFactory.createControlNodeInstance(controlNode, this);
			addControlNodeInstance(nodeInstance);
			return nodeInstance;
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
		controlNodeInstanceIdToInstance.put(nodeInstance.getId(), nodeInstance);
		controlNodeIdToInstance.put(nodeInstance.getControlNode().getId(), nodeInstance);
	}

	/**
	 * 
	 * @param node
	 * @return true if the ControlNode is instantiated.
	 */
	public boolean isInstantiated(AbstractControlNode node) {
		return controlNodeIdToInstance.containsKey(node.getId());
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
		if (controlNodeIdToInstance.containsKey(node.getId()) && controlNodeIdToInstance.get(node.getId()).getState() == State.TERMINATED) {
			return true;
		} else {
			return false;
		}
	}

	// TODO maybe not needed >>> maybe a separate map for executing (Xor-)
	// Gateways is better and faster
	/**
	 * @return a ArrayList of executing gateways.
	 */
	public Map<String, ExclusiveGatewayInstance> getExecutingExclusiveGateways() {
		return this.controlNodeInstanceIdToInstance.entrySet().stream().filter(x -> (x.getValue().getClass().equals(ExclusiveGatewayInstance.class) && x.getValue().getState() == State.EXECUTING)).collect(Collectors.toMap(p -> p.getKey(), p -> (ExclusiveGatewayInstance) p.getValue()));
	}

	// TODO maybe not needed >>> maybe a separate map for executing (Xor-)
	// Gateways is better and faster
	/**
	 * @return a ArrayList of executing gateways.
	 */
	public Map<String, AbstractGatewayInstance> getExecutingGateways() {
		return this.controlNodeIdToInstance.entrySet().stream().filter(x -> (x.getValue() instanceof AbstractGatewayInstance && x.getValue().getState() == State.EXECUTING)).collect(Collectors.toMap(p -> p.getKey(), p -> (AbstractGatewayInstance) p.getValue()));
	}

	/**
	 * 
	 * @param controlNodeId
	 * @return the Instance of the ControlNode with the given Id. Null if that
	 *         ControlNode isn't instantiated yet.
	 */
	public ControlNodeInstance getControlNodeInstanceById(String controlNodeId) {
		// TODO is it possible that someone calls this method for a controllnode
		// that isn't instantiated yet?
		if (controlNodeIdToInstance.containsKey(controlNodeId)) {
			return controlNodeIdToInstance.get(controlNodeId);
		} else {
			return null;
		}
	}


	/**
	 * Checks whether the given ControlNodeInstance is part of a branch of an
	 * executing Gateway.In this case all the ControlNodeInstances of all the
	 * other branches of this Gateway will be skipped. (used for activities
	 * after ExclusiveGateways)
	 *
	 * @param controlNodeInstance
	 *            The control node instance, which was transferred to the state
	 *            ready.
	 */
	public void skipAlternativeControlNodes(ControlNodeInstance controlNodeInstance) {
		Map<String, ExclusiveGatewayInstance> gateways = this.getExecutingExclusiveGateways();
		for (ExclusiveGatewayInstance gateway : gateways.values()) {
			if (gateway.containsControlNodeInFollowing(controlNodeInstance.getControlNode())) {
				// log.info(String.format("skipping alternatives for the
				// following Gateway: %s", gateway.getId()));
				gateway.skipAlternativeBranches(controlNodeInstance.getControlNode());
			}
		}
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

	public Map<String, ControlNodeInstance> getControlNodeInstanceIdToInstance() {
		return controlNodeInstanceIdToInstance;
	}

	public void setControlNodeInstances(Map<String, ControlNodeInstance> controlNodeInstances) {
		this.controlNodeInstanceIdToInstance = controlNodeInstances;
	}

	public Map<String, ControlNodeInstance> getControlNodeIdToInstance() {
		return controlNodeIdToInstance;
	}

	public List<ControlNodeInstance> getControlNodeInstances() {
		return new ArrayList<>(controlNodeInstanceIdToInstance.values());
	}

	public Case getCase() {
		return caze;
	}

	public void setCase(Case caze) {
		this.caze = caze;
	}
}
