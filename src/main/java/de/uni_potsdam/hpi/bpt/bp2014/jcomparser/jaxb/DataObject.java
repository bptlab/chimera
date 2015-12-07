package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 */
@XmlRootElement(name = "bpmn:dataObject")
@XmlAccessorType(XmlAccessType.NONE)
public class DataObject {
    @XmlAttribute
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
