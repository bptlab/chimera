package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;


import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 *
 */
public class DataAttributeTest  {
    private Document document = new DocumentImpl(null);
    private String dataClass;

    @Before
    public void setupDataClass(){
        dataClass = new JSONObject()
                .put("name", "Reise")
                .put("_id", "801101005")
                .put("is_root", true)
                .put("attributes", new JSONArray()
                        .put(new JSONObject()
                                .put("name", "Beginn")
                                .put("datatype", "String"))
                        .put(new JSONObject()
                                .put("name", "Ende")
                                .put("datatype", "String"))
                        .put(new JSONObject()
                                .put("name", "Gesamtkosten")
                                .put("datatype", "Float"))
                ).toString();
    }

    @Test
    public void testDataAttributeSetMethod(){
        DataClass dClass = new DataClass();
        dClass.initializeInstanceFromJson(dataClass);
        for(DataAttribute attr : dClass.getDataAttributes()){
            attr.setDataClassID(42);
        }
        for(int i = 0; i < dClass.getDataAttributes().size(); i++){
            Assert.assertEquals("DataClassID has not been set correctly", 42, dClass.getDataAttributes().get(i).getDataClassID());
        }
    }

    @Test
    public void testDataAttribute(){
        DataAttribute dataAttribute = new DataAttribute("state");
        Assert.assertEquals("AttributeName has been set correctly", "state", dataAttribute.getDataAttributeName());
        Assert.assertEquals("AttributeType has not been set correctly", "", dataAttribute.getDataAttributeType());
    }
}
