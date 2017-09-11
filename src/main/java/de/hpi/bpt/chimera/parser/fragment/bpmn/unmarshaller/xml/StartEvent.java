package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;

import de.hpi.bpt.chimera.jcomparser.saving.Connector;

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

	@Override
	public int save() {
		Connector connector = new Connector();
		this.databaseId = connector.insertControlNode(this.getName(), "StartEvent", this.getFragmentId(), this.getId());

		connector.insertEvent("StartEvent", this.eventQuery, this.fragmentId, this.getId(), this.databaseId);

		return this.databaseId;
	}

	public String getEventQuery() {
		return this.eventQuery;
	}

	public void setEventQuery(String eventQuery) {
		this.eventQuery = eventQuery;
	}

}
