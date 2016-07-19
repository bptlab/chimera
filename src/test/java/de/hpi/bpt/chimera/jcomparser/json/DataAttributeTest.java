package de.hpi.bpt.chimera.jcomparser.json;


import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;


/**
 *
 */
public class DataAttributeTest {
    private Document document = new DocumentImpl(null);
    private String dataClass;

    @Before
    public void setupDataClass() {
        dataClass = new JSONObject()
                .put("name", "Reise")
                .put("_id", "801101005")
                .put("attributes", new JSONArray()
                        .put(new JSONObject()
                                .put("name", "Beginn")
                                .put("datatype", "String")
                                .put("_id", "editorId1")
                        )
                        .put(new JSONObject()
                                .put("name", "Ende")
                                .put("datatype", "String")
                                .put("_id", "editorId2")
                        )
                        .put(new JSONObject()
                                .put("name", "Gesamtkosten")
                                .put("datatype", "Float")
                                .put("_id", "editorId3")
                        )
                ).toString();
    }

    @Test
    public void testDataAttributeSetMethod() {
        DataClass dClass = new DataClass(dataClass);
        for (DataAttribute attr : dClass.getAttributes()) {
            attr.setDataClassId(42);
        }
        for (int i = 0; i < dClass.getAttributes().size(); i++) {
            Assert.assertEquals("DataClassID has not been set correctly", 42, dClass.getAttributes().get(i).getDataClassId());
        }
    }

    @Test
    public void testDataAttribute() {
        DataAttribute dataAttribute = new DataAttribute("state", "String", "123EditorId");
        Assert.assertEquals("AttributeName has not been set correctly", "state", dataAttribute
                .getName());
        Assert.assertEquals("AttributeType has not been set correctly", "String", dataAttribute.getType());
    }

    @Test
    public void testTypedDataAttribute() {
        DataAttribute dataAttribute = new DataAttribute("state", "String", "123EditorId");
        Assert.assertEquals("AttributeName has not been set correctly", "state", dataAttribute
                .getName());
        Assert.assertEquals("AttributeType has not been set correctly", "String", dataAttribute
                .getType());
    }

    @Test
    public void testDataAttributeParsing() {
        DataClass dClass = new DataClass(dataClass);
        DataAttribute dAttribute = dClass.getAttributes().get(0);
        Assert.assertEquals("AttributeName has not been set correctly", "Beginn", dAttribute
                .getName());
        Assert.assertEquals("AttributeType has not been set correctly", "String", dAttribute
                .getType());
    }
}
