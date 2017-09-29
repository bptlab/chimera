package de.hpi.bpt.chimera.model.fragment.bpmn.event;

import javax.persistence.Entity;

@Entity
public class StartEvent extends AbstractEvent {
	private String eventQuerry = "";

	@Override
	public boolean hasEventQuerry() {
		return true;
	}

	@Override
	public String getEventQuerry() {
		return eventQuerry;
	}

	public void setEventQuerry(String eventQuerry) {
		this.eventQuerry = eventQuerry;
	}
}
