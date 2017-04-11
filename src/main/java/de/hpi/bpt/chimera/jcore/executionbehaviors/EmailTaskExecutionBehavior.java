package de.hpi.bpt.chimera.jcore.executionbehaviors;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.database.DbEmailConfiguration;
import de.hpi.bpt.chimera.database.data.DbState;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import de.hpi.bpt.chimera.jcore.data.DataAttributeInstance;
import de.hpi.bpt.chimera.jcore.data.DataManager;
import de.hpi.bpt.chimera.jcore.data.DataObject;
import de.hpi.bpt.chimera.util.PropertyLoader;

/**
 * Class defining the execution behavior of an Email Task.
 */
public class EmailTaskExecutionBehavior extends ActivityExecutionBehavior {
	private static Logger log = Logger.getLogger(EmailTaskExecutionBehavior.class);
	private final int controlNodeId;
	private final DbEmailConfiguration emailConfiguration = new DbEmailConfiguration();
	private String hostName;
	private int port;
	private String receiverMail;
	private String sendMail;
	private String password;
	private String subject;
	private String message;

	public EmailTaskExecutionBehavior(ActivityInstance activityInstance) {
		super(activityInstance);
		controlNodeId = activityInstance.getControlNodeId();
	}

	@Override
	public void execute() {
		this.setValues();
		this.sendMail();
	}

	/**
	 * Sets the attributes for the e mail reading the information from
	 * properties and database.
	 */
	private void setValues() {
		hostName = PropertyLoader.getProperty("mailtask.hostname");
		port = Integer.valueOf(PropertyLoader.getProperty("mailtask.port"));
		sendMail = PropertyLoader.getProperty("mailtask.mail.username");
		password = PropertyLoader.getProperty("mailtask.mail.password");

		receiverMail = emailConfiguration.getReceiverEmailAddress(controlNodeId);
		subject = emailConfiguration.getSubject(controlNodeId);
		message = emailConfiguration.getMessage(controlNodeId);
		this.setDataAttributes();
	}

	private void setDataAttributes() {
		DataManager dataManager = getScenarioInstance().getDataManager();
		for (DataAttributeInstance dataAttributeInstance : dataManager.getDataAttributeInstances()) {
			message = message.replace("#" + (dataAttributeInstance.getDataObject()).getName() + "." + dataAttributeInstance.getName(), dataAttributeInstance.getValue().toString());
			subject = subject.replace("#" + (dataAttributeInstance.getDataObject()).getName() + "." + dataAttributeInstance.getName(), dataAttributeInstance.getValue().toString());
			receiverMail = receiverMail.replace("#" + (dataAttributeInstance.getDataObject()).getName() + "." + dataAttributeInstance.getName(), dataAttributeInstance.getValue().toString());
		}
		DbState dbState = new DbState();
		for (DataObject dataObject : dataManager.getDataObjects()) {
			message = message.replace("$" + dataObject.getName(), dbState.getStateName(dataObject.getStateId()));
		}
	}

	/**
	 * Sends an e mail.
	 */
	private void sendMail() {
		Email email = new SimpleEmail();
		email.setHostName(hostName);
		email.setSmtpPort(port);
		email.setAuthenticator(new DefaultAuthenticator(sendMail, password));
		email.setSSLOnConnect(true);
		try {
			email.setFrom(sendMail);
			email.setSubject(subject);
			email.setMsg(message);
			email.addTo(receiverMail);
			email.send();
		} catch (EmailException e) {
			log.error("Error by sending e-Mail - wrong e-Mail properties or wrong config.properties:", e);
		}
	}
}
