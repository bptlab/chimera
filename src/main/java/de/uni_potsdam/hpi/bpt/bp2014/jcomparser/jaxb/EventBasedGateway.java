package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.AbstractControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 *<bpmn:eventBasedGateway id=\"EventBasedGateway_1m6jwvb\">\n
 * <bpmn:incoming>SequenceFlow_1iz21bo</bpmn:incoming>\n
 * <bpmn:outgoing>SequenceFlow_1rcgg1s</bpmn:outgoing>\n
 * <bpmn:outgoing>SequenceFlow_01940kc</bpmn:outgoing>\n
 * <bpmn:outgoing>SequenceFlow_034so3b</bpmn:outgoing>\n
 * </bpmn:eventBasedGateway>\n
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class EventBasedGateway extends AbstractControlNode {
    @XmlElement(name = "bpmn:incoming")
    private String incoming;

    @XmlAttribute(name = "id")
    private String id;

    @XmlElement(name = "bpmn:outgoing")
    private List<String> outgoing = new ArrayList<>();

    public List<String> getOutgoing() {
        return outgoing;
    }

    public String getIncoming() {
        return incoming;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public int save() {
        Connector connector = new Connector();
        this.databaseId = connector.insertControlNodeIntoDatabase(
                "", "EventBasedGateway", this.getFragmentId(), this.id);
        return this.databaseId;
    }
}
