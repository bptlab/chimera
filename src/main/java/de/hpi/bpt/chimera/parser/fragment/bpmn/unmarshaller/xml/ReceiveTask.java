package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 */
@XmlRootElement(name = "bpmn:receiveTask")
@XmlAccessorType(XmlAccessType.NONE)
public class ReceiveTask extends AbstractDataControlNode {
	/* "<bpmn:receiveTask id="ReceiveTask_12m516y" name="ReceiveTask" griffin:eventquery="SELECT * FROM Event">
            <bpmn:incoming>SequenceFlow_0vy2x8y</bpmn:incoming>
            <bpmn:outgoing>SequenceFlow_1uw5t49</bpmn:outgoing>
        </bpmn:receiveTask>" */

	@XmlAttribute(name = "griffin:eventquery")
	private String eventQuery;
}
