package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.LinkedList;

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


public class DbControlNodeInstance extends DbObject {
    public Boolean existControlNodeInstance(int controlNode_id, int fragmentInstance_id) {
        String sql = "SELECT id FROM controlnodeinstance WHERE controlnode_id = " + controlNode_id + " AND fragmentinstance_id = " + fragmentInstance_id;
        return executeExistStatement(sql);
    }

    public int createNewControlNodeInstance(int controlNode_id, String controlNodeType, int fragmentInstance_id) {
        String sql = "INSERT INTO controlnodeinstance (Type, controlnode_id, fragmentinstance_id) VALUES ('" + controlNodeType + "', " + controlNode_id + ", " + fragmentInstance_id + ")";
        return executeInsertStatement(sql);
    }

    public int getControlNodeInstanceID(int controlNode_id, int fragmentInstance_id) {
        String sql = "SELECT id FROM controlnodeinstance WHERE controlnode_id = " + controlNode_id + " AND fragmentinstance_id = " + fragmentInstance_id;
        return this.executeStatementReturnsInt(sql, "id");
    }

    public LinkedList<Integer> getActivitiesForFragmentInstanceID(int fragmentInstance_id) {
        String sql = "SELECT controlnode_id FROM controlnodeinstance WHERE controlnodeinstance.Type = 'Activity' AND fragmentinstance_id = " + fragmentInstance_id;
        return this.executeStatementReturnsListInt(sql, "controlnode_id");
    }

    public LinkedList<Integer> getGatewaysForFragmentInstanceID(int fragmentInstance_id) {
        String sql = "SELECT controlnode_id FROM controlnodeinstance WHERE (controlnodeinstance.Type = 'AND' OR controlnodeinstance.Type = 'XOR') AND fragmentinstance_id = " + fragmentInstance_id;
        return this.executeStatementReturnsListInt(sql, "controlnode_id");
    }
}
