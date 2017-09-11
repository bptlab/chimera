package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;

import de.hpi.bpt.chimera.jcomparser.saving.Connector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 */
// TODO: root name missing?
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class EndEvent extends AbstractDataControlNode {

	@XmlElement(name = "bpmn:messageEventDefinition")
	private MessageDefinition message;

	@Override
	public int save() {
		Connector connector = new Connector();
		this.databaseId = connector.insertControlNode(this.getName(), "EndEvent", this.getFragmentId(), this.getId());

		if (message != null) {
			connector.insertSendEvent(databaseId);
		}

		return this.databaseId;
	}
}
