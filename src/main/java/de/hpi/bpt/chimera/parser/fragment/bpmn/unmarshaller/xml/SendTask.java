package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;

import de.hpi.bpt.chimera.jcomparser.saving.Connector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bpmn:sendTask")
@XmlAccessorType(XmlAccessType.NONE)
public class SendTask extends AbstractDataControlNode {

	@Override
	public int save() {
		Connector connector = new Connector();
		// given SendTasks are saved as EmailTasks which then can be configured
		// using the Admin Mailconfiguration tool of the Chimera frontend
		this.databaseId = connector.insertControlNode(this.getName(), "EmailTask", this.getFragmentId(), this.getId());

		return this.databaseId;
	}
}
