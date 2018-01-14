package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * .
 */
@XmlRootElement(name = "bpmn:boundaryEvent")
@XmlAccessorType(XmlAccessType.NONE)
public class BoundaryEvent extends AbstractEvent {
	/**
	 * Stores the Id of the activity (or subprocess) the Event is attached to.
	 */
	@XmlAttribute(name = "attachedToRef")
	private String attachedToRef;

	public boolean hasAttachedToRef() {
		return attachedToRef != null;
	}

	public String getAttachedToRef() {
		return attachedToRef;
	}
}
