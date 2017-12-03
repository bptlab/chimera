package de.hpi.bpt.chimera.model.fragment.bpmn.activity;

import javax.persistence.Entity;

import de.hpi.bpt.chimera.model.configuration.EmailConfiguration;

@Entity
public class EmailActivity extends AbstractActivity {

	private EmailConfiguration emailConfiguration = new EmailConfiguration();

	public EmailConfiguration getEmailConfiguration() {
		return emailConfiguration;
	}

	public void setEmailConfiguration(EmailConfiguration emailConfiguration) {
		this.emailConfiguration = emailConfiguration;
	}
}
