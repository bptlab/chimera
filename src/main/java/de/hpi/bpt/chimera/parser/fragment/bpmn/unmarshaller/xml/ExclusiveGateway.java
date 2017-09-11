package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;

import de.hpi.bpt.chimera.jcomparser.saving.AbstractControlNode;
import de.hpi.bpt.chimera.jcomparser.saving.Connector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to store exclusive Gateway. An exclusive gateway can have an arbitrary number
 * of incoming control flows and one outgoing control flow.
 * Example String:
 * <bpmn:exclusiveGateway id="ExclusiveGateway_0pjea83">
 * <bpmn:incoming>SequenceFlow_0306jnu</bpmn:incoming>
 * <bpmn:outgoing>SequenceFlow_0ct04t8</bpmn:outgoing>
 * <bpmn:outgoing>SequenceFlow_1pj6qq6</bpmn:outgoing>
 * </bpmn:exclusiveGateway>
 */
@XmlRootElement(name = "bpmn:exclusiveGateway")
@XmlAccessorType(XmlAccessType.NONE)
public class ExclusiveGateway extends AbstractControlNode {
	@XmlAttribute(name = "name")
	private String name = "";

	public String getName() {
		return name;
	}

	@Override
	public int save() {
		Connector connector = new Connector();
		this.databaseId = connector.insertControlNode(this.getName(), "XOR", this.getFragmentId(), this.getId());
		return this.databaseId;
	}
}
