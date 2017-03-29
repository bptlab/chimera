package de.hpi.bpt.chimera.choreography;

import java.util.UUID;

public class Message {
	
	protected UUID messageId;
	protected String name;
	// TODO: probably this is better implemented as object
	protected String restAnnotation;
	
	public void setId(UUID uuid) {
		this.messageId = uuid;
	}
	
	public UUID getMessageId() {
		return messageId;
	}
}
