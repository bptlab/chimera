package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataClass;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 *
 */
public class DataClassTest  {
    private Document document = new DocumentImpl(null);
    private String rootDataClass;
    private String normalDataClass;

    @Before
    public void setupRootDataClass(){
        rootDataClass = new JSONObject()
                .put("name", "Reise")
                .put("_id", "801101005L")
                .put("attributes", new JSONArray()
                        .put(new JSONObject()
                                .put("name", "Beginn")
                                .put("datatype", "String")
                                .put("_id", "editorId3")
                        )
                        .put(new JSONObject()
                                .put("name", "Ende")
                                .put("datatype", "String")
                                .put("_id", "editorId3")
                        )
                        .put(new JSONObject()
                                .put("name", "Gesamtkosten")
                                .put("datatype", "Float")
                                .put("_id", "editorId3")
                        )
                ).toString();

    }

    @Before
    public void setupNormalDataClass(){
        normalDataClass = new JSONObject()
                .put("name", "Flug")
                .put("_id", "679826034L")
                .put("attributes", new JSONArray()
                        .put(new JSONObject()
                                .put("name", "Abflugsdatum")
                                .put("datatype", "String")
                                .put("_id", "editorId1")
                        )
                        .put(new JSONObject()
                                .put("name", "Ankunftsdatum")
                                .put("datatype", "String")
                                .put("_id", "editorId2")
                        )
                        .put(new JSONObject()
                                .put("name", "StartFlughafen")
                                .put("datatype", "String")
                                .put("_id", "editorId3")
                        )
                        .put(new JSONObject()
                                .put("name", "EndFlughafen")
                                .put("datatype", "String")
                                .put("_id", "editorId4")
                        )
                ).toString();
    }

    @Test
    public void testRootDataClass(){
        DataClass dClass = new DataClass(rootDataClass);
        Assert.assertEquals("ID has not been set correctly", "801101005L", dClass.getModelId());
        Assert.assertEquals("Name has not been set correctly", "Reise", dClass.getName());
        Assert.assertEquals("Attributes have not been set correctly", 3, dClass.getAttributes().size());
        String[] attribute = {"Beginn","Ende","Gesamtkosten"};
        for(int i = 0; i < dClass.getAttributes().size(); i++){
            Assert.assertEquals("Attribute" + i + "has not been set correctly", attribute[i], dClass.getAttributes().get(i).getDataAttributeName());
        }
    }

    @Test
    public void testNormalDataClass(){
        DataClass dClass = new DataClass(normalDataClass);
        Assert.assertEquals("ID has not been set correctly", "679826034L", dClass.getModelId());
        Assert.assertEquals("Name has not been set correctly", "Flug", dClass.getName());
        Assert.assertEquals("Attributes have not been set correctly", 4, dClass.getAttributes().size());
        String[] attribute = {"Abflugsdatum","Ankunftsdatum","StartFlughafen", "EndFlughafen"};
        for(int i = 0; i < dClass.getAttributes().size(); i++){
            Assert.assertEquals("Attribute" + i + "has not been set correctly", attribute[i], dClass.getAttributes().get(i).getDataAttributeName());
        }
    }

    @Test
    public void testSaving() {
        Assert.fail();
    }
}
