package de.hpi.bpt.chimera.jcore.flowbehaviors;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.GatewayInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractControlNodeInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;

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
        ScenarioInstance scenarioInstance = this.getScenarioInstance();
        scenarioInstance.updateDataFlow();
        scenarioInstance.checkXorGatewaysForTermination(this.getControlNodeId());

        this.enableFollowing();
		this.runAutomaticTasks();
	}

    @Override
    public void skip() {

    }

    @Override protected AbstractControlNodeInstance createFollowingNodeInstance(
			int controlNodeId) {
		for (AbstractControlNodeInstance controlNodeInstance
				: this.getScenarioInstance().getControlNodeInstances()) {
			if (controlNodeId
					== controlNodeInstance.getControlNodeId()
					&& !controlNodeInstance.getClass().equals(
						ActivityInstance.class)
					&& !controlNodeInstance.getState().equals(State.TERMINATED)) {
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
		case "SendTask":
			if (!gatewayInstance.isAutomaticExecution()) {
				((ActivityInstance) controlNodeInstance).setAutomaticTask(false);
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
