package de.hpi.bpt.chimera.model.fragment.bpmn.activity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import de.hpi.bpt.chimera.model.configuration.EmailConfiguration;

@Entity
public class EmailActivity extends AbstractActivity {

	@OneToOne(cascade = CascadeType.ALL)
	private EmailConfiguration emailConfiguration = new EmailConfiguration();

	/**
	 * Email Activities are executed automatically.
	 */
	@Override
	public boolean isAutomatic() {
		return true;
	}

	public EmailConfiguration getEmailConfiguration() {
		return emailConfiguration;
	}

	public void setEmailConfiguration(EmailConfiguration emailConfiguration) {
		this.emailConfiguration = emailConfiguration;
	}
}
