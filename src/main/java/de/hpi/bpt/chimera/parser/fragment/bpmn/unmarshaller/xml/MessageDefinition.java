package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <bpmn:messageEventDefinition messageRef=\"Message_16ekl50\" />\n
 */
@XmlRootElement(name = "bpmn:messageEventDefinition")
@XmlAccessorType(XmlAccessType.NONE)
public class MessageDefinition {
	// TODO: may not be needed
	@XmlElement(name = "bpmn:messageRef")
	private String messageId;

	public String getMessageId() {
		return messageId;
	}
}
