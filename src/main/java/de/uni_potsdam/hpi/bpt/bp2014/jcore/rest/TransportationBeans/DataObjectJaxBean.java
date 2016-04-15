package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.TransportationBeans;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A JAX bean which is used for dataobject data.
 * It contains the data of one dataobject.
 * It can be used to create a JSON Object
 */
@XmlRootElement
public class DataObjectJaxBean {
    /**
     * The label of the data object.
     */
    private String label;
    /**
     * The id the dataobject (not the instance) has inside
     * the database.
     */
    private int id;
    /**
     * The state inside the database of the dataobject
     * which is stored in the table.
     * The label not the id will be saved.
     */
    private String state;
    /**
     * An array of all dataAttributes belonging to this dataObject.
     * Each attribute has an id, name, type and value.
     */
    private DataAttributeJaxBean[] attributeConfiguration;

    private int setId;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public DataAttributeJaxBean[] getAttributeConfiguration() {
        return attributeConfiguration;
    }

    public void setAttributeConfiguration(DataAttributeJaxBean[] attributeConfiguration) {
        this.attributeConfiguration = attributeConfiguration;
    }

    public int getSetId() {
        return setId;
    }

    public void setSetId(int setId) {
        this.setId = setId;
    }


}

