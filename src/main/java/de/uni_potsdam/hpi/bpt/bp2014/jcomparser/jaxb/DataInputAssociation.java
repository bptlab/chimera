package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

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
}
