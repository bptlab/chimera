package de.uni_potsdam.hpi.bpt.bp2014.database;


/**
 * ********************************************************************************
 * <p/>
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 * <p/>
 * ******************************************************************
 * <p/>
 * Copyright © All Rights Reserved 2014 - 2015
 * <p/>
 * Please be aware of the License. You may found it in the root directory.
 * <p/>
 * **********************************************************************************
 */


public class DbGatewayInstance extends DbObject {
    public String getType(int gatewayInstance_id) {
        String sql = "SELECT type FROM gatewayinstance WHERE id = " + gatewayInstance_id;
        return this.executeStatementReturnsString(sql, "type");
    }

    public String getState(int gatewayInstance_id) {
        String sql = "SELECT gateway_state FROM gatewayinstance WHERE id = " + gatewayInstance_id;
        return this.executeStatementReturnsString(sql, "gateway_state");
    }

    public void createNewGatewayInstance(int controlNodeInstance_id, String type, String state) {
        String sql = "INSERT INTO gatewayinstance (id, gatewayinstance.type, gateway_state) VALUES (" + controlNodeInstance_id + ", '" + type + "', '" + state + "')";
        this.executeUpdateStatement(sql);
    }

    public void setState(int id, String state) {
        String sql = "UPDATE gatewayinstance SET gateway_state = '" + state + "' WHERE id = " + id;
        this.executeUpdateStatement(sql);
    }

}
