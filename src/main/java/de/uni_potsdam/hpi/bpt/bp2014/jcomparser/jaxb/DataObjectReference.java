package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 */
@XmlRootElement(name = "bpmn:dataObjectReference")
@XmlAccessorType(XmlAccessType.NONE)
public class DataObjectReference {
    @XmlAttribute
    private String id;

    @XmlAttribute
    private String name;
    /**
     * Id of an data object. This construct is used to express that, an data object can
     * occur multiple times in an BPMN process.
     */
    @XmlAttribute
    private String dataObjectRef;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataObjectRef() {
        return dataObjectRef;
    }

    public void setDataObjectRef(String dataObjectRef) {
        this.dataObjectRef = dataObjectRef;
    }
}
