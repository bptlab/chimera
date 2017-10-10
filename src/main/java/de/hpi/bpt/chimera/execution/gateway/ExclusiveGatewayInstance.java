package de.hpi.bpt.chimera.execution.gateway;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.ControlNodeInstance;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractControlNodeInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.EventBasedGateway;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.ParallelGateway;
import de.hpi.bpt.chimera.model.fragment.bpmn.SequenceFlowAssociation;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.ExclusiveGateway;

public class ExclusiveGatewayInstance extends AbstractGatewayInstance {
	private static Logger log = Logger.getLogger(ExclusiveGatewayInstance.class);

	private List<List<String>> branches = new ArrayList<>();

	public ExclusiveGatewayInstance(ExclusiveGateway gateway, FragmentInstance fragmentInstance) {
		super(gateway, fragmentInstance);
		initializeBranches();
		setState(State.INIT);
	}

	/**
	 * Adds the ids of the following control nodes to the list. Creates for
	 * every control node a bucket.
	 */
	private void initializeBranches() {
		for (AbstractControlNode node : this.getControlNode().getOutgoingControlNodes()) {
			branches.add(expandBranch(node));
		}
	}

	/**
	 * Expands branch to cover all control nodes skipping intermediate gateways.
	 *
	 * @param id
	 *            The id of the control node getting added.
	 */
	private List<String> expandBranch(AbstractControlNode controlNode) {
		List<String> ids = new ArrayList<>();
		ids.add(controlNode.getId());

		// Expanding each branch along outgoing Gateways until the first non
		// Gateway ControlNode appears
		if (controlNode.getClass().equals(ExclusiveGateway.class) || controlNode.getClass().equals(ParallelGateway.class) || controlNode.getClass().equals(EventBasedGateway.class)) {
			for (AbstractControlNode outgoingNode : controlNode.getOutgoingControlNodes()) {
				ids.addAll(expandBranch(outgoingNode));
			}
		}
		return ids;
	}

	/**
	 * Skips all ControlNodeInstances which aren't on the branch of the given
	 * ControlNode
	 *
	 * @param ongoingNode
	 *            The ControlNode on the ongoing branch. (the begun activity)
	 */
	public void skipAlternativeBranches(AbstractControlNode ongoingNode) {
		for (List<String> branch : branches) {
			if (!branch.contains(ongoingNode.getId())) {
				// log.info("skipping a branch");
				skipBranch(branch);
			}
		}
		this.terminate();
	}

	/**
	 * Skips all the ControlNodes with the given ControlNodeIds
	 *
	 * @param branch
	 *            The list of ControlNodeIds that should be skipped.
	 */
	private void skipBranch(List<String> branch) {
		// Gets the ControlNodesInstances for the given Ids and calls skip for
		// each of the ControlNodeInstances.
		for (String toSkip : branch) {
			ControlNodeInstance node = this.getFragmentInstance().getControlNodeInstance(toSkip);
			if (node != null) {
				node.skip();
				// log.info(String.format("skipping the ControlNode with the id:
				// %s", node.getControlNode().getId()));
			}
		}
	}

	/**
	 * Returns true, if the given ControlNode follows this Gateway (directly or
	 * with some other Gateways in between). Returns false otherwise.
	 * 
	 * @param controlNode
	 *            The ControlNode which is tested
	 * @return true when the Controlnode is in following, false otherwise
	 */
	public boolean containsControlNodeInFollowing(AbstractControlNode controlNode) {
		List<String> allFollowing = branches.stream().flatMap(Collection::stream).collect(Collectors.toList());
		return allFollowing.contains(controlNode.getId());
	}

	/**
	 * Automatically begins the Gateway.
	 */
	@Override
	public void enableControlFlow() {
		begin();
	}

	/**
	 * Instantiates the following ControlNodes (and starts automatic tasks).
	 */
	@Override
	public void begin() {
		setState(State.EXECUTING);
		getFragmentInstance().createFollowing(getControlNode());
		// TODO is it right to start automatic Tasks here?
		this.getFragmentInstance().getCase().getCaseExecutioner().startAutomaticTasks();
		// this.terminate();
	}

	/**
	 * Terminates an executing Gateway (sets its state to TERMINATED).
	 */
	@Override
	public void terminate() {
		if (!getState().equals(State.EXECUTING))
			return;
		setState(State.TERMINATED);
	}

	/**
	 * Skips the gateway (e.g. when this Gateway is part of a branch after an
	 * ExclusiveGateway but the user selects another branch) Sets the state of
	 * the Gateway to SKIPPED.
	 */
	@Override
	public void skip() {
		// TODO Auto-generated method stub
		this.setState(State.SKIPPED);
	}

	/**
	 * returns the ExclusiveGateway to this ExclusiveGatewayInstance.
	 */
	@Override
	public ExclusiveGateway getControlNode() {
		return (ExclusiveGateway) super.getControlNode();
	}
}
