package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;


import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
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
    private Element dataClass;

    @Before
    public void setupDataClass(){
        dataClass = document.createElement("node");
        dataClass.appendChild(createProperty("text", "Reise"));
        dataClass.appendChild(createProperty("#attributes", "{0}+Beginn ;{1}+Ende ;{2}+Gesamtkosten ;"));
        dataClass.appendChild(createProperty("#id", "801101005"));
        dataClass.appendChild(createProperty("#type", "net.frapu.code.visualization.domainModel.DomainClass"));
        dataClass.appendChild(createProperty("stereotype", "root_instance"));
    }

    /**
     *
     * @param name
     * @param value
     * @return
     */
    private Element createProperty(String name, String value) {
        if (null == document) {
            return null;
        }
        Element property = document.createElement("property");
        property.setAttribute("name", name);
        property.setAttribute("value", value);
        return property;
    }

    @Test
    public void testDataAttributeSetMethod(){
        DataClass dClass = new DataClass();
        dClass.initializeInstanceFromXML(dataClass);
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
