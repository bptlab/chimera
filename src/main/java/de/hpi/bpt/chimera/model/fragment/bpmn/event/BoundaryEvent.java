package de.hpi.bpt.chimera.model.fragment.bpmn.event;

import javax.persistence.Entity;

import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;

@Entity
public class BoundaryEvent extends IntermediateCatchEvent {
	private AbstractActivity attachedtoActivity;

	public AbstractActivity getAttachedToActivity() {
		return attachedtoActivity;
	}

	public void setAttachedToActivity(AbstractActivity attachedToActivity) {
		this.attachedtoActivity = attachedToActivity;
	}

	@Override
	public boolean hasEventQuerry() {
		return true;
	}
}
