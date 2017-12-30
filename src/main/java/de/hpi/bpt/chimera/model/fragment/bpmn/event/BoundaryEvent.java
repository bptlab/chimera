package de.hpi.bpt.chimera.model.fragment.bpmn.event;

import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;

public class BoundaryEvent extends AbstractEvent {
	private String eventQuerry = "";
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

	@Override
	public String getEventQuery() {
		return eventQuerry;
	}

	public void setEventQuerry(String eventQuerry) {
		this.eventQuerry = eventQuerry;
	}
}
