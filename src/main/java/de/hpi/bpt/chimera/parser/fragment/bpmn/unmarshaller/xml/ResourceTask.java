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

//    @XmlAttribute(name = "resource:host")
    private String host = "https://rembrandt.voelker.dev/api";

//    @XmlAttribute(name = "resource:ID")
    private String ID = "5dcaa9c160966400118967aa";

//    @XmlAttribute(name = "resource:contentType")
    private String contentType = "application/vnd.api+json";

//    @XmlAttribute(name = "resource:problemDefinition")
    private String problemDefinition = "";

//    @XmlAttribute(name = "resource:optimizationMethod")
    private String optimizationMethod = "";


    public String getContentType() {
        return contentType;
    }

    public String getHost() {
        return host;
    }

    public String getID() {
        return ID;
    }

    public String getProblemDefinition() {
        return problemDefinition;
    }

    public String getOptimizationMethod() {
        return optimizationMethod;
    }
}
