package de.hpi.bpt.chimera.model.fragment.BpmnFragment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StartEvent extends AbstractDataControlNode {
	private String eventQuerry = "";

	public String getEventQuerry() {
		return eventQuerry;
	}

	public void setEventQuerry(String eventQuerry) {
		this.eventQuerry = eventQuerry;
	}
}
