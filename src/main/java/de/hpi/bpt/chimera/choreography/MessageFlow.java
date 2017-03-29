package de.hpi.bpt.chimera.choreography;

import java.util.UUID;

public class MessageFlow {

	UUID messageFlowId;
	Message message;
	Participant targetParticipant;
	Participant sourceParticipant;

	public void setId(UUID transformId) {
		this.messageFlowId = transformId;

	}

	public UUID getMessageFlowId() {
		return messageFlowId;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Participant getTargetParticipant() {
		return targetParticipant;
	}

	public void setTargetParticipant(Participant targetParticipant) {
		this.targetParticipant = targetParticipant;
	}

	public Participant getSourceParticipant() {
		return sourceParticipant;
	}

	public void setSourceParticipant(Participant sourceParticipant) {
		this.sourceParticipant = sourceParticipant;
	}

}
