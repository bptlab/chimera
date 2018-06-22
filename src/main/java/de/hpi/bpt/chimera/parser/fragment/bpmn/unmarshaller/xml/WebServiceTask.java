package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class used to read in a Service Task from BPMN standard.
 * Example String
 * <bpmn:serviceTask id="ServiceTask_165z0yh" name="Execute service">
 * <bpmn:incoming>SequenceFlow_0ct04t8</bpmn:incoming>
 * <bpmn:outgoing>SequenceFlow_1gyvb8d</bpmn:outgoing>
 * </bpmn:serviceTask>
 */
@XmlRootElement(name = "bpmn:serviceTask")
@XmlAccessorType(XmlAccessType.NONE)
public class WebServiceTask extends AbstractDataControlNode {

	@XmlAttribute(name = "griffin:webserviceurl")
	String webServiceUrl = "";

	@XmlAttribute(name = "griffin:webservicemethod")
	String webServiceMethod = "GET";

	@XmlAttribute(name = "griffin:webservicebody")
	String webServiceBody = "";

	@XmlAttribute(name = "griffin:webserviceheader")
	String webServiceHeader = "";

	@XmlAttribute(name = "griffin:contenttype")
	String contentType = "";


	public String getWebServiceUrl() {
		return webServiceUrl;
	}

	public String getWebServiceMethod() {
		return webServiceMethod;
	}

	public String getWebServiceBody() {
		return webServiceBody;
	}

	public String getWebServiceHeader() {
		return webServiceHeader;
	}

	public String getContentType() {
		return contentType;
	}
}
