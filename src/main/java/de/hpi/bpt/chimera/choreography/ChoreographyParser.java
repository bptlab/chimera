package de.hpi.bpt.chimera.choreography;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class ChoreographyParser {

	static final String MESSAGE = "message";
	static final String MESSAGEFLOW = "messageFlow";
	static final String MESSAGEREF = "messageRef";
	static final String MESSAGEFLOWREF = "messageFlowRef";
	static final String ID = "id";

	static final String CHOREOGRAPHYTASK = "choreographyTask";
	static final String INCOMING = "incoming";
	static final String OUTGOING = "outgoing";
	static final String SEQUENCEFLOW = "sequenceFlow";
	static final String TARGETREF = "targetRef";
	static final String SOURCEREF = "sourceRef";

	static final String INITIATINGPARTICIPANTREF = "initiatingParticipantRef";
	static final String PARTICIPANT = "participant";
	static final String PARTICIPANTREF = "participantRef";

	static final String STARTEVENT = "startEvent";
	static final String ENDEVENT = "endEvent";
	static final String INTERMEDIATECATCHEVENT = "intermediateCatchEvent";

	static final String EXCLUSIVEGATEWAY = "exclusiveGateway";
	static final String PARALLELGATEWAY = "parallelGateway";
	static final String NAME = "name";
	private FlowNode current;
	private UUID currentId;
	private Map<UUID, Participant> idParticipantMapping = new HashMap<UUID, Participant>();
	private Map<UUID, Message> idMessageMapping = new HashMap<UUID, Message>();
	private Map<UUID, MessageFlow> idMessageFlowMapping = new HashMap<UUID, MessageFlow>();
	private Map<UUID, UUID> messageFlowIdMessageRefId = new HashMap<UUID, UUID>();
	private Map<UUID, UUID> messageFlowIdSourceRef = new HashMap<UUID, UUID>();
	private Map<UUID, UUID> messageFlowIdTargetRef = new HashMap<UUID, UUID>();
	private Map<UUID, UUID> sequenceFlowIdSourceRef = new HashMap<UUID, UUID>();
	private Map<UUID, UUID> sequenceFlowIdTargetRef = new HashMap<UUID, UUID>();
	private Map<UUID, FlowNode> idNodeMapping = new HashMap<UUID, FlowNode>();
	private Map<UUID, SequenceFlow> idFlowMapping = new HashMap<UUID, SequenceFlow>();
	private Map<UUID, List<UUID>> nodeIncomingMapping = new HashMap<UUID, List<UUID>>();
	private Map<UUID, List<UUID>> nodeOutgoingMapping = new HashMap<UUID, List<UUID>>();
	private Map<UUID, List<UUID>> taskParticpantRefMapping = new HashMap<UUID, List<UUID>>();
	private Map<UUID, UUID> taskInitiatorRef = new HashMap<UUID, UUID>();
	private Map<UUID, List<UUID>> taskMessageFlowRefMapping = new HashMap<UUID, List<UUID>>();

	// private Map<String, ArrayList<Activity>> diagrams = new HashMap<String,
	// ArrayList<Activity>>();

	@SuppressWarnings({ "unchecked", "null" })
	public ChimeraChoreography parse(InputStream choreoStream) {
		ChimeraChoreography chimeraChoreography = new ChimeraChoreography();
		try {
			// First, create a new XMLInputFactory
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			// Setup a new eventReader
			// InputStream in = new FileInputStream(choreographyFileName);
			XMLEventReader eventReader = inputFactory
					.createXMLEventReader(choreoStream);
			// read the XML document

			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();

				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();

					// MESSAGE
					if (startElement.getName().getLocalPart().equals(MESSAGE)) {
						Message message = new Message();
						Iterator<Attribute> attributes = startElement
								.getAttributes();
						while (attributes.hasNext()) {
							Attribute attribute = attributes.next();
							if (attribute.getName().toString().equals(ID)) {
								UUID id = transformId(attribute.getValue());
								message.setId(id);
								idMessageMapping.put(id, message);
							}
						}
						chimeraChoreography.addMessage(message);
					}

					// PARTICIPANT start tag
					if (startElement.getName().getLocalPart()
							.equals(PARTICIPANT)) {
						Participant participant = new Participant();
						Iterator<Attribute> attributes = startElement
								.getAttributes();
						while (attributes.hasNext()) {
							Attribute attribute = attributes.next();
							if (attribute.getName().toString().equals(ID)) {
								UUID id = transformId(attribute.getValue());
								participant.setId(id);
								idParticipantMapping.put(id, participant);
							}
							if (attribute.getName().toString().equals(NAME)) {
								participant.setName(attribute.getValue());
							}
						}
						chimeraChoreography.addParticipant(participant);
					}

					// MESSAGEFLOW start tag
					if (startElement.getName().getLocalPart()
							.equals(MESSAGEFLOW)) {
						MessageFlow messageFlow = new MessageFlow();
						UUID myId = UUID.randomUUID();
						
						Iterator<Attribute> attributeId = startElement
								.getAttributes();
						while (attributeId.hasNext()) {
							Attribute attribute = attributeId.next();
							if (attribute.getName().toString().equals(ID)) {
								myId = transformId(attribute.getValue());
								messageFlow.setId(myId);
								idMessageFlowMapping.put(myId, messageFlow);
							}
							
						}
						
						Iterator<Attribute> attributes = startElement
								.getAttributes();
						while (attributes.hasNext()) {
							Attribute attribute = attributes.next();
							
							if (attribute.getName().toString()
									.equals(MESSAGEREF)) {
								UUID messageRefId = transformId(attribute
										.getValue());
								messageFlowIdMessageRefId.put(myId,
										messageRefId);
							}
							if (attribute.getName().toString()
									.equals(SOURCEREF)) {
								messageFlowIdSourceRef.put(myId,
										transformId(attribute.getValue()));
							}
							if (attribute.getName().toString()
									.equals(TARGETREF)) {
								messageFlowIdTargetRef.put(myId,
										transformId(attribute.getValue()));
							}
						}
						chimeraChoreography.addMessageFlows(messageFlow);
					}

					// SEQUENCEFLOW start tag
					if (startElement.getName().getLocalPart()
							.equals(SEQUENCEFLOW)) {
						SequenceFlow sequenceFlow = new SequenceFlow();
						
						UUID myId = UUID.randomUUID();
						Iterator<Attribute> attributeId = startElement
								.getAttributes();
						while (attributeId.hasNext()) {
							Attribute attribute = attributeId.next();
							if (attribute.getName().toString().equals(ID)) {
								myId = transformId(attribute.getValue());
								sequenceFlow.setId(myId);
								idFlowMapping.put(myId, sequenceFlow);
							}
							
						}
						Iterator<Attribute> attributes = startElement
								.getAttributes();
						// FIXME: We assume that the Id is always the first
						// attribute!!!
						while (attributes.hasNext()) {
							Attribute attribute = attributes.next();
							
							if (attribute.getName().toString().equals(NAME)) {
								sequenceFlow.setName(attribute.getValue());
							}
							if (attribute.getName().toString()
									.equals(SOURCEREF)) {
								sequenceFlowIdSourceRef.put(myId,
										transformId(attribute.getValue()));
							}
							if (attribute.getName().toString()
									.equals(TARGETREF)) {
								sequenceFlowIdTargetRef.put(myId,
										transformId(attribute.getValue()));
							}
						}
						
						chimeraChoreography.addSequenceFlows(sequenceFlow);
					}

					// STARTEVENT start tag
					if (startElement.getName().getLocalPart()
							.equals(STARTEVENT)) {
						StartEvent startEvent = new StartEvent();
						Iterator<Attribute> attributes = startElement
								.getAttributes();
						while (attributes.hasNext()) {
							Attribute attribute = attributes.next();
							if (attribute.getName().toString().equals(ID)) {
								UUID id = transformId(attribute.getValue());
								startEvent.setId(id);
								idNodeMapping.put(id, startEvent);
								currentId = id;
							}
							if (attribute.getName().toString().equals(NAME)) {
								startEvent.setName(attribute.getValue());
							}
						}
						chimeraChoreography.addNode(startEvent);
					}
					// INTERMEDIATECATCH start tag
					if (startElement.getName().getLocalPart()
							.equals(INTERMEDIATECATCHEVENT)) {
						IntermediateEvent intermediateEvent = new IntermediateEvent();
						Iterator<Attribute> attributes = startElement
								.getAttributes();
						while (attributes.hasNext()) {
							Attribute attribute = attributes.next();
							if (attribute.getName().toString().equals(ID)) {
								UUID id = transformId(attribute.getValue());
								intermediateEvent.setId(id);
								idNodeMapping.put(id, intermediateEvent);
								currentId = id;
							}
							if (attribute.getName().toString().equals(NAME)) {
								intermediateEvent.setName(attribute.getValue());
							}
						}
						chimeraChoreography.addNode(intermediateEvent);
					}
					// END EVENT start tag
					if (startElement.getName().getLocalPart().equals(ENDEVENT)) {
						EndEvent endEvent = new EndEvent();
						Iterator<Attribute> attributes = startElement
								.getAttributes();
						while (attributes.hasNext()) {
							Attribute attribute = attributes.next();
							if (attribute.getName().toString().equals(ID)) {
								UUID id = transformId(attribute.getValue());
								endEvent.setId(id);
								idNodeMapping.put(id, endEvent);
								currentId = id;
							}
							if (attribute.getName().toString().equals(NAME)) {
								endEvent.setName(attribute.getValue());
							}
						}
						chimeraChoreography.addNode(endEvent);
					}

					// CHOREOGRAPHY TASK start tag
					if (startElement.getName().getLocalPart()
							.equals(CHOREOGRAPHYTASK)) {
						ChoreographyTask choreographyTask = new ChoreographyTask();
						UUID taskId = UUID.randomUUID();
					
						Iterator<Attribute> attributId = startElement
								.getAttributes();
						while (attributId.hasNext()) {
							Attribute attribute = attributId.next();
							if (attribute.getName().toString().equals(ID)) {
								taskId = transformId(attribute.getValue());
								choreographyTask.setId(taskId);
								idNodeMapping.put(taskId, choreographyTask);
							}
							
						}
						Iterator<Attribute> attributes = startElement
								.getAttributes();
						
						while (attributes.hasNext()) {
							Attribute attribute = attributes.next();
							
							if (attribute.getName().toString().equals(NAME)) {
								choreographyTask.setName(attribute.getValue());
							}
							if (attribute.getName().toString()
									.equals(INITIATINGPARTICIPANTREF)) {
								taskInitiatorRef.put(taskId,
										transformId(attribute.getValue()));
							}
						}
						// nodeIncomingMapping.put(taskId, new
						// ArrayList<UUID>());
						currentId = taskId;
						chimeraChoreography.addNode(choreographyTask);
					}
					// GATEWAYS
					if (startElement.getName().getLocalPart().equals(EXCLUSIVEGATEWAY)) {
						ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
						Iterator<Attribute> attributes = startElement
								.getAttributes();
						while (attributes.hasNext()) {
							Attribute attribute = attributes.next();
							if (attribute.getName().toString().equals(ID)) {
								UUID id = transformId(attribute.getValue());
								exclusiveGateway.setId(id);
								idNodeMapping.put(id, exclusiveGateway);
								currentId = id;
							}
							if (attribute.getName().toString().equals(NAME)) {
								exclusiveGateway.setName(attribute.getValue());
							}
						}
						chimeraChoreography.addNode(exclusiveGateway);
					}
					
					// INCOMING flows for flow nodes
					if (startElement.getName().getLocalPart().equals(INCOMING)) {
						if (currentId != null) {
							XMLEvent nextEvent = eventReader.nextEvent();
							UUID sequenceFlowId = transformId(nextEvent
									.asCharacters().getData());
							if (!nodeIncomingMapping.containsKey(currentId)) {
								List<UUID> emptyList = new ArrayList<UUID>();
								nodeIncomingMapping.put(currentId, emptyList);
							}
							List<UUID> list = nodeIncomingMapping
									.get(currentId);
							list.add(sequenceFlowId);
							nodeIncomingMapping.put(currentId, list);
						}
					}
					// OUTGOING flows for flow nodes
					if (startElement.getName().getLocalPart().equals(OUTGOING)) {
						if (currentId != null) {
							XMLEvent nextEvent = eventReader.nextEvent();
							UUID seqenceFlowId = transformId(nextEvent
									.asCharacters().getData());
							if (!nodeOutgoingMapping.containsKey(currentId)) {
								List<UUID> emptyList = new ArrayList<UUID>();
								nodeOutgoingMapping.put(currentId, emptyList);
							}
							List<UUID> list = nodeOutgoingMapping
									.get(currentId);
							list.add(seqenceFlowId);
							nodeOutgoingMapping.put(currentId, list);
						}
					}
					if (startElement.getName().getLocalPart()
							.equals(PARTICIPANTREF)) {
						if (currentId != null) {
							XMLEvent nextEvent = eventReader.nextEvent();
							UUID seqenceFlowId = transformId(nextEvent
									.asCharacters().getData());
							if (!taskParticpantRefMapping
									.containsKey(currentId)) {
								List<UUID> emptyList = new ArrayList<UUID>();
								taskParticpantRefMapping.put(currentId,
										emptyList);
							}
							List<UUID> list = taskParticpantRefMapping
									.get(currentId);
							list.add(seqenceFlowId);
							taskParticpantRefMapping.put(currentId, list);
						}
					}
					if (startElement.getName().getLocalPart()
							.equals(MESSAGEFLOWREF)) {
						if (currentId != null) {
							XMLEvent nextEvent = eventReader.nextEvent();
							UUID seqenceFlowId = transformId(nextEvent
									.asCharacters().getData());
							if (!taskMessageFlowRefMapping
									.containsKey(currentId)) {
								List<UUID> emptyList = new ArrayList<UUID>();
								taskMessageFlowRefMapping.put(currentId,
										emptyList);
							}
							List<UUID> list = taskMessageFlowRefMapping
									.get(currentId);
							list.add(seqenceFlowId);
							taskMessageFlowRefMapping.put(currentId, list);
						}
					}
				}
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		
		//Message flow to Message 
		for (MessageFlow messageFlow : chimeraChoreography.getMessageflows()){
			UUID messageFlowId = messageFlow.getMessageFlowId();
			// setting the message
			UUID msgRef = messageFlowIdMessageRefId.get(messageFlowId);
			Message msg = idMessageMapping.get(msgRef);
			messageFlow.setMessage(msg);
			// setting participants
			UUID sourceId = messageFlowIdSourceRef.get(messageFlowId);
			Participant sourceParticipant = idParticipantMapping.get(sourceId);
			messageFlow.setSourceParticipant(sourceParticipant);
			UUID targetId = messageFlowIdTargetRef.get(messageFlowId);
			Participant targetParticipant = idParticipantMapping.get(targetId);
			messageFlow.setTargetParticipant(targetParticipant);
		}
		for (SequenceFlow sequenceFlow : chimeraChoreography.getSequenceFlows()) {
			UUID sequenceFlowId = sequenceFlow.getSequenceFlowId();
			UUID sourceId = sequenceFlowIdSourceRef.get(sequenceFlowId);
			FlowNode sourceNode = idNodeMapping.get(sourceId);
			sequenceFlow.setSource(sourceNode);
	
			sourceNode.outgoingFlows.add(sequenceFlow);
			UUID targetId = sequenceFlowIdTargetRef.get(sequenceFlowId);
			FlowNode targetNode = idNodeMapping.get(targetId);
			sequenceFlow.setTarget(targetNode);
			targetNode.incomingFlows.add(sequenceFlow);
		}
		for (FlowNode node : chimeraChoreography.getNodes()) {
			
			if (node instanceof ChoreographyTask) {
				ChoreographyTask task = (ChoreographyTask) node;
				UUID nodeId = task.getId();
				// set participants
				for (UUID participantId : taskParticpantRefMapping.get(nodeId)) {
					Participant participant = idParticipantMapping.get(participantId);
					task.participants.add(participant);
				}
				// set initiating particpant
				UUID initiatorId = taskInitiatorRef.get(nodeId);
				Participant initiator = idParticipantMapping.get(initiatorId);
				task.setInitiatingParticipant(initiator);
				// set initiating and response message
				for (UUID messageFlowId : taskMessageFlowRefMapping.get(nodeId)) {
					MessageFlow msgFlow = idMessageFlowMapping.get(messageFlowId);
					if (msgFlow.getSourceParticipant().equals(initiator)) {
						task.initatingMessage = msgFlow.getMessage();
					} else {
						task.responseMessage = msgFlow.getMessage();
					}
				}
			}
		}
		return chimeraChoreography;
	}

	/**
	 * Transforms the Signavio Id found in the BPMN XML into a UUID by dropping
	 * the first 4 characters ("sid-").
	 * 
	 * @param signavioId
	 * @return
	 */
	private UUID transformId(String signavioId) {
		return UUID.fromString(signavioId.substring(4));
	}
}
