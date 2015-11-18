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
		if ("executing".equals(getState())) {
			scenarioInstance.getExecutingGateways().add(
					(GatewayInstance) controlNodeInstance);
		} else {
			if ("terminated".equals(getState()) || "skipped".equals(getState())) {
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
		setState("executing");
		dbGatewayInstance.setState(
				getControlNodeInstance().getControlNodeInstanceId(), getState());
		getScenarioInstance().getExecutingGateways()
				.add((GatewayInstance) getControlNodeInstance());
	}

	/**
	 * Terminates the gateway instance.
	 * Sets the state for the gateway instance in the database to terminated.
	 */
	@Override public boolean terminate() {
		setState("terminated");
		dbGatewayInstance.setState(
				getControlNodeInstance().getControlNodeInstanceId(), getState());
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
		setState("skipped");
		dbGatewayInstance.setState(
				getControlNodeInstance().getControlNodeInstanceId(), getState());
		getScenarioInstance().getExecutingGateways().remove(getControlNodeInstance());
		getScenarioInstance().getTerminatedControlNodeInstances()
				.add(getControlNodeInstance());
		return true;
	}
}
