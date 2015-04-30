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
        String sql = "SELECT dataattribute_id FROM `webservicetaskattribute` WHERE `controlnode_id` = " + controlNode_id;
        return this.executeStatementReturnsListInt(sql, "dataattribute_id");
    }

    public LinkedList<String> getKeys(int controlNode_id, int dataattribute_id){
        String sql = "SELECT `key` FROM `webservicetaskattribute` WHERE `controlnode_id` = " + controlNode_id + " AND `dataattribute_id` = " + dataattribute_id + " ORDER BY `order` ASC";
        return this.executeStatementReturnsListString(sql, "key");
    }
}
