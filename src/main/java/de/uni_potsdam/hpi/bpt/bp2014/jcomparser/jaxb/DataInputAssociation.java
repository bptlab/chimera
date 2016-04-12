package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;

import javax.xml.bind.annotation.*;
import java.util.Map;

/**
 *
 */
@XmlRootElement(name = "bpmn:dataInputAssociation")
@XmlAccessorType(XmlAccessType.NONE)
public class DataInputAssociation extends Edge {
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

}
