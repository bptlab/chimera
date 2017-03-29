/**
 * 
 */
package de.hpi.bpt.chimera.choreography;

import java.util.ArrayList;
import java.util.List;

/**
 * Choreography data structure of Chimera.
 */
public class ChimeraChoreography {
	
	protected List<FlowNode> nodes = new ArrayList<FlowNode>(); 
	
	protected List<SequenceFlow> sequenceFlows = new ArrayList<SequenceFlow>();

	protected List<Participant> participants = new ArrayList<Participant>();;
	
	protected List<Message> messages = new ArrayList<Message>();;
	
	protected List<MessageFlow> messageflows = new ArrayList<MessageFlow>();


	public void addNode(FlowNode node){
		this.nodes.add(node);
	}
	
	public void addMessage(Message message) {
		this.messages.add(message);
	}

	public void addParticipant(Participant participant) {
		this.participants.add(participant);
		
	}

	public void addMessageFlows(MessageFlow messageFlow) {
		this.messageflows.add(messageFlow);
		
	}
	
	public void addSequenceFlows(SequenceFlow sequenceFLow) {
		this.sequenceFlows.add(sequenceFLow);
		
	}

	public List<FlowNode> getNodes() {
		return nodes;
	}

	public List<SequenceFlow> getSequenceFlows() {
		return sequenceFlows;
	}

	public List<Participant> getParticipants() {
		return participants;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public List<MessageFlow> getMessageflows() {
		return messageflows;
	}

//	public List<Event> getEvents() {
//		return events;
//	}

//	public void addStartEvent(StartEvent startEvent) {
//		this.events.add(startEvent);
//	}

//	public void addChoreographyTask(ChoreographyTask choreographyTask) {
//		this.nodes.add(choreographyTask);
//	}

//	public void addEndEvent(EndEvent endEvent) {
//		this.events.add(endEvent);
//		
//	}
	
}
