package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbGatewayInstance;

/**
 * This class represents a gateway state machine.
 */
public class GatewayStateMachine extends AbstractStateMachine {
	/**
	 * Database Connection objects
	 */
	private final DbGatewayInstance dbGatewayInstance = new DbGatewayInstance();

	private ScenarioInstance scenarioInstance;
	private AbstractControlNodeInstance controlNodeInstance;
	private String state;

	/**
	 * Initializes the GatewayStateMachine.
	 *
	 * @param gatewayId          This is the id of the gateway.
	 * @param scenarioInstance    This is an instance from the class ScenarioInstance.
	 * @param controlNodeInstance This is an AbstractControlNodeInstance.
	 */
	public GatewayStateMachine(int gatewayId, ScenarioInstance scenarioInstance,
			AbstractControlNodeInstance controlNodeInstance) {
		this.scenarioInstance = scenarioInstance;
		this.setControlNodeInstanceId(gatewayId);
		this.controlNodeInstance = controlNodeInstance;
		this.state = dbGatewayInstance.getState(
				controlNodeInstance.getControlNodeInstanceId());
		if ("executing".equals(state)) {
			scenarioInstance.getExecutingGateways().add(
					(GatewayInstance) controlNodeInstance);
		} else if ("terminated".equals(state) || "skipped".equals(state)) {
			scenarioInstance.getTerminatedControlNodeInstances()
					.add(controlNodeInstance);
		}
		scenarioInstance.getControlNodeInstances().add(controlNodeInstance);
	}

	/**
	 * Executes the gateway instance.
	 * Sets the state for the gateway instance in the database to executing.
	 */
	public void execute() {
		state = "executing";
		dbGatewayInstance.setState(controlNodeInstance.getControlNodeInstanceId(), state);
		scenarioInstance.getExecutingGateways().add((GatewayInstance) controlNodeInstance);
	}

	/**
	 * Terminates the gateway instance.
	 * Sets the state for the gateway instance in the database to terminated.
	 */
	@Override public boolean terminate() {
		state = "terminated";
		dbGatewayInstance.setState(controlNodeInstance.getControlNodeInstanceId(), state);
		scenarioInstance.getExecutingGateways().remove(controlNodeInstance);
		scenarioInstance.getTerminatedControlNodeInstances().add(controlNodeInstance);
		return true;
	}

	/**
	 * Skips the gateway instance.
	 * Sets the state for the gateway instance in the database to skipped.
	 */
	@Override public boolean skip() {
		state = "skipped";
		dbGatewayInstance.setState(controlNodeInstance.getControlNodeInstanceId(), state);
		scenarioInstance.getExecutingGateways().remove(controlNodeInstance);
		scenarioInstance.getTerminatedControlNodeInstances().add(controlNodeInstance);
		return true;
	}
}
