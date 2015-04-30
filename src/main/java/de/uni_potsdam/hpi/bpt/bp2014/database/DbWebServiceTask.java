package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.LinkedList;

/**
 * Created by jaspar.mang on 30.04.15.
 */
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

    public void insertWebServiceTaskLinkIntoDatabase(int controlNodeID, String link) {
        String sql = "INSERT INTO webservicetasklink VALUES " +
                "("+ controlNodeID + ", '" + link + "')";
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
}
