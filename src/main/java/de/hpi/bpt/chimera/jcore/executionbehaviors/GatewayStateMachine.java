package de.hpi.bpt.chimera.jcore.executionbehaviors;

import de.hpi.bpt.chimera.database.controlnodes.DbGatewayInstance;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractControlNodeInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.GatewayInstance;

/**
 * This class represents a gateway state machine.
 */
public class GatewayStateMachine {
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

	}

	/**
	 * Executes the gateway instance.
	 * Sets the state for the gateway instance in the database to executing.
	 */
	public void execute() {
	}
}
