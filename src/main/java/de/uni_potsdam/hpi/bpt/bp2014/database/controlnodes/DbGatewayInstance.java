package de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors.AbstractStateMachine;

/**
 * This class is the representation of a gateway instance in the database.
 * It provides the functionality to create a new gateway instance or get/set their state.
 */
public class DbGatewayInstance extends DbObject {
	/**
	 * This method gives you the type of a gateway[and/xor].
	 *
	 * @param gatewayInstanceId This is the database ID of a gateway instance.
	 * @return the type of the gateway instance as a String.
	 */
	public String getType(int gatewayInstanceId) {
		String sql = "SELECT type FROM gatewayinstance WHERE id = " + gatewayInstanceId;
		return this.executeStatementReturnsString(sql, "type");
	}

	/**
	 * This method gives you the state of the gateway.
	 *
	 * @param gatewayInstanceId This is the database ID of a gateway instance.
	 * @return the state of the gateway instance as a String.
	 */
	public AbstractStateMachine.STATE getState(int gatewayInstanceId) {
		String sql = "SELECT gateway_state FROM gatewayinstance "
				+ "WHERE id = " + gatewayInstanceId;
		String state = this.executeStatementReturnsString(sql, "gateway_state");
	    return AbstractStateMachine.STATE.valueOf(state.toUpperCase());
    }

	/**
	 * This method creates and saves a new gateway instance to the database.
	 *
	 * @param controlNodeInstanceId This is the database ID of a controlNode instance.
	 * @param type                   This is the desirable type of the gateway instance.
	 * @param state                  This is the desirable state of the gateway instance.
	 */
	public void createNewGatewayInstance(int controlNodeInstanceId, String type, String state) {
		String sql =
				"INSERT INTO gatewayinstance ("
						+ "id, gatewayinstance.type, gateway_state) "
						+ "VALUES (" + controlNodeInstanceId + ", '"
						+ type + "', '" + state + "')";
		this.executeUpdateStatement(sql);
	}

	/**
	 * This method sets the state of a gateway to a desirable one.
	 *
	 * @param id    This is the database ID of a gateway instance.
	 * @param state this is the desirable state of the gateway instance.
	 */
	public void setState(int id, String state) {
		String sql = "UPDATE gatewayinstance SET gateway_state = '" + state
				+ "' WHERE id = " + id;
		this.executeUpdateStatement(sql);
	}

}