package de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.*;

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
	 * @param stateMachine     An instance from the class AbstractStateMachine.
	 */
	public ExclusiveGatewayJoinBehavior(GatewayInstance gatewayInstance,
			ScenarioInstance scenarioInstance, AbstractStateMachine stateMachine) {
		this.setScenarioInstance(scenarioInstance);
		this.setControlNodeInstance(gatewayInstance);
		this.setStateMachine(stateMachine);
	}

	@Override public void enableControlFlow() {
		Collection conditions = this.getDbControlFlow().getConditions(
				getControlNodeInstance().getControlNodeId()).values();
		boolean condition = true;
		if (conditions.size() > 0 && !conditions.iterator().next().equals("")) {
			condition = false;
		}
		if (condition) {
			((GatewayStateMachine) this.getStateMachine()).execute();
			((ExclusiveGatewaySplitBehavior) getControlNodeInstance()
					.getOutgoingBehavior()).execute();
		} else {
			((ExclusiveGatewaySplitBehavior) getControlNodeInstance()
					.getOutgoingBehavior()).evaluateConditions();
			getControlNodeInstance().terminate();
		}

	}
}
