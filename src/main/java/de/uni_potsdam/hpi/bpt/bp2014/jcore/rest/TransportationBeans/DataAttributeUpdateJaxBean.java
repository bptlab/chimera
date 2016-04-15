package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.TransportationBeans;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
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
