package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 */
@XmlRootElement(name = "bpmn:startEvent")
@XmlAccessorType(XmlAccessType.NONE)
public class StartEvent extends AbstractDataControlNode {

	@XmlAttribute(name = "griffin:eventquery")
	private String eventQuery = "";

	public String getEventQuery() {
		return this.eventQuery;
	}

	public void setEventQuery(String eventQuery) {
		this.eventQuery = eventQuery;
	}

}
