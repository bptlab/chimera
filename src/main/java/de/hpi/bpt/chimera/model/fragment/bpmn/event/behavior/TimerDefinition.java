package de.hpi.bpt.chimera.model.fragment.bpmn.event.behavior;

import javax.persistence.Entity;

@Entity
public class TimerDefinition extends SpecialEventDefinition {
	private String timerDuration;

	public String getTimerDuration() {
		return timerDuration;
	}

	public void setTimerDuration(String timerDuration) {
		this.timerDuration = timerDuration;
	}
}
