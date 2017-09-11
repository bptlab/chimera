package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;

import de.hpi.bpt.chimera.jcomparser.saving.Connector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class used to read in Activity from BPMN standard.
 * Example String
 * <bpmn:task id="Task_0c9phqs" name="Drink coffee">
 * <bpmn:incoming>SequenceFlow_0ct04t8</bpmn:incoming>
 * <bpmn:outgoing>SequenceFlow_1gyvb8d</bpmn:outgoing>
 * </bpmn:task>
 */
@XmlRootElement(name = "bpmn:task")
@XmlAccessorType(XmlAccessType.NONE)
public class Task extends AbstractDataControlNode {
	@Override
	public int save() {
		Connector connector = new Connector();
		this.databaseId = connector.insertControlNode(this.getName(), "Activity", this.getFragmentId(), this.getId());
		return this.databaseId;
	}
}
