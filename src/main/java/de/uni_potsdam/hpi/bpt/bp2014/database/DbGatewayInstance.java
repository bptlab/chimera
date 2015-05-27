package de.uni_potsdam.hpi.bpt.bp2014.database;


/**
 * This class is the representation of a gateway instance in the database.
 * It provides the functionality to create a new gateway instance or get/set their state.
 */
public class DbGatewayInstance extends DbObject {
    /**
     * This method gives you the type of a gateway[and/xor].
     *
     * @param gatewayInstance_id This is the database ID of a gateway instance.
     * @return the type of the gateway instance as a String.
     */
    public String getType(int gatewayInstance_id) {
        String sql = "SELECT type FROM gatewayinstance WHERE id = " + gatewayInstance_id;
        return this.executeStatementReturnsString(sql, "type");
    }

    /**
     * This method gives you the state of the gateway.
     *
     * @param gatewayInstance_id This is the database ID of a gateway instance.
     * @return the state of the gateway instance as a String.
     */
    public String getState(int gatewayInstance_id) {
        String sql = "SELECT gateway_state FROM gatewayinstance WHERE id = " + gatewayInstance_id;
        return this.executeStatementReturnsString(sql, "gateway_state");
    }

    /**
     * This method creates and saves a new gateway instance to the database.
     *
     * @param controlNodeInstance_id This is the database ID of a controlNode instance.
     * @param type                   This is the desirable type of the gateway instance.
     * @param state                  This is the desirable state of the gateway instance.
     */
    public void createNewGatewayInstance(int controlNodeInstance_id, String type, String state) {
        String sql = "INSERT INTO gatewayinstance (id, gatewayinstance.type, gateway_state) VALUES (" + controlNodeInstance_id + ", '" + type + "', '" + state + "')";
        this.executeUpdateStatement(sql);
    }

    /**
     * This method sets the state of a gateway to a desirable one.
     *
     * @param id    This is the database ID of a gateway instance.
     * @param state this is the desirable state of the gateway instance.
     */
    public void setState(int id, String state) {
        String sql = "UPDATE gatewayinstance SET gateway_state = '" + state + "' WHERE id = " + id;
        this.executeUpdateStatement(sql);
    }

}
