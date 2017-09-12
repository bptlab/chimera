package de.hpi.bpt.chimera.model.fragment.bpmn;

import javax.persistence.Entity;

@Entity
public abstract class AbstractEvent extends AbstractDataControlNode {
	public abstract boolean hasEventQuerry();

	public String getEventQuerry() {
		return null;
	}
}
