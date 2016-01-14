package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;

/**
 * This class represents a DataAttribute.
 */
public class EventTypeAttribute implements IPersistable {
    /**
     * This is the databaseID of the eventType belonging to this attribute.
     */
    private int eventTypeID = -1;
    /**
     * This is the name of the attribute.
     */
    private String eventTypeAttributeName;
    /**
     * This is the type of the attribute.
     */
    private String eventTypeAttributeType;
    /**
     * This is the databaseID of the attribute.
     */
    private int eventTypeAttributeID;

    /**
     * Constructor which sets the Name of the eventTypeAttribute.
     *
     * @param name This is the name of the eventTypeAttribute.
     */
    public EventTypeAttribute(String name) {
        this.eventTypeAttributeName = name;
        this.eventTypeAttributeType = "";
    }

    /**
     * Constructor which sets the Name and Type of the EventTypeAttribute.
     *
     * @param name This is the name of the EventTypeAttribute.
     * @param type This is the dataype of the EventTypeAttribute.
     */
    public EventTypeAttribute(String name, String type) {
        this.eventTypeAttributeName = name;
        this.eventTypeAttributeType = type;
    }

    /**
     * This constructor is only used for testCases
     * therefore, a connection to the server is not needed.
     */
    public EventTypeAttribute() {
    }

    /**
     * This method sets the databaseID of the eventType corresponding to the eventTypeAttribute.
     *
     * @param id This is the databaseID of the eventType
     */
    public void setEventTypeID(final int id) {
        this.eventTypeID = id;
    }

    @Override public int save() {
        Connector conn = new Connector();
        this.eventTypeAttributeID = conn.insertEventTypeAttributeIntoDatabase(
                this.eventTypeAttributeName, this.eventTypeID,
                this.eventTypeAttributeType);
        return eventTypeAttributeID;
    }

    public int getEventTypeID() {
        return eventTypeID;
    }

    public int getEventTypeAttributeID() {
        return eventTypeAttributeID;
    }

    public String getEventTypeAttributeType() {
        return eventTypeAttributeType;
    }

    public String getEventTypeAttributeName() {
        return eventTypeAttributeName;
    }
}
