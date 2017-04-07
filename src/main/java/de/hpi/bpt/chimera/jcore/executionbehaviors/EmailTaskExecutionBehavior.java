package de.hpi.bpt.chimera.jcore.executionbehaviors;

import de.hpi.bpt.chimera.database.DbEmailConfiguration;
import de.hpi.bpt.chimera.database.data.DbState;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import de.hpi.bpt.chimera.jcore.data.DataAttributeInstance;
import de.hpi.bpt.chimera.jcore.data.DataManager;
import de.hpi.bpt.chimera.jcore.data.DataObject;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

/**
 * Class defining the execution behavior of an Email Task.
 */
public class EmailTaskExecutionBehavior extends ActivityExecutionBehavior {
	private static Logger log = Logger.getLogger(EmailTaskExecutionBehavior.class);
	private final int controlNodeId;
	private final DbEmailConfiguration emailConfiguration = new DbEmailConfiguration();
	private final int port = 587;
	private final String serverAddress = "exchange.framsteg.org";
	private String receiverMail;
	private String sendMail;
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
	 * Sets the attributes for the e mail reading the information from database.
	 */
	private void setValues() {
		receiverMail = emailConfiguration.getReceiverEmailAddress(controlNodeId);
		sendMail = emailConfiguration.getSendEmailAddress(controlNodeId);
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
		log.info("trying to send a mail");
		Email email = new SimpleEmail();
		email.setHostName(serverAddress);
		email.setSmtpPort(port);
		email.setAuthentication("bp2014w01@framsteg.org", "UjB9T8kAS8H9g4YC1f8U");
		try {
			email.setFrom(sendMail);
			email.setSubject(subject);
			email.setMsg(message);
			email.addTo(receiverMail);
			email.send();
		} catch (EmailException e) {
			log.error("Error by sending e-Mail:", e);
		}
	}
}
