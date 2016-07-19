package de.hpi.bpt.chimera.jcomparser.jaxb;

import de.hpi.bpt.chimera.jcomparser.saving.AbstractControlNode;
import de.hpi.bpt.chimera.jcomparser.saving.Connector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to store parallel Gateway. An exclusive gateway can have an arbitrary number
 * of incoming control flows and one outgoing control flow.
 * Example String:
 * <bpmn:parallelGateway id="ParallelGateway_1kginh1">
 *  <bpmn:incoming>SequenceFlow_08t16y9</bpmn:incoming>
 *  <bpmn:incoming>SequenceFlow_1llhwid</bpmn:incoming>
 *  <bpmn:outgoing>SequenceFlow_08rfwur</bpmn:outgoing>
 * </bpmn:parallelGateway>
 */
@XmlRootElement(name = "bpmn:parallelGateway")
@XmlAccessorType(XmlAccessType.NONE)
public class ParallelGateway extends AbstractControlNode {
    @XmlAttribute(name = "name")
    private String name = "";

    public String getName() {
        return name;
    }

    @Override
    public int save() {
        Connector connector = new Connector();
        this.databaseId = connector.insertControlNodeIntoDatabase(
                this.getName(), "AND", this.getFragmentId(), this.getId());
        return this.databaseId;
    }
}
