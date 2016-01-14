package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.w3c.dom.Node;

import java.util.LinkedList;
import java.util.List;

/**
 * This class represents an EventType.
 */
public class EventType implements IDeserialisableJson, IPersistable {
    /**
     * This is the modelID of the eventType.
     */
    private long eventTypeModelID;
    /**
     * This is the databaseID of the eventType.
     */
    private int eventTypeID;
    /**
     * This is the name of the eventType.
     */
    private String eventTypeName;
    /**
     * This is a list containing all eventTypeAttributes belonging to this eventType.
     */
    private List<EventTypeAttribute> eventTypeAttributes = new LinkedList<>();
    /**
     * This contains the JSON representation of the eventType.
     */
    private JSONObject eventTypeJson;

    /**
     * Sets the processeditorServerUrl which is needed for connecting to the server
     * in order to get the XML-files for the fragments.
     *
     * @param serverURL This is the server URL.
     */
    public eventType(String serverURL) {

    }

    /**
     * The standard constructor.
     */
    public eventType() {
    }

    /**
     * This initializes the eventType from the JSON.
     *
     * @param element The JSON String which will be used for deserialisation
     */
    @Override public void initializeInstanceFromJson(final String element) {
        try {
            this.eventTypeJson = new JSONObject(element);

            this.eventTypeName = this.eventTypeJson.getString("name");
            this.eventTypeModelID = this.eventTypeJson.getLong("_id");
            generateEventTypeAttributeList(this.eventTypeJson.getJSONArray("attributes"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override public void initializeInstanceFromXML(final Node element) {

    }

    /**
     * This method saves the eventType to the database.
     *
     * @return the databaseID of the eventType.
     */
    @Override public int save() {
        Connector conn = new Connector();
        this.eventTypeID = conn.insertEventTypeIntoDatabase(this.eventTypeName);
        saveEventTypeAttributes();

        return eventTypeID;
    }

    /**
     * This method gets all the eventTypeAttributes from the JSON.
     * EventTypeAttributes can only be alphanumerical.
     *
     * @param jsonAttributes This JSONArray contains all eventTypeAttributes from the JSON.
     */
    private void generateEventTypeAttributeList(JSONArray jsonAttributes) {
        int length = jsonAttributes.length();
        for (int i = 0; i < length; i++) {
            EventTypeAttribute eventTypeAttribute = new EventTypeAttribute(
                    jsonAttributes.getJSONObject(i).getString("name"),
                    jsonAttributes.getJSONObject(i).getString("datatype")
            );
            this.eventTypeAttributes.add(eventTypeAttribute);
        }
    }

    /**
     * This method iterates through all eventTypeAttributes and sets
     * the eventType for them as well as calling the save method.
     */
    private void saveEventTypeAttributes() {
        for (EventTypeAttribute eventTypeAttribute : this.eventTypeAttributes) {
            eventTypeAttribute.setEventTypeID(eventTypeID);
            eventTypeAttribute.save();
        }
    }

    public int getEventTypeID() {
        return eventTypeID;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public List<EventTypeAttribute> getEventTypeAttributes() {
        return eventTypeAttributes;
    }


    public JSONObject getEventTypeJson() {
        return eventTypeJson;
    }

    public long getEventTypeModelID() {
        return eventTypeModelID;
    }

}
