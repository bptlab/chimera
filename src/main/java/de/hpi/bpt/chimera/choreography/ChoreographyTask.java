package de.hpi.bpt.chimera.choreography;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a choreography task.
 *
 */
public class ChoreographyTask extends FlowNode {

	protected Participant initiatingParticipant;
	protected List<Participant> participants = new ArrayList<Participant>();
	protected Message initatingMessage;
	/**
	 * Optional, might be null
	 */
	protected Message responseMessage;

	public ChoreographyTask() {

	}

	public void setInitiatingParticipant(Participant participant) {
		this.initiatingParticipant = participant;
	}

}
