package de.hpi.bpt.chimera.jcore.executionbehaviors;

import de.hpi.bpt.chimera.database.controlnodes.DbGatewayInstance;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractControlNodeInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.GatewayInstance;

/**
 * This class represents a gateway state machine.
 */
public class GatewayStateMachine extends AbstractStateMachine {
	/**
	 * Database ConnectionWrapper objects
	 */
	private final DbGatewayInstance dbGatewayInstance = new DbGatewayInstance();

	/**
	 * Initializes the GatewayStateMachine.
	 *
	 * @param gatewayId          This is the id of the gateway.
	 * @param scenarioInstance    This is an instance from the class ScenarioInstance.
	 * @param controlNodeInstance This is an AbstractControlNodeInstance.
	 */
	public GatewayStateMachine(int gatewayId, ScenarioInstance scenarioInstance,
			AbstractControlNodeInstance controlNodeInstance) {
		this.setScenarioInstance(scenarioInstance);
		this.setControlNodeInstanceId(gatewayId);
		this.setControlNodeInstance(controlNodeInstance);
		this.setState(dbGatewayInstance.getState(
				controlNodeInstance.getControlNodeInstanceId()));
		if (STATE.EXECUTING.equals(getState())) {
			scenarioInstance.getExecutingGateways().add(
					(GatewayInstance) controlNodeInstance);
		} else {
			if (STATE.TERMINATED.equals(getState()) || STATE.SKIPPED.equals(getState())) {
				scenarioInstance.getTerminatedControlNodeInstances()
						.add(controlNodeInstance);
			}
		}
		scenarioInstance.getControlNodeInstances().add(controlNodeInstance);
	}

	/**
	 * Executes the gateway instance.
	 * Sets the state for the gateway instance in the database to executing.
	 */
	public void execute() {
		setState(STATE.EXECUTING);
		dbGatewayInstance.setState(
				getControlNodeInstance().getControlNodeInstanceId(), getState().toString());
		getScenarioInstance().getExecutingGateways()
				.add((GatewayInstance) getControlNodeInstance());
	}

	/**
	 * Terminates the gateway instance.
	 * Sets the state for the gateway instance in the database to terminated.
	 */
	@Override public boolean terminate() {
		setState(STATE.TERMINATED);
		dbGatewayInstance.setState(
				getControlNodeInstance().getControlNodeInstanceId(), getState().toString());
		getScenarioInstance().getExecutingGateways().remove(getControlNodeInstance());
		getScenarioInstance().getTerminatedControlNodeInstances()
				.add(getControlNodeInstance());
		return true;
	}

	/**
	 * Skips the gateway instance.
	 * Sets the state for the gateway instance in the database to skipped.
	 */
	@Override public boolean skip() {
		setState(STATE.SKIPPED);
		dbGatewayInstance.setState(
				getControlNodeInstance().getControlNodeInstanceId(), getState().toString());
		getScenarioInstance().getExecutingGateways().remove(getControlNodeInstance());
		getScenarioInstance().getTerminatedControlNodeInstances()
				.add(getControlNodeInstance());
		return true;
	}
}
