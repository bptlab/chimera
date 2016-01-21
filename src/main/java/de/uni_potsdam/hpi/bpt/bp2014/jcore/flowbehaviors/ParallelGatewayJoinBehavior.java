package de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.GatewayInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.AbstractIncomingBehavior;

import java.util.LinkedList;

/**
 * This class implements join behavior for parallel gateways.
 */
public class ParallelGatewayJoinBehavior extends AbstractIncomingBehavior {
	/**
	 * Database Connection objects
	 */
	private final DbControlFlow dbControlFlow = new DbControlFlow();
	private final DbControlNode dbControlNode = new DbControlNode();

	/**
	 * Initializes the ParallelGatewayJoinBehavior.
	 *
	 * @param gatewayInstance  This is an instance from the class GatewayInstance.
	 * @param scenarioInstance This is an instance from the class ScenarioInstance.
	 */
    public ParallelGatewayJoinBehavior(GatewayInstance gatewayInstance,
                                       ScenarioInstance scenarioInstance) {
		this.setScenarioInstance(scenarioInstance);
		this.setControlNodeInstance(gatewayInstance);
	}

	@Override public void enableControlFlow() {
		if (checkEnabled()) {
			this.getControlNodeInstance().terminate();
		}
	}

	/**
	 * Checks if all control node instances before this gateway have been terminated
	 *
	 * @return true if this gateway can get enabled. false if not.
	 */
	private Boolean checkEnabled() {
		LinkedList<Integer> predecessors = dbControlFlow
				.getPredecessorControlNodes(this.getControlNodeInstance()
						.getControlNodeId());
		//if a start Event ist before this Gateway it is enabled
		if (predecessors.size() == 1 && dbControlNode.getType(predecessors.get(0))
				.equals("Startevent")) {
			return true;
		}
		//looks that all predecessors are terminated
		for (int controlNode : predecessors) {
			if (!this.getScenarioInstance()
					.terminatedControlNodeInstancesContainControlNodeID(
							controlNode)
					&& !this.getScenarioInstance()
					.executingGatewaysContainControlNodeID(controlNode)) {
				return false;
			}
		}
		return true;
	}
}
