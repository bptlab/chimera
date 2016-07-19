package de.hpi.bpt.chimera.jcore.rest.TransportationBeans;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents an update of a activity
 */
@XmlRootElement
public class DataAttributeUpdateJaxBean {
    private int id;
    private String value;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
