package de.hpi.bpt.chimera.model.fragment.bpmn.event.behavior;

import javax.persistence.Entity;

@Entity
public class MessageReceiveDefinition extends SpecialEventDefinition {
	private String eventQuerry;

	public String getEventQuerry() {
		return eventQuerry;
	}

	public void setEventQuerry(String eventQuerry) {
		this.eventQuerry = eventQuerry;
	}
}
