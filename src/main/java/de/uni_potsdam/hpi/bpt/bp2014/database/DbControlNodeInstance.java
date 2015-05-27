package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.LinkedList;



/**
 * This class is the representation of a controlNode instance in the database.
 * It provides the functionality to check for existing instances as well as creating new ones.
 * Moreover it can retrieve all activities/gateways belonging to a specific fragment instance.
 */
public class DbControlNodeInstance extends DbObject {
    /**
     * This method checks if a controlNode instance is existing in the database belonging to a controlNode and a fragment instance.
     *
     * @param controlNode_id      This is the database ID of a controlNode.
     * @param fragmentInstance_id This is the database ID of a fragment instance.
     * @return true if the controlNode instance exists els false.
     */
    public Boolean existControlNodeInstance(int controlNode_id, int fragmentInstance_id) {
        String sql = "SELECT id FROM controlnodeinstance WHERE controlnode_id = " + controlNode_id + " AND fragmentinstance_id = " + fragmentInstance_id;
        return executeExistStatement(sql);
    }

    public Boolean existControlNodeInstance(int controlNodeInstance_id) {
        String sql = "SELECT id FROM controlnodeinstance WHERE id = " + controlNodeInstance_id;
        return executeExistStatement(sql);
    }

    /**
     * This method creates and saves a new controlNode instance to the database in the context of a fragment instance.
     *
     * @param controlNode_id      This is the database ID of a controlNode.
     * @param controlNodeType     This is the desirable type of the new controlNode instance.
     * @param fragmentInstance_id This is the database ID of a fragment instance.
     * @return -1 if something went wrong else the database ID of the newly created controlNode instance.
     */
    public int createNewControlNodeInstance(int controlNode_id, String controlNodeType, int fragmentInstance_id) {
        String sql = "INSERT INTO controlnodeinstance (Type, controlnode_id, fragmentinstance_id) VALUES ('" + controlNodeType + "', " + controlNode_id + ", " + fragmentInstance_id + ")";
        return executeInsertStatement(sql);
    }

    /**
     * This method returns the database ID of a controlNode instance belonging to a controlNode and fragment instance.
     *
     * @param controlNode_id      This is the database ID of a controlNode.
     * @param fragmentInstance_id This is the database ID of a fragment instance.
     * @return -1 if something went wrong else the database ID of a controlNode instance.
     */
    public int getControlNodeInstanceID(int controlNode_id, int fragmentInstance_id) {
        String sql = "SELECT id FROM controlnodeinstance WHERE controlnode_id = " + controlNode_id + " AND fragmentinstance_id = " + fragmentInstance_id;
        return this.executeStatementReturnsInt(sql, "id");
    }

    public LinkedList<Integer> getControlNodeInstanceIDs(int controlNode_id, int fragmentInstance_id) {
        String sql = "SELECT id FROM controlnodeinstance WHERE controlnode_id = " + controlNode_id + " AND fragmentinstance_id = " + fragmentInstance_id;
        return this.executeStatementReturnsListInt(sql, "id");
    }

    /**
     * This method returns all database ID's of all activities belonging to a fragment instance.
     *
     * @param fragmentInstance_id This is the database ID of a fragment instance.
     * @return a list of database ID's of all activities of this fragment instance.
     */
    public LinkedList<Integer> getActivitiesForFragmentInstanceID(int fragmentInstance_id) {
        String sql = "SELECT controlnode_id FROM controlnodeinstance WHERE controlnodeinstance.Type = 'Activity' AND fragmentinstance_id = " + fragmentInstance_id;
        return this.executeStatementReturnsListInt(sql, "controlnode_id");
    }

    public LinkedList<Integer> getActivityInstancesForFragmentInstanceID(int fragmentInstance_id) {
        String sql = "SELECT id FROM controlnodeinstance WHERE controlnodeinstance.Type = 'Activity' AND fragmentinstance_id = " + fragmentInstance_id;
        return this.executeStatementReturnsListInt(sql, "id");
    }

    /**
     * This method returns all database ID's for all gateways belonging to a specific fragment instance.
     *
     * @param fragmentInstance_id This is the database ID of a fragment instance.
     * @return a list of database ID's of gateways belonging to this fragment instance.
     */
    public LinkedList<Integer> getGatewaysForFragmentInstanceID(int fragmentInstance_id) {
        String sql = "SELECT controlnode_id FROM controlnodeinstance WHERE (controlnodeinstance.Type = 'AND' OR controlnodeinstance.Type = 'XOR') AND fragmentinstance_id = " + fragmentInstance_id;
        return this.executeStatementReturnsListInt(sql, "controlnode_id");
    }

    /**
     * This method returns the controlNodeID of a controlNodeInstance.
     *
     * @param controlNodeInstanceID ID of the controlNodeInstance.
     * @return controlNodeID.
     */
    public int getControlNodeID(int controlNodeInstanceID) {
        String sql = "SELECT controlnode_id FROM controlnodeinstance WHERE id = " + controlNodeInstanceID;
        return this.executeStatementReturnsInt(sql, "controlnode_id");
    }

    public LinkedList<Integer> getGatewayInstancesForFragmentInstanceID(int fragmentInstance_id) {
        String sql = "SELECT id FROM controlnodeinstance WHERE (controlnodeinstance.Type = 'AND' OR controlnodeinstance.Type = 'XOR') AND fragmentinstance_id = " + fragmentInstance_id;
        return this.executeStatementReturnsListInt(sql, "id");
    }
}
