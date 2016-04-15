package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.TransportationBeans;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 *
 */
@XmlRootElement
public class DataObjectSetsJaxBean {
    @XmlAnyAttribute
    private Map<String, String> dataObjects;
    private int id;
    private String linkDataObject;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getLinkDataObject() {
        return linkDataObject;
    }

    public void setLinkDataObject(String linkDataObject) {
        this.linkDataObject = linkDataObject;
    }

    Map<String, String> getDataObjects() {
        return this.dataObjects;
    }

    public void setDataObjects(Map<String, String> dataObjects) {
        this.dataObjects = dataObjects;
    }
}
