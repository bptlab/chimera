package de.uni_potsdam.hpi.bpt.bp2014.database;


/**
 * Created by jaspar.mang on 28.01.15.
 */
public class DbEmailConfiguration extends DbObject {
    public String getReceiverEmailAddress(int controlNode_id) {
        String sql = "SELECT receivermailaddress FROM emailconfiguration WHERE controlnode_id = " + controlNode_id;
        return this.executeStatementReturnsString(sql, "receivermailaddress");
    }

    public String getSubject(int controlNode_id) {
        String sql = "SELECT subject FROM emailconfiguration WHERE controlnode_id = " + controlNode_id;
        return this.executeStatementReturnsString(sql, "subject");
    }

    public String getMessage(int controlNode_id) {
        String sql = "SELECT message FROM emailconfiguration WHERE controlnode_id = " + controlNode_id;
        return this.executeStatementReturnsString(sql, "message");
    }

    public String getSendEmailAddress(int controlNode_id) {
        String sql = "SELECT sendmailaddress FROM emailconfiguration WHERE controlnode_id = " + controlNode_id;
        return this.executeStatementReturnsString(sql, "sendmailaddress");
    }
}
