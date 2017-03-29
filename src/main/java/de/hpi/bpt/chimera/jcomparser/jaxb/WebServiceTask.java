package de.hpi.bpt.chimera.jcomparser.jaxb;

import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import org.apache.commons.lang3.StringEscapeUtils;

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

	@Override
	public int save() {
		Connector connector = new Connector();
		this.databaseId = connector.insertControlNode(this.getName(), "WebServiceTask", this.getFragmentId(), this.getId());
		connector.insertWebServiceTask(this.databaseId, StringEscapeUtils.unescapeHtml4(this.webServiceUrl), this.webServiceMethod, this.webServiceBody);
		return this.databaseId;
	}

}
