package de.hpi.bpt.chimera.database;

import java.util.List;

/**
 * This class is the representation of a E-mail configuration in the database.
 * It provides get methods to get the sender, receiver, subject and message of an e-mail.
 */
public class DbEmailConfiguration extends DbObject {
	/**
	 * This method returns the e-mail address of the receiver of a mail.
	 *
	 * @param controlNodeId This is the database ID of a controlNode.
	 * @return the e-mail address of the receiver as a String.
	 */
	public String getReceiverEmailAddress(int controlNodeId) {
		String sql = "SELECT receivermailaddress FROM emailconfiguration " + "WHERE controlnode_id = " + controlNodeId;
		return this.executeStatementReturnsString(sql, "receivermailaddress");
	}

	/**
	 * This method returns the subject of an e-mail.
	 *
	 * @param controlNodeId This is the database ID of a controlNode.
	 * @return the subject of the e-mail as a String.
	 */
	public String getSubject(int controlNodeId) {
		String sql = "SELECT subject FROM emailconfiguration " + "WHERE controlnode_id = " + controlNodeId;
		return this.executeStatementReturnsString(sql, "subject");
	}

	/**
	 * This method returns the message of an e-mail.
	 *
	 * @param controlNodeId This is the database ID of a controlNode.
	 * @return the message of the e-mail as a String.
	 */
	public String getMessage(int controlNodeId) {
		String sql = "SELECT message FROM emailconfiguration " + "WHERE controlnode_id = " + controlNodeId;
		return this.executeStatementReturnsString(sql, "message");
	}

	/**
	 * This method returns the e-mail address of the sender of an e-mail.
	 *
	 * @param controlNodeId This is the database ID of a controlNode.
	 * @return the e-mail address of the sender of the e-mail as a String.
	 */
	public String getSendEmailAddress(int controlNodeId) {
		String sql = "SELECT sendmailaddress FROM emailconfiguration " + "WHERE controlnode_id = " + controlNodeId;
		return this.executeStatementReturnsString(sql, "sendmailaddress");
	}

	/**
	 * @param id       This is the database ID of a controlNode.
	 * @param receiver This is the email address of the receiver.
	 * @param subject  This is the subject line of the email.
	 * @param message  This is the message text of the email.
	 * @return the integer status code of the operation
	 */
	public int setEmailConfiguration(int id, String receiver, String subject, String message) {
		if (this.executeExistStatement("SELECT * FROM emailconfiguration WHERE controlnode_id =" + id)) {
			String sql = "UPDATE emailconfiguration SET message = '" + message + "', subject  = '" + subject + "', receivermailaddress = '" + receiver + "'WHERE controlnode_id = " + id;
			return this.executeUpdateStatement(sql);
		} else {
			// TODO save a correct sendmailaddress
			String sql = "INSERT INTO emailconfiguration (message, sendmailaddress, subject, receivermailaddress, controlnode_id) VALUES (" + "'" + message + "', 'TODO', '" + subject + "', '" + receiver + "', " + id + ")";
			return this.executeInsertStatement(sql);
		}
	}

	/**
	 * @param scenarioId This is the database ID of a scenario.
	 * @return a List with email addresses
	 */
	public List<Integer> getAllEmailTasksForScenario(int scenarioId) {
		String sql = "SELECT id FROM `controlnode` WHERE type = 'EmailTask' " + "AND fragment_id IN (Select id FROM fragment " + "WHERE scenario_id = " + scenarioId + ")";
		return this.executeStatementReturnsListInt(sql, "id");
	}
}
