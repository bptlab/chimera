package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.LinkedList;

/**
 * This class is the representation of a E-mail configuration in the database.
 * It provides get methods to get the sender, receiver, subject and message of an e-mail.
 */
public class DbEmailConfiguration extends DbObject {
    /**
     * This method returns the e-mail address of the receiver of a mail.
     *
     * @param controlNode_id This is the database ID of a controlNode.
     * @return the e-mail address of the receiver as a String.
     */
    public String getReceiverEmailAddress(int controlNode_id) {
        String sql = "SELECT receivermailaddress FROM emailconfiguration WHERE controlnode_id = " + controlNode_id;
        return this.executeStatementReturnsString(sql, "receivermailaddress");
    }

    /**
     * This method returns the subject of an e-mail.
     *
     * @param controlNode_id This is the database ID of a controlNode.
     * @return the subject of the e-mail as a String.
     */
    public String getSubject(int controlNode_id) {
        String sql = "SELECT subject FROM emailconfiguration WHERE controlnode_id = " + controlNode_id;
        return this.executeStatementReturnsString(sql, "subject");
    }

    /**
     * This method returns the message of an e-mail.
     *
     * @param controlNode_id This is the database ID of a controlNode.
     * @return the message of the e-mail as a String.
     */
    public String getMessage(int controlNode_id) {
        String sql = "SELECT message FROM emailconfiguration WHERE controlnode_id = " + controlNode_id;
        return this.executeStatementReturnsString(sql, "message");
    }

    /**
     * This method returns the e-mail address of the sender of an e-mail.
     *
     * @param controlNode_id This is the database ID of a controlNode.
     * @return the e-mail address of the sender of the e-mail as a String.
     */
    public String getSendEmailAddress(int controlNode_id) {
        String sql = "SELECT sendmailaddress FROM emailconfiguration WHERE controlnode_id = " + controlNode_id;
        return this.executeStatementReturnsString(sql, "sendmailaddress");
    }

    public int setEmailConfiguration(int id, String receiver, String subject, String message) {
        String sql = "UPDATE emailconfiguration SET message = '" +
                message + "', subject  = '" + subject +
                "', receivermailaddress = '" + receiver +
                "'WHERE controlnode_id = " + id;
        return this.executeUpdateStatement(sql);
    }

    public LinkedList<Integer> getAllEmailTasksForScenario(int scenario_id) {
        String sql = "SELECT id FROM `controlnode` WHERE type = 'EmailTask' AND fragment_id IN (Select id FROM fragment WHERE scenario_id = " + scenario_id + ")";
        return this.executeStatementReturnsListInt(sql, "id");
    }
}
