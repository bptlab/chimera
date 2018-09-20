package de.hpi.bpt.chimera.rest.beans.email;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.model.configuration.EmailConfiguration;

/**
 * This is a data class for the email configuration. It is used by Jersey to
 * deserialize JSON. Also it can be used for tests to provide the correct
 * contents. This class in particular is used by the POST for the email
 * configuration. See the
 * {@link #updateEmailConfiguration(int, EmailConfigJaxBean)}
 * updateEmailConfiguration} method for more information.
 */
@XmlRootElement
public class EmailConfigJaxBean {
	private String receiver;
	private String subject;
	private String message;

	public EmailConfigJaxBean() {
	}

	public EmailConfigJaxBean(EmailConfiguration emailConfig) {
		setReceiver(emailConfig.getReceiverEmailAddress());
		setSubject(emailConfig.getSubject());
		setMessage(emailConfig.getMessage());
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
