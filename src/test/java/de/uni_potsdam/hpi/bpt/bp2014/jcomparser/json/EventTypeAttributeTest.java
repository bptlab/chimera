package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
/**
 *
 */
public class EventTypeAttributeTest {

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

    @Test
    public void testEventTypeAttributeSetMethod(){
        EventType eType = new EventType();
        eType.initializeInstanceFromJson(eventType);
        int etid = 42;
        for(EventTypeAttribute attr : eType.getEventTypeAttributes()) {
           attr.setEventTypeID(etid++);
        }
        for(int i = 0; i < eType.getEventTypeAttributes().size(); i++){
            Assert.assertEquals("EventTypeID has not been set correctly", 42 + i,
                    eType.getEventTypeAttributes().get(i).getEventTypeID());
        }
    }

    @Test
    public void testDataAttribute(){
        DataAttribute dataAttribute = new DataAttribute("state");
        Assert.assertEquals("AttributeName has been set correctly", "state", dataAttribute.getDataAttributeName());
        Assert.assertEquals("AttributeType has not been set correctly", "", dataAttribute.getDataAttributeType());
    }
}
