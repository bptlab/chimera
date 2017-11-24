package de.hpi.bpt.chimera.jconfiguration.rest.beans;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.model.fragment.bpmn.activity.EmailActivity;

@XmlRootElement
public class EmailActivityJaxBean {
	private String id;
	private String label;

	public EmailActivityJaxBean(EmailActivity emailActivity) {
		setId(emailActivity.getId());
		setLabel(emailActivity.getName());
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
}
