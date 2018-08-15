package de.hpi.bpt.chimera.execution.controlnodes.activity;

import javax.persistence.Entity;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.model.configuration.EmailConfiguration;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.EmailActivity;
import de.hpi.bpt.chimera.util.PropertyLoader;

/**
 * I represent an instance of an {@link EmailActivity}. When I am executed, I read the config file
 * and the {@link EmailConfiguration} of my control node to set the values required for sending
 * emails. 
 * @author Marcin.Hewelt
 */
@Entity
public class EmailActivityInstance extends AbstractActivityInstance {
	private static final Logger log = Logger.getLogger(EmailActivityInstance.class);

	private String hostName;
	private int port;
	private String receiverMail;
	private String sendMail;
	private String password;
	private String subject;
	private String message;

	/**
	 * for JPA only
	 */
	public EmailActivityInstance() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}

	public EmailActivityInstance(EmailActivity mailActivity, FragmentInstance fragmentInstance) {
		super(mailActivity, fragmentInstance);
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
	 * Sets the attributes for the e mail reading the information from the EmailConfigurations and the Properties.
	 */
	private void setValues() {
		// read the properties
		this.hostName = PropertyLoader.getProperty("mailtask.hostname");
		this.port = PropertyLoader.getIntProperty("mailtask.port");
		this.sendMail = PropertyLoader.getProperty("mailtask.mail.username");
		this.password = PropertyLoader.getProperty("mailtask.mail.password");
		if (hostName.isEmpty() || sendMail.isEmpty() || password.isEmpty()) { // not checking 'port' as 0 is valid value
			log.warn("Email tasks are not configured correctly. You need to set 'mailtask.hostname', 'mailtask.port', "
					+ "'mailtask.mail.username', and 'mailtask.mail.password' in 'config.properties'");
		}

		// get the email configuration 
		EmailConfiguration emailConfiguration = getControlNode().getEmailConfiguration();
		this.receiverMail = emailConfiguration.getReceiverEmailAddress();
		this.subject = emailConfiguration.getSubject();
		this.message = emailConfiguration.getMessage();
		if (receiverMail.isEmpty() || subject.isEmpty() || message.isEmpty()) {
			log.warn("Email task was not configured. You need to set recipient, subject, and message text in the "
					+ "Chimera UI (Admin -> Mail Configuration -> Select Case Model)");
		}
		
		// replace variables
		this.message = replaceVariableExpressions(this.message);
		this.subject = replaceVariableExpressions(this.subject);
		this.receiverMail = replaceVariableExpressions(this.receiverMail);
	}


	/**
	 * Now send the email using the values we set previously.
	 */
	private void sendMail() {
		// Attention, when Chimera throws an error while sending an E-mail,
		// sometimes the reason is an antivirus software. Try turning it off and
		// running some EmailActivities to figure out whether your antivirus
		// software is responsible for the errors.
		log.info("Sending email");
		Email email = new SimpleEmail();
		email.setHostName(this.hostName);
		email.setSmtpPort(this.port);
		email.setAuthenticator(new DefaultAuthenticator(this.sendMail, this.password));
		email.setSSLOnConnect(true);
		try {
			email.setFrom(this.sendMail);
			email.setSubject(this.subject);
			email.setMsg(this.message);
			email.addTo(this.receiverMail);
			email.send();
		} catch (EmailException e) {
			log.error("Error sending email. Check whether email task has been configured in the Chimera UI and "
					+ "neccessary properties in 'config.properties' are set. Another reason might be antivirus "
					+ "software blocking outgoing mails.", e);
		}
	}
}
