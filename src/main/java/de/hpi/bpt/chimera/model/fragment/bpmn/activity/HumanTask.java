package de.hpi.bpt.chimera.model.fragment.bpmn.activity;

import javax.persistence.Entity;

@Entity
public class HumanTask extends AbstractActivity {
	/**
	 * Human Tasks are not executed automatically.
	 */
	@Override
	public boolean isAutomaticTask() {
		return false;
	}
}
