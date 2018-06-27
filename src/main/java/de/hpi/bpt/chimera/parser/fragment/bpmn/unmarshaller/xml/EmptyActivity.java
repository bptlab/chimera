package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class used to read in a Service Task from BPMN standard.
 * Example String
 *  <bpmn:callActivity id="Task_0wyhfye" name="EmptyActivity">
 *      <bpmn:incoming>SequenceFlow_0vy2x8y</bpmn:incoming>
 *      <bpmn:outgoing>SequenceFlow_1uw5t49</bpmn:outgoing>
 *  </bpmn:callActivity>
 */
@XmlRootElement(name = "bpmn:callActivity")
@XmlAccessorType(XmlAccessType.NONE)
public class EmptyActivity extends AbstractDataControlNode {
}
