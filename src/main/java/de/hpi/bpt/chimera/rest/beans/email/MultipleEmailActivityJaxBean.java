package de.hpi.bpt.chimera.rest.beans.email;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.model.fragment.bpmn.activity.EmailActivity;

@XmlRootElement
public class MultipleEmailActivityJaxBean {
	private List<EmailActivityJaxBean> emailTasks;

	public MultipleEmailActivityJaxBean(List<EmailActivity> emailActivities) {
		this.setEmailTasks(emailActivities.stream()
								.map(EmailActivityJaxBean::new)
								.collect(Collectors.toList()));
	}

	public List<EmailActivityJaxBean> getEmailTasks() {
		return emailTasks;
	}

	public void setEmailTasks(List<EmailActivityJaxBean> emailTasks) {
		this.emailTasks = emailTasks;
	}
}
