package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.Edge;

import javax.xml.bind.annotation.*;

/**
 *
 */
@XmlRootElement(name = "bpmn:dataInputAssociation")
@XmlAccessorType(XmlAccessType.NONE)
public class DataInputAssociation {
    @XmlAttribute
    private String id;
    @XmlElement(name = "bpmn:sourceRef")
    private String sourceRef;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceRef() {
        return sourceRef;
    }

    public void setSourceRef(String sourceRef) {
        this.sourceRef = sourceRef;
    }

    public Edge convertToEdge(String taskId) {
        Edge edge = new Edge();
        edge.setId(this.id);
        edge.setIsDataInput(true);
        edge.setTargetNodeId(taskId);
        edge.setSourceNodeId(this.sourceRef);
        edge.setType("Association");
        return edge;
    }
}
