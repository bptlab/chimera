package de.hpi.bpt.chimera.choreography;

import java.util.UUID;

/**
 * A participant of the choreography.S
 */
public class Participant {

	protected UUID participantId;
	protected String name;

	public void setId(UUID transformId) {
		this.participantId = transformId;

	}

	public void setName(String value) {
		this.name = value;

	}

	public String getName() {
		return name;
	}

}
