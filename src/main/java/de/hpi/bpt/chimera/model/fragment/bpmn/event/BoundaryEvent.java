package de.hpi.bpt.chimera.model.fragment.bpmn.event;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;

@Entity
public class BoundaryEvent extends AbstractEvent {
	@OneToOne(cascade = CascadeType.ALL)
	private AbstractActivity attachedtoActivity;

	public AbstractActivity getAttachedToActivity() {
		return attachedtoActivity;
	}

	public void setAttachedToActivity(AbstractActivity attachedToActivity) {
		this.attachedtoActivity = attachedToActivity;
	}
}
