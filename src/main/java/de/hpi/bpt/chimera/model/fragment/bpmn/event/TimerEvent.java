package de.hpi.bpt.chimera.model.fragment.bpmn.event;

import javax.persistence.Entity;

@Entity
public class TimerEvent extends IntermediateCatchEvent {

	private String timerDuration;


	@Override
	public boolean hasEventQuerry() {
		return false;
	}

	public String getTimerDuration() {
		return timerDuration;
	}

	public void setTimerDuration(String timerDuration) {
		this.timerDuration = timerDuration;
	}
}
