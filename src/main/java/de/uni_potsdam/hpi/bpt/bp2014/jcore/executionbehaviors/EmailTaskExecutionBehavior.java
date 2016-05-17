package de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbEmailConfiguration;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbState;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataObjectInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractControlNodeInstance;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

/**
 * Class defining the execution behavior of an Email Task.
 */
public class EmailTaskExecutionBehavior extends TaskExecutionBehavior {
	private static Logger log = Logger.getLogger(EmailTaskExecutionBehavior.class);
	private final int controlNodeId;
	private final DbEmailConfiguration emailConfiguration = new DbEmailConfiguration();
	private int port;
	private String serverAddress;
	private String receiverMail;
	private String sendMail;
	private String subject;
	private String message;

	/**
	 * Initializes and creates an EmailTaskExecutionBehavior.
	 *
	 * @param activityInstanceId This is an id for an activity instance.
	 * @param scenarioInstance    This is an instance from the class ScenarioInstance.
	 * @param controlNodeInstance This is an AbstractControlNodeInstance.
	 */
	public EmailTaskExecutionBehavior(int activityInstanceId, ScenarioInstance scenarioInstance,
			AbstractControlNodeInstance controlNodeInstance) {
		super(activityInstanceId, scenarioInstance, controlNodeInstance);
		controlNodeId = controlNodeInstance.getControlNodeId();
	}

	@Override public void execute() {
		this.setValues();
		this.sendMail();
		this.setCanTerminate(true);
		this.setCanTerminate(true);
	}

	/**
	 * Sets the attributes for the e mail reading the information from database.
	 */
	private void setValues() {
		port = 587;
		serverAddress = "exchange.framsteg.org";
		receiverMail = emailConfiguration.getReceiverEmailAddress(controlNodeId);
		sendMail = emailConfiguration.getSendEmailAddress(controlNodeId);
		subject = emailConfiguration.getSubject(controlNodeId);
		message = emailConfiguration.getMessage(controlNodeId);
		this.setDataAttributes();
	}

	private void setDataAttributes() {
		for (DataAttributeInstance dataAttributeInstance : getScenarioInstance()
				.getDataAttributeInstances().values()) {
			message = message.replace(
					"#" + (dataAttributeInstance.getDataObjectInstance())
							.getName()
							+ "."
							+ dataAttributeInstance.getName(),
					dataAttributeInstance.getValue().toString());
			subject = subject.replace(
					"#" + (dataAttributeInstance.getDataObjectInstance())
							.getName()
							+ "."
							+ dataAttributeInstance.getName(),
					dataAttributeInstance.getValue().toString());
			receiverMail = receiverMail.replace(
					"#" + (dataAttributeInstance.getDataObjectInstance())
							.getName()
							+ "."
							+ dataAttributeInstance.getName(),
					dataAttributeInstance.getValue().toString());
		}
		DbState dbState = new DbState();
		for (DataObjectInstance dataObjectInstance
				: getScenarioInstance().getDataManager().getDataObjectInstances()) {
			message = message.replace("$" + dataObjectInstance.getName(),
					dbState.getStateName(dataObjectInstance.getStateId()));
		}
	}

	/**
	 * Sends an e mail.
	 */
	private void sendMail() {
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
