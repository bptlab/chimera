package de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.AbstractControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.GatewayInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.AbstractParallelOutgoingBehavior;

/**
 *
 */
public class ParallelGatewaySplitBehavior extends AbstractParallelOutgoingBehavior {
	private GatewayInstance gatewayInstance;

	/**
	 * Initializes the ParallelGatewaySplitBehavior.
	 *
	 * @param gatewayId          This is the database id from the gateway.
	 * @param scenarioInstance    This is an instance from the class ScenarioInstance.
	 * @param fragmentInstanceId This is the database id from the fragment instance.
	 * @param gatewayInstance     This is an instance from the class GatewayInstance.
	 */
    public ParallelGatewaySplitBehavior(int gatewayId, ScenarioInstance scenarioInstance,
                                        int fragmentInstanceId, GatewayInstance gatewayInstance) {
		this.setControlNodeId(gatewayId);
		this.setScenarioInstance(scenarioInstance);
		this.setFragmentInstanceId(fragmentInstanceId);
		this.gatewayInstance = gatewayInstance;
	}

	@Override public void terminate() {
		this.checkAfterTermination();
		this.enableFollowing();
		this.runAfterTermination();
	}

	@Override protected AbstractControlNodeInstance createFollowingNodeInstance(
			int controlNodeId) {
		for (AbstractControlNodeInstance controlNodeInstance
				: this.getScenarioInstance().getControlNodeInstances()) {
			if (controlNodeId
					== controlNodeInstance.getControlNodeId()
					&& !controlNodeInstance.getClass().equals(
						ActivityInstance.class)
					&& !controlNodeInstance.getStateMachine()
					.getState().equals("terminated")) {
				return controlNodeInstance;
			}
		}
		String type = this.getDbControlNode().getType(controlNodeId);
		AbstractControlNodeInstance controlNodeInstance
				= createControlNode(type, controlNodeId);
		this.setAutomaticExecutionToFalse(type, controlNodeInstance);
		return controlNodeInstance;
	}

	/**
	 * @param type a type for automatic execution.
	 * @param controlNodeInstance a control node instance.
	 */
	private void setAutomaticExecutionToFalse(String type,
			AbstractControlNodeInstance controlNodeInstance) {
		switch (type) {
		case "Activity":
		case "EmailTask":
		case "WebServiceTask":
			if (!gatewayInstance.isAutomaticExecution()) {
				((ActivityInstance) controlNodeInstance)
						.setAutomaticExecution(false);
			}
			break;
		case "AND":
			if (!gatewayInstance.isAutomaticExecution()) {
				((GatewayInstance) controlNodeInstance)
						.setAutomaticExecution(false);
			}
			break;
		default:
		}
	}
}
