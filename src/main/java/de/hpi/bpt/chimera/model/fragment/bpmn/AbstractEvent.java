package de.hpi.bpt.chimera.model.fragment.bpmn;

public abstract class AbstractEvent extends AbstractDataControlNode {
	public abstract boolean hasEventQuerry();

	public String getEventQuerry() {
		return null;
	}
}
