package de.hpi.bpt.chimera.jcore.rest.TransportationBeans;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 */
@XmlRootElement
public class ActivityJaxBean {
    private int id;
    private String label;
    private String outputSetLink;
    private String inputSetLink;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOutputSetLink() {
        return outputSetLink;
    }

    public void setOutputSetLink(String outputSetLink) {
        this.outputSetLink = outputSetLink;
    }

    public String getInputSetLink() {
        return inputSetLink;
    }

    public void setInputSetLink(String inputSetLink) {
        this.inputSetLink = inputSetLink;
    }
}