package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class used to read a Task with resource
 * Example String:
 *     <resource:resourceTask id="ResourceTask_1aas10p" name="this is a Resource Task">
 *       <bpmn:incoming>SequenceFlow_0yzfiqa</bpmn:incoming>
 *       <bpmn:outgoing>SequenceFlow_0nzrmhc</bpmn:outgoing>
 *     </resource:resourceTask>
 */
@XmlRootElement(name = "resource:resourceTask")
@XmlAccessorType(XmlAccessType.NONE)
public class ResourceTask extends AbstractDataControlNode{

}
