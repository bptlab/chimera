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
public class BoundaryEvent extends AbstractDataControlNode {
	/**
	 * Stores the Id of the activity (or subprocess) the Event is attached to.
	 */
	@XmlAttribute(name = "attachedToRef")
	private String attachedToRef;

	@XmlAttribute(name = "griffin:eventquery")
	private String eventQuery;

	public String getAttachedToRef() {
		return attachedToRef;
	}

	public void setAttachedToRef(String attachedToRef) {
		this.attachedToRef = attachedToRef;
	}

	public String getEventQuery() {
		return this.eventQuery;
	}

	public void setEventQuery(String eventQuery) {
		this.eventQuery = eventQuery;
	}
}
