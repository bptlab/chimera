package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.AbstractControlNode;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@XmlTransient
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractDataControlNode extends AbstractControlNode {
    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "name")
    protected String name = "";
    @XmlElement(name = "bpmn:incoming")
    protected String incoming = "";
    @XmlElement(name = "bpmn:outgoing")
    protected String outgoing = "";
    @XmlElement(name = "bpmn:dataOutputAssociation")
    protected List<DataOutputAssociation> dataOutputAssociations = new ArrayList<>();
    @XmlElement(name = "bpmn:dataInputAssociation")
    protected List<DataInputAssociation> dataInputAssociations = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DataOutputAssociation> getDataOutputAssociations() {
        return dataOutputAssociations;
    }

    public void setDataOutputAssociations(List<DataOutputAssociation> dataOutputAssociations) {
        this.dataOutputAssociations = dataOutputAssociations;
    }

    public List<DataInputAssociation> getDataInputAssociations() {
        return dataInputAssociations;
    }

    public void setDataInputAssociations(List<DataInputAssociation> dataInputAssociations) {
        this.dataInputAssociations = dataInputAssociations;
    }

    public void setIncoming(String incoming) {
        List<String> incomingList = new ArrayList<>();
        incomingList.add(incoming);
        this.setIncoming(incomingList);
    }

    public void setOutgoing(String incoming) {
        List<String> outgoingList = new ArrayList<>();
        outgoingList.add(incoming);
        this.setOutgoing(outgoingList);
    }
}
