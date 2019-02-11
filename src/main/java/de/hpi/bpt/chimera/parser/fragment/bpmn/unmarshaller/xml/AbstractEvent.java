package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
@XmlAccessorType(XmlAccessType.NONE)
public class AbstractEvent extends AbstractDataControlNode {
	@XmlAttribute(name = "griffin:eventquery")
	private String eventQuery;

	@XmlElement(name = "bpmn:timerEventDefinition")
	private TimerDefinition timerDefinition;

	@XmlElement(name = "bpmn:messageEventDefinition")
	private MessageDefinition messageDefinition;

	@XmlElement(name = "bpmn:signalEventDefinition")
	private SignalDefinition signalDefinition;

	public String getEventQuery() {
		return this.eventQuery;
	}

	public TimerDefinition getTimerDefinition() {
		return timerDefinition;
	}

	public MessageDefinition getMessageDefinition() {
		return messageDefinition;
	}

	public SignalDefinition getSignalDefinition() { return signalDefinition; }

	public boolean hasTimerDefinition() {
		return timerDefinition != null;
	}

	public boolean hasMessageDefiniton() {
		return messageDefinition != null;
	}

	public boolean hasSignalDefinition() { return signalDefinition != null; }
}
