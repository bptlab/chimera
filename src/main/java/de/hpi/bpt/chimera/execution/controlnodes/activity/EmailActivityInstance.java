package de.hpi.bpt.chimera.execution.controlnodes.activity;

import javax.persistence.Entity;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.data.DataManager;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.EmailActivity;
import de.hpi.bpt.chimera.util.PropertyLoader;

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
	 * Sets the attributes for the e mail reading the information from the
	 * EmailConfigurations and the Properties.
	 */
	private void setValues() {
		this.hostName = PropertyLoader.getProperty("mailtask.hostname");
		this.port = Integer.valueOf(PropertyLoader.getProperty("mailtask.port"));
		this.sendMail = PropertyLoader.getProperty("mailtask.mail.username");
		this.password = PropertyLoader.getProperty("mailtask.mail.password");

		this.receiverMail = this.getControlNode().getEmailConfiguration().getReceiverEmailAddress();
		this.subject = this.getControlNode().getEmailConfiguration().getSubject();
		this.message = this.getControlNode().getEmailConfiguration().getMessage();
		this.setDataAttributes();
	}


	private void setDataAttributes() {
		this.message = replaceVariableExpressions(this.message);
		this.subject = replaceVariableExpressions(this.subject);
		this.receiverMail = replaceVariableExpressions(this.receiverMail);
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
			log.error("Error by sending e-Mail - wrong e-Mail properties or wrong config.properties or an antivirus software blocking the outgoing mails:", e);
		}
	}
}
