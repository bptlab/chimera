package de.hpi.bpt.chimera.execution.activity;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.State;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.EmailActivity;
import de.hpi.bpt.chimera.util.PropertyLoader;

public class EmailActivityInstance extends AbstractActivityInstance {
	private static final Logger log = Logger.getLogger(EmailActivityInstance.class);

	private String hostName;
	private int port;
	private String receiverMail;
	private String sendMail;
	private String password;
	private String subject;
	private String message;


	public EmailActivityInstance(EmailActivity mailActivity, FragmentInstance fragmentInstance) {
		super(mailActivity, fragmentInstance);
		setState(State.INIT);
		setAutomaticTask(true);
	}

	@Override
	public EmailActivity getControlNode() {
		return (EmailActivity) super.getControlNode();
	}

	@Override
	public void execute() {
		this.setValues();
		this.sendMail();
	}

	/**
	 * Sets the attributes for the e mail reading the information from the
	 * EmailConfigurations and the Properties.
	 */
	private void setValues() {
		hostName = PropertyLoader.getProperty("mailtask.hostname");
		port = Integer.valueOf(PropertyLoader.getProperty("mailtask.port"));
		sendMail = PropertyLoader.getProperty("mailtask.mail.username");
		password = PropertyLoader.getProperty("mailtask.mail.password");

		// TODO
		receiverMail = this.getControlNode().getEmailConfiguration().getReceiverEmailAddress();
		subject = this.getControlNode().getEmailConfiguration().getSubject();
		message = this.getControlNode().getEmailConfiguration().getMessage();
		this.setDataAttributes();
	}


	private void setDataAttributes() {
		// TODO
		// DataManager dataManager = getScenarioInstance().getDataManager();
		// for (DataAttributeInstance dataAttributeInstance :
		// dataManager.getDataAttributeInstances()) {
		// message = message.replace("#" +
		// (dataAttributeInstance.getDataObject()).getName() + "." +
		// dataAttributeInstance.getName(),
		// dataAttributeInstance.getValue().toString());
		// subject = subject.replace("#" +
		// (dataAttributeInstance.getDataObject()).getName() + "." +
		// dataAttributeInstance.getName(),
		// dataAttributeInstance.getValue().toString());
		// receiverMail = receiverMail.replace("#" +
		// (dataAttributeInstance.getDataObject()).getName() + "." +
		// dataAttributeInstance.getName(),
		// dataAttributeInstance.getValue().toString());
		// }
		// DbState dbState = new DbState();
		// for (DataObject dataObject : dataManager.getDataObjects()) {
		// message = message.replace("$" + dataObject.getName(),
		// dbState.getStateName(dataObject.getStateId()));
		// }
	}

	/**
	 * Sends an e mail.
	 */
	private void sendMail() {
		// Attention, when Chimera throws an error while sending an E-mail,
		// sometimes the reason is an antivirus software. Try turning it off and
		// running some EmailActivities to figure out whether your antivirus
		// software is responsible for the errors.
		log.info("sending a Mail");
		// TODO
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
			log.error("Error by sending e-Mail - wrong e-Mail properties or wrong config.properties or an antivirus software blocking the outgoing mails:", e);
		}
	}
}
