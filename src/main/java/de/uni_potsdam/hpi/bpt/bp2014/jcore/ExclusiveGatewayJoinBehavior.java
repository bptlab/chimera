package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import java.util.Collection;

/**
 * This class deals with the join behavior of exclusive gateways.
 */
public class ExclusiveGatewayJoinBehavior extends AbstractIncomingBehavior {
	private AbstractControlNodeInstance controlNodeInstance;

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
		this.controlNodeInstance = gatewayInstance;
		this.setStateMachine(stateMachine);
	}

	@Override public void enableControlFlow() {
		Collection conditions = this.getDbControlFlow().getConditions(
				controlNodeInstance.getControlNodeId()).values();
		boolean condition = true;
		if (conditions.size() > 0 && !conditions.iterator().next().equals("")) {
			condition = false;
		}
		if (condition) {
			((GatewayStateMachine) this.getStateMachine()).execute();
			((ExclusiveGatewaySplitBehavior) controlNodeInstance
					.getOutgoingBehavior()).execute();
		} else {
			((ExclusiveGatewaySplitBehavior) controlNodeInstance.getOutgoingBehavior())
					.evaluateConditions();
			controlNodeInstance.terminate();
		}

	}
}
