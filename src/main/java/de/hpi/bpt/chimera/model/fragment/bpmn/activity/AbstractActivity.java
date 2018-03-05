package de.hpi.bpt.chimera.model.fragment.bpmn.activity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractDataControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.BoundaryEvent;

@Entity
public abstract class AbstractActivity extends AbstractDataControlNode {
	@OneToMany(cascade = CascadeType.ALL)
	private List<BoundaryEvent> attachedBoundaryEvents = new ArrayList<>();

	public List<BoundaryEvent> getAttachedBoundaryEvents() {
		return attachedBoundaryEvents;
	}

	public void setAttachedBoundaryEvents(List<BoundaryEvent> attachedBoundaryEvents) {
		this.attachedBoundaryEvents = attachedBoundaryEvents;
	}

	public void addAttachedBoundaryEvent(BoundaryEvent attachedBoundaryEvent) {
		this.attachedBoundaryEvents.add(attachedBoundaryEvent);
	}

	/**
	 * 
	 * @return true if the activity type executes automatically.
	 */
	public abstract boolean isAutomaticTask();
}
