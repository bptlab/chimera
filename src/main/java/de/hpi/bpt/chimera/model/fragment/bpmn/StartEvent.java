package de.hpi.bpt.chimera.model.fragment.bpmn;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
