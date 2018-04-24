package de.hpi.bpt.chimera.model.configuration;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 
 * a class for storing the EmailConfigurations like receive address, subject,
 * message... These values aren't set from Griffin, so the parser will call the
 * constructor, setting all values to an empty string. Later appropriate values
 * can be set from within Chimera.
 */
@Entity
public class EmailConfiguration {

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private int dbId;

	private String receiverEmailAddress = "";

	private String subject = "";

	private String message = "";

	private String sendEmailAddress = "";

	public String getReceiverEmailAddress() {
		return receiverEmailAddress;
	}

	public void setReceiverEmailAddress(String receiverEmailAddress) {
		this.receiverEmailAddress = receiverEmailAddress;
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

	public String getSendEmailAddress() {
		return sendEmailAddress;
	}

	public void setSendEmailAddress(String sendEmailAddress) {
		this.sendEmailAddress = sendEmailAddress;
	}


}
