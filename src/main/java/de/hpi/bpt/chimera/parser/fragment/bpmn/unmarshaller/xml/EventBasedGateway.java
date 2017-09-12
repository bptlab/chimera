package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <bpmn:eventBasedGateway id=\"EventBasedGateway_1m6jwvb\">\n
 * <bpmn:incoming>SequenceFlow_1iz21bo</bpmn:incoming>\n
 * <bpmn:outgoing>SequenceFlow_1rcgg1s</bpmn:outgoing>\n
 * <bpmn:outgoing>SequenceFlow_01940kc</bpmn:outgoing>\n
 * <bpmn:outgoing>SequenceFlow_034so3b</bpmn:outgoing>\n
 * </bpmn:eventBasedGateway>\n
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class EventBasedGateway extends AbstractControlNode {

}
