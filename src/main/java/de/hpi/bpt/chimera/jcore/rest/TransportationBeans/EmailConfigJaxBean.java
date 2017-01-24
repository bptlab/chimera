package de.hpi.bpt.chimera.jcore.rest.TransportationBeans;

import de.hpi.bpt.chimera.jconfiguration.rest.RestConfigurator;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is a data class for the email configuration.
 * It is used by Jersey to deserialize JSON.
 * Also it can be used for tests to provide the correct contents.
 * This class in particular is used by the POST for the email configuration.
 * See the {@link RestConfigurator
 * #updateEmailConfiguration(int, de.uni_potsdam.hpi.bpt.bp2014.jconfiguration.rest
 * .RestConfigurator.EmailConfigJaxBean)}
 * updateEmailConfiguration} method for more information.
 */
@XmlRootElement
public class EmailConfigJaxBean {
	/**
	 * The receiver of the email.
	 * coded as an valid email address (as String)
	 */
	private String receiver;

	/**
	 * The subject of the email.
	 * Could be any String but null.
	 */
	private String subject;
	/**
	 * The content of the email.
	 * Could be any String but null.
	 */
	private String content;

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
