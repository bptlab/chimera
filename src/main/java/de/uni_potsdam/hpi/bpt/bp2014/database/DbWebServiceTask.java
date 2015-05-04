package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DbWebServiceTask extends DbObject {


    public String getLinkForControlNode(int controlNode_id){
        String sql = "SELECT link FROM webservicetasklink WHERE controlnode_id = " + controlNode_id;
        return this.executeStatementReturnsString(sql, "link");
    }

    public LinkedList<Integer> getAttributeIdsForControlNode(int controlNode_id){
        String sql = "SELECT DISTINCT dataattribute_id FROM `webservicetaskattribute` WHERE `controlnode_id` = " + controlNode_id;
        return this.executeStatementReturnsListInt(sql, "dataattribute_id");
    }

    public LinkedList<String> getKeys(int controlNode_id, int dataattribute_id){
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
                "("+ controlNodeID + ", '" + link + "', '" + method + "')";
        executeUpdateStatement(sql);
    }

    public void updateWebServiceTaskLink (int controlNodeID, String link) {
        String sql = "UPDATE webservicetasklink SET link = '" + link + "' WHERE controlnode_id = " + controlNodeID;
        executeUpdateStatement(sql);
    }

    public void deleteWebServiceTaskAtribute (int controlNodeID, int dataAttributeID) {
        String sql = "DELETE FROM webservicetaskattribute " +
                "WHERE controlnode_id = " + controlNodeID + " AND dataattribute_id = " + dataAttributeID;
        executeUpdateStatement(sql);
    }
    /**
     * Get all WebServiceTasks for one scenario
     * @param scenarioID The databaseID of the scenario
     * @return controlnodeIDs of WebServiceTasks that belong to the scenario
     */
    public LinkedList<Integer> getWebServiceTasks (int scenarioID) {
        String sql = "SELECT controlnode.id " +
                "FROM fragment, controlnode " +
                "WHERE fragment.id = controlnode.fragment_id " +
                "AND fragment.scenario_id = " + scenarioID + " " +
                "AND controlnode.type = WebServiceTask";
        return executeStatementReturnsListInt(sql, "controlnode.id");
    }

    public HashMap<Integer, List<String>> getComplexAttributeMap(int controlNode_id){

        HashMap<Integer, List<String>> result = new HashMap<>();
        LinkedList<Integer> attributes = getAttributeIdsForControlNode(controlNode_id);
        for (int attr : attributes) {
            String sql = "SELECT webservicetaskattribute.key FROM webservicetaskattribute WHERE dataattribute_id = " + attr + " ORDER BY webservicetaskattribute.order ASC";
            List<String> keys = executeStatementReturnsListString(sql, "key");
            result.put(attr, keys);
        }
        return result;
    }

    public String getMethod(int controlNode_id){
        String sql = "SELECT method FROM webservicetasklink WHERE `controlnode_id` = " + controlNode_id;
        return this.executeStatementReturnsString(sql, "method");
    }
}
