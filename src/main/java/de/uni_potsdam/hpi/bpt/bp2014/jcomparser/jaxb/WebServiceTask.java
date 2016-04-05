package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.xml.bind.annotation.*;

/**
 * Class used to read in a Service Task from BPMN standard.
 * Example String
 * <bpmn:serviceTask id="ServiceTask_165z0yh" name="Execute service">
 *      <bpmn:incoming>SequenceFlow_0ct04t8</bpmn:incoming>
 *      <bpmn:outgoing>SequenceFlow_1gyvb8d</bpmn:outgoing>
 * </bpmn:serviceTask>
 */
@XmlRootElement(name = "bpmn:serviceTask")
@XmlAccessorType(XmlAccessType.NONE)
public class WebServiceTask extends Task{

    @XmlAttribute(name = "griffin:webserviceurl")
    String webServiceUrl = "";

    @XmlAttribute(name = "griffin:webservicemethod")
    String webServiceMethod = "GET";

    @XmlAttribute(name = "griffin:webservicebody")
    String webServiceBody = "";

    @Override
    public int save() {
        Connector connector = new Connector();
        this.databaseId = connector.insertControlNodeIntoDatabase(
                this.getName(), "WebServiceTask", this.getFragmentId(), this.getId());
        connector.insertWebServiceTaskIntoDatabase(this.databaseId,
                StringEscapeUtils.unescapeHtml4(this.webServiceUrl),
                this.webServiceMethod,
                this.webServiceBody);
        return this.databaseId;
    }
}
