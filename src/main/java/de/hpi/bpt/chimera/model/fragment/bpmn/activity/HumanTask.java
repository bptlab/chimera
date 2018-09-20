package de.hpi.bpt.chimera.model.fragment.bpmn.activity;

import javax.persistence.Entity;

@Entity
public class HumanTask extends AbstractActivity {
	private String role;
	/**
	 * Human Tasks are not executed automatically.
	 */
	@Override
	public boolean isAutomatic() {
		return false;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
