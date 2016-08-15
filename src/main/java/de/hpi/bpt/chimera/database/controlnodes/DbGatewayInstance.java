package de.hpi.bpt.chimera.database.controlnodes;

import de.hpi.bpt.chimera.database.DbObject;
import de.hpi.bpt.chimera.jcore.controlnodes.State;

/**
 * This class is the representation of a gateway instance in the database.
 * It provides the functionality to create a new gateway instance or get/set their state.
 */
public class DbGatewayInstance extends DbObject {
	/**
	 * This method gives you the type of a gateway[and/xor].
	 *
	 * @param id This is the database ID of a gateway instance.
	 * @return the type of the gateway instance as a String.
	 */
	public String getType(int id) {
		String sql = "SELECT type FROM gatewayinstance WHERE id = " + id;
		return this.executeStatementReturnsString(sql, "type");
	}

	/**
	 * This method gives you the state of the gateway.
	 *
	 * @param id This is the database ID of a gateway instance.
	 * @return the state of the gateway instance as a String.
	 */
	public State getState(int id) {
		String sql = "SELECT gateway_state FROM gatewayinstance "
				+ "WHERE id = " + id;
		String state = this.executeStatementReturnsString(sql, "gateway_state");
 	    return State.valueOf(state.toUpperCase());
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
