package de.hpi.bpt.chimera.model.fragment.bpmn.event;

import javax.persistence.Entity;

import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractDataControlNode;

@Entity
public abstract class AbstractEvent extends AbstractDataControlNode {
	public abstract boolean hasEventQuerry();

	public String getEventQuery() {
		return null;
	}
}
