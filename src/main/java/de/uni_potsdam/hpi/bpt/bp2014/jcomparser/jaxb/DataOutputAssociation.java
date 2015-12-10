package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.Edge;

import javax.xml.bind.annotation.*;

/**
 *
 */

@XmlRootElement(name = "bpmn:dataOutputAssociation")
@XmlAccessorType(XmlAccessType.NONE)
public class DataOutputAssociation {
    @XmlAttribute
    private String id;
    @XmlElement(name = "bpmn:targetRef")
    private String targetRef;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTargetRef() {
        return targetRef;
    }

    public void setTargetRef(String targetRef) {
        this.targetRef = targetRef;
    }

    public Edge convertToEdge(String taskId) {
        Edge edge = new Edge();
        edge.setId(this.id);
        edge.setIsDataInput(false);
        edge.setTargetNodeId(this.targetRef);
        edge.setSourceNodeId(taskId);
        edge.setType("Association");
        return edge;
    }
}
