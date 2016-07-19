package de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.GatewayInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;


import java.util.List;

/**
 * This class implements join behavior for parallel gateways.
 */
public class ParallelGatewayJoinBehavior extends AbstractIncomingBehavior {
	/**
	 * Database ConnectionWrapper objects
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
		List<Integer> predecessors = dbControlFlow
				.getPredecessorControlNodes(this.getControlNodeInstance()
						.getControlNodeId());
		//if a start Event ist before this Gateway it is enabled
		if (predecessors.size() == 1 && dbControlNode.getType(predecessors.get(0))
				.equals("StartEvent")) {
			return true;
		}
		//looks that all predecessors are terminated
		for (int controlNode : predecessors) {
			if (!this.getScenarioInstance().terminatedControlNodeInstancesContainControlNodeID(
							controlNode)
					&& !this.getScenarioInstance()
					.executingGatewaysContainControlNodeID(controlNode)) {
				return false;
			}
		}
		return true;
	}
}
