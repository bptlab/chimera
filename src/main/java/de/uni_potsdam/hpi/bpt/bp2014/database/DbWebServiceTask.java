package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class DbWebServiceTask extends DbObject {


    public String getLinkForControlNode(int controlNode_id) {
        String sql = "SELECT link FROM webservicetasklink WHERE controlnode_id = " + controlNode_id;
        return this.executeStatementReturnsString(sql, "link");
    }

    public LinkedList<Integer> getAttributeIdsForControlNode(int controlNode_id) {
        String sql = "SELECT DISTINCT dataattribute_id FROM `webservicetaskattribute` WHERE `controlnode_id` = " + controlNode_id;
        return this.executeStatementReturnsListInt(sql, "dataattribute_id");
    }

    public LinkedList<String> getKeys(int controlNode_id, int dataattribute_id) {
        String sql = "SELECT `key` FROM `webservicetaskattribute` WHERE `controlnode_id` = " + controlNode_id + " AND `dataattribute_id` = " + dataattribute_id + " ORDER BY `order` ASC";
        return this.executeStatementReturnsListString(sql, "key");
    }

    public void insertWebServiceTaskAttributeIntoDatabase(int order, int controlNodeID, int dataAttributeID, String key) {
        String sql = "INSERT INTO webservicetaskattribute VALUES " +
                "(" + order + ", " + controlNodeID + ", " + dataAttributeID + ", '" + key + "')";
        executeUpdateStatement(sql);
    }

    public void insertWebServiceTaskLinkIntoDatabase(int controlNodeID, String link, String method) {
        String sql = "INSERT INTO webservicetasklink VALUES " +
                "(" + controlNodeID + ", '" + link + "', '" + method + "')";
        executeUpdateStatement(sql);
    }

    public void insertWebServiceTaskPOSTIntoDatabase(int controlNodeID, String post) {
        String sql = "INSERT INTO webservicetaskpost VALUES " +
                "(" + controlNodeID + ", '" + post + "')";
        executeUpdateStatement(sql);
    }


    public void updateWebServiceTaskLink(int controlNodeID, String link) {
        String sql = "UPDATE webservicetasklink SET link = '" + link + "' WHERE controlnode_id = " + controlNodeID;
        executeUpdateStatement(sql);
    }

    public void updateWebServiceTaskMethod(int controlNodeID, String method) {
        String sql = "UPDATE webservicetasklink SET method = '" + method + "' WHERE controlnode_id = " + controlNodeID;
        executeUpdateStatement(sql);
    }

    public void updateWebServiceTaskPOST(int controlNodeID, String post) {
        String sql = "UPDATE webservicetaskpost SET post = '" + post + "' WHERE controlnode_id = " + controlNodeID;
        executeUpdateStatement(sql);
    }

    public void deleteWebServiceTaskAtribute(int controlNodeID, int dataAttributeID) {
        String sql = "DELETE FROM webservicetaskattribute " +
                "WHERE controlnode_id = " + controlNodeID + " AND dataattribute_id = " + dataAttributeID;
        executeUpdateStatement(sql);
    }

    /**
     * Get all WebServiceTasks for one scenario.
     *
     * @param scenarioID The databaseID of the scenario.
     * @return controlNodeIDs of WebServiceTasks that belong to the scenario.
     */
    public LinkedList<Integer> getWebServiceTasks(int scenarioID) {
        String sql = "SELECT controlnode.id " +
                "FROM fragment, controlnode " +
                "WHERE fragment.id = controlnode.fragment_id " +
                "AND fragment.scenario_id = " + scenarioID + " " +
                "AND controlnode.type = 'WebServiceTask'";
        return executeStatementReturnsListInt(sql, "controlnode.id");
    }

    public ArrayList<HashMap<String, Object>> getAllWebServiceTaskAttributeFancy() {
        String sql = "SELECT *  FROM webservicetaskattribute";
        return executeStatementReturnsHashMap(sql);
    }

    public ArrayList<HashMap<String, Object>> getComplexAttributeMap(int controlnode_id) {
        String sql = "SELECT *  FROM webservicetaskattribute WHERE controlnode_id = " + controlnode_id;
        return executeStatementReturnsHashMap(sql);
    }

    public String getMethod(int controlNode_id) {
        String sql = "SELECT method FROM webservicetasklink WHERE `controlnode_id` = " + controlNode_id;
        return this.executeStatementReturnsString(sql, "method");
    }

    public String getPOST(int controlNode_id) {
        String sql = "SELECT post FROM webservicetaskpost WHERE `controlnode_id` = " + controlNode_id;
        return this.executeStatementReturnsString(sql, "post");
    }

    /**
     * Checks if there are entries in the table WebServiceTaskLink in the database.
     *
     * @param controlNode_id The id of the webservice task.
     * @return true if there is entry. false if not.
     */
    public boolean existWebServiceTaskIDinLink(int controlNode_id) {
        String sql = "Select controlnode_id from webservicetasklink WHERE controlnode_id = " + controlNode_id;
        return executeExistStatement(sql);
    }

    /**
     * Checks if there are entries in the table WebServiceTaskPost in the database.
     *
     * @param controlNode_id The id of the webservice task.
     * @return true if there is entry. false if not.
     */
    public boolean existWebServiceTaskIDinPost(int controlNode_id) {
        String sql = "Select controlnode_id from webservicetaskpost WHERE controlnode_id = " + controlNode_id;
        return executeExistStatement(sql);
    }

    /**
     * Return a list of all dataattributeIDs and its names that are accessable in any dataobject of the outputset of the webserviceTask
     *
     * @param webserviceID The controlnode_id of the webservice task.
     * @return A map of all dataattributeIDs and its names
     */
    public Map<Integer, String> getOutputAttributesForWebservice(int webserviceID) {
        String sql = "SELECT name, id  FROM dataattribute WHERE dataclass_id IN" +
                        "(SELECT DISTINCT dataclass_id FROM datanode WHERE id IN" +
                            "(SELECT DISTINCT datanode_id FROM datasetconsistsofdatanode WHERE dataset_id IN " +
                                "(SELECT DISTINCT dataset_id FROM dataflow WHERE input = 0 AND controlnode_id = " + webserviceID + " )))";
        return executeStatementReturnsMap(sql, "id", "name");
    }

    public boolean deleteAllAttributes(int webserviceID) {
        String sql = "DELETE FROM webservicetaskattribute WHERE controlnode_id = " + webserviceID;
        return executeExistStatement(sql);
    }
}
