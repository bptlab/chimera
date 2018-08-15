package de.hpi.bpt.chimera.model.fragment.bpmn.event;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractDataControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.behavior.SpecialBehavior;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.behavior.SpecialEventDefinition;

@Entity
public abstract class AbstractEvent extends AbstractDataControlNode {
	@Enumerated(EnumType.STRING)
	private SpecialBehavior specialBehavior;

	@OneToOne(cascade = CascadeType.ALL)
	private SpecialEventDefinition specialEventDefinition;

	public SpecialBehavior getSpecialBehavior() {
		return specialBehavior;
	}

	public void setSpecialBehavior(SpecialBehavior specialBehavior) {
		this.specialBehavior = specialBehavior;
	}

	public SpecialEventDefinition getSpecialEventDefinition() {
		return specialEventDefinition;
	}

	public void setSpecialEventDefinition(SpecialEventDefinition specialEventDefinition) {
		this.specialEventDefinition = specialEventDefinition;
	}

	/**
	 * Events are always executed automatically.
	 */
	@Override
	public boolean isAutomatic() {
		return true;
	}
}
