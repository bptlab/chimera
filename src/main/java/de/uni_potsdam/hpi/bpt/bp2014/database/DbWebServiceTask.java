package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class DbWebServiceTask extends DbObject {

    /**
     * Returns the Link for the webservice task
     *
     * @param controlNode_id The controlnode_id of the webservice task.
     * @return a String with the url.
     */
    public String getLinkForControlNode(int controlNode_id) {
        String sql = "SELECT link FROM webservicetasklink WHERE controlnode_id = " + controlNode_id;
        return this.executeStatementReturnsString(sql, "link");
    }

    /**
     * Get a List with all ids from the attributes which get changed by the webservice task.
     *
     * @param controlNode_id The controlnode_id of the webservice task.
     * @return a List with all Attribute ids for the webservice task.
     */
    public LinkedList<Integer> getAttributeIdsForControlNode(int controlNode_id) {
        String sql = "SELECT DISTINCT dataattribute_id FROM `webservicetaskattribute` WHERE `controlnode_id` = " + controlNode_id;
        return this.executeStatementReturnsListInt(sql, "dataattribute_id");
    }

    /**
     * Get the keys for the JSON for a webservice task and a data attribute.
     *
     * @param controlNode_id   The controlnode_id of the webservice task.
     * @param dataattribute_id The id of the data attribute.
     * @return a List of the keys for the JSON
     */
    public LinkedList<String> getKeys(int controlNode_id, int dataattribute_id) {
        String sql = "SELECT `key` FROM `webservicetaskattribute` WHERE `controlnode_id` = " + controlNode_id + " AND `dataattribute_id` = " + dataattribute_id + " ORDER BY `order` ASC";
        return this.executeStatementReturnsListString(sql, "key");
    }

    /**
     * Insert a entry in the database table webservicetaskattribute.
     *
     * @param order           The order of the entries.
     * @param controlNodeID   The controlnode_id of the webservice task.
     * @param dataAttributeID The id of the data attribute.
     * @param key             The Key to get information from the JSON.
     */
    public void insertWebServiceTaskAttributeIntoDatabase(int order, int controlNodeID, int dataAttributeID, String key) {
        String sql = "INSERT INTO webservicetaskattribute VALUES " +
                "(" + order + ", " + controlNodeID + ", " + dataAttributeID + ", '" + key + "')";
        executeUpdateStatement(sql);
    }

    /**
     * Insert a entry in the database table webservicetasklink.
     *
     * @param controlNodeID The controlnode_id of the webservice task.
     * @param link          The Link for the webservice task.
     * @param method        The method GET/PUT/POST.
     */
    public void insertWebServiceTaskLinkIntoDatabase(int controlNodeID, String link, String method) {
        String sql = "INSERT INTO webservicetasklink VALUES " +
                "(" + controlNodeID + ", '" + link + "', '" + method + "')";
        executeUpdateStatement(sql);
    }

    /**
     * Insert a entry in the database table webservicetaskpost.
     *
     * @param controlNodeID The controlnode_id of the webservice task.
     * @param post          A JSON that is used by the PUT/POST.
     */
    public void insertWebServiceTaskPOSTIntoDatabase(int controlNodeID, String post) {
        String sql = "INSERT INTO webservicetaskpost VALUES " +
                "(" + controlNodeID + ", '" + post + "')";
        executeUpdateStatement(sql);
    }

    /**
     * Updates the link for the given webservice task.
     *
     * @param controlNodeID The controlnode_id of the webservice task.
     * @param link          The Link for the webservice task.
     */
    public void updateWebServiceTaskLink(int controlNodeID, String link) {
        String sql = "UPDATE webservicetasklink SET link = '" + link + "' WHERE controlnode_id = " + controlNodeID;
        executeUpdateStatement(sql);
    }

    /**
     * Updates the method for the given webservice task.
     *
     * @param controlNodeID The controlnode_id of the webservice task.
     * @param method        The method GET/PUT/POST.
     */
    public void updateWebServiceTaskMethod(int controlNodeID, String method) {
        String sql = "UPDATE webservicetasklink SET method = '" + method + "' WHERE controlnode_id = " + controlNodeID;
        executeUpdateStatement(sql);
    }

    /**
     * Updates the post for the given webservice task.
     *
     * @param controlNodeID The controlnode_id of the webservice task.
     * @param post          A JSON that is used by the PUT/POST.
     */
    public void updateWebServiceTaskPOST(int controlNodeID, String post) {
        String sql = "UPDATE webservicetaskpost SET post = '" + post + "' WHERE controlnode_id = " + controlNodeID;
        executeUpdateStatement(sql);
    }

    /**
     * Deletes all entries in the database table webservicetaskattribute for the given webservice task
     * and the given data attribute.
     *
     * @param controlNodeID   The controlnode_id of the webservice task.
     * @param dataAttributeID The id of the data attribute.
     */
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

    /**
     * Returns the whole database table webservicetaskattribute in a Map.
     *
     * @return a Map with the whole database table webservicetaskattribute.
     */
    public ArrayList<HashMap<String, Object>> getAllWebServiceTaskAttributeFancy() {
        String sql = "SELECT *  FROM webservicetaskattribute";
        return executeStatementReturnsHashMap(sql);
    }

    /**
     * Returns a Map with all entries in the database for the given webservice task.
     *
     * @param controlnode_id The id of the webservice task.
     * @return a Map with all database entries for the given webservice task.
     */
    public ArrayList<HashMap<String, Object>> getComplexAttributeMap(int controlnode_id) {
        String sql = "SELECT *  FROM webservicetaskattribute WHERE controlnode_id = " + controlnode_id;
        return executeStatementReturnsHashMap(sql);
    }

    /**
     * Get the method GET/PUT/POST.
     *
     * @param controlNode_id The controlnode_id of the webservice task.
     * @return a String whith GET/PUT/POST.
     */
    public String getMethod(int controlNode_id) {
        String sql = "SELECT method FROM webservicetasklink WHERE `controlnode_id` = " + controlNode_id;
        return this.executeStatementReturnsString(sql, "method");
    }

    /**
     * Returns the JSON String for the POST/PUT.
     *
     * @param controlNode_id The controlnode_id of the webservice task.
     * @return The JSON String for the POST/PUT.
     */
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

    /**
     * Deletes all Attribute entries for the webservice Task.
     *
     * @param webserviceID The controlnode_id of the webservice task.
     */
    public void deleteAllAttributes(int webserviceID) {
        String sql = "DELETE FROM webservicetaskattribute WHERE controlnode_id = " + webserviceID;
        executeUpdateStatement(sql);
    }
}
