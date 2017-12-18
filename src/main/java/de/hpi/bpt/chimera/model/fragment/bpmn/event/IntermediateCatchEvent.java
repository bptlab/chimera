package de.hpi.bpt.chimera.model.fragment.bpmn.event;

import javax.persistence.Entity;

@Entity
public class IntermediateCatchEvent extends AbstractEvent {
	// TODO
	private String eventQuery;

	@Override
	public String getEventQuery() {
		return eventQuery;
	}

	public void setEventQuery(String eventQuery) {
		this.eventQuery = eventQuery;
	}

	@Override
	public boolean hasEventQuerry() {
		return true;
	}
}
