package de.hpi.bpt.chimera.model.fragment.bpmn.activity;

import javax.persistence.Entity;

@Entity
public class EmptyActivity extends AbstractActivity {
	/**
	 * Empty Activities should be executed automatically.
	 */
	@Override
	public boolean isAutomatic() {
		return true;
	}
}
