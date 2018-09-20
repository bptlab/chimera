package de.hpi.bpt.chimera.rest.beans.email;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.model.fragment.bpmn.activity.EmailActivity;

@XmlRootElement
public class EmailActivityJaxBean {
	private String id;
	private String label;
	private EmailConfigJaxBean configuration;

	public EmailActivityJaxBean(EmailActivity emailActivity) {
		setId(emailActivity.getId());
		setLabel(emailActivity.getName());
		setConfiguration(new EmailConfigJaxBean(emailActivity.getEmailConfiguration()));
	}

	public String getId() {
		return id;
	}

	public void setId(String emailActivityId) {
		this.id = emailActivityId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public EmailConfigJaxBean getConfiguration() {
		return configuration;
	}

	public void setConfiguration(EmailConfigJaxBean configuration) {
		this.configuration = configuration;
	}
}
