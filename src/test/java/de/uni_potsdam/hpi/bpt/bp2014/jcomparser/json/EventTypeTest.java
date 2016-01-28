package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

/**
 *
 */
public class EventTypeTest {

    private String eventType;

    @Before
    public void setupEventType(){
        eventType = new JSONObject()
                .put("name", "TestEvent")
                .put("_id", 679826037L)
                .put("attributes", new JSONArray()
                        .put(new JSONObject()
                                .put("name", "EventWert")
                                .put("datatype", "String"))
                        .put(new JSONObject()
                                .put("name", "Eintrittsdatum")
                                .put("datatype", "String"))

                ).toString();
    }

    @Test@Ignore
    public void testEventType() {
        EventType eType = new EventType();
        eType.initializeInstanceFromJson(eventType);
        Assert.assertEquals("ID has not been set correctly", 679826037L, eType.getEventTypeID());
        Assert.assertEquals("Name has not been set correctly", "TestEvent", eType.getEventTypeName());
        Assert.assertEquals("Attributes have not been set correctly", 2, eType.getEventTypeAttributes().size());
        String[] attributes = {"EventWert", "Eintrittsdatum"};
        for (int i = 0; i < eType.getEventTypeAttributes().size(); i++) {
            Assert.assertEquals("Attribute" + i + "has not been set correctly", attributes[i],
                    eType.getEventTypeAttributes().get(i).getEventTypeAttributeName());
        }
    }

}
