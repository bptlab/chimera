package de.hpi.bpt.chimera.jcore.flowbehaviors;

import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.GatewayInstance;
import de.hpi.bpt.chimera.jcore.executionbehaviors.GatewayStateMachine;

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
	public ExclusiveGatewayJoinBehavior(GatewayInstance gatewayInstance,
			ScenarioInstance scenarioInstance) {
		this.setScenarioInstance(scenarioInstance);
		this.setControlNodeInstance(gatewayInstance);
	}

	@Override public void enableControlFlow() {
		Collection conditions = this.getDbControlFlow().getConditions(
				getControlNodeInstance().getControlNodeId()).values();
		boolean condition = true;
		if (conditions.size() > 0 && !conditions.iterator().next().equals("")) {
			condition = false;
		}
//		if (condition) {
//
//            //GatewayStateMachine stateMachine =
//                    (GatewayStateMachine) this.getControlNodeInstance().getStateMachine();
//            if (null != stateMachine) {
//                stateMachine.execute();
//            }
//
//			((ExclusiveGatewaySplitBehavior) getControlNodeInstance()
//					.getOutgoingBehavior()).execute();
//		} else {
//			((ExclusiveGatewaySplitBehavior) getControlNodeInstance()
//					.getOutgoingBehavior()).evaluateConditions();
//			getControlNodeInstance().terminate();
//		}
	}
}
