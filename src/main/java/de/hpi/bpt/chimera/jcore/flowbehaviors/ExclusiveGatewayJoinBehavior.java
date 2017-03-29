package de.hpi.bpt.chimera.jcore.flowbehaviors;

import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.GatewayInstance;

import java.util.Collection;

/**
 * This class deals with the join behavior of exclusive gateways.
 */
public class ExclusiveGatewayJoinBehavior extends AbstractIncomingBehavior {

	/**
	 * Initializes and creates an ExclusiveGatewayJoinBehavior.
	 *
	 * @param gatewayInstance  An instance from the class GatewayInstance.
	 * @param scenarioInstance An instance from the class ScenarioInstance.
	 */
	public ExclusiveGatewayJoinBehavior(GatewayInstance gatewayInstance, ScenarioInstance scenarioInstance) {
		this.setScenarioInstance(scenarioInstance);
		this.setControlNodeInstance(gatewayInstance);
	}

	@Override
	public void enableControlFlow() {
		// Let the outgoing behavior check the conditions!
		//	Collection conditions = this.getDbControlFlow().getConditions(getControlNodeInstance().getControlNodeId()).values();
		//	if (!conditions.isEmpty() && !"".equals(conditions.iterator().next())) {
		//		((ExclusiveGatewaySplitBehavior) getControlNodeInstance().getOutgoingBehavior()).evaluateConditions();
		//	}
		getControlNodeInstance().terminate();
	}
}
