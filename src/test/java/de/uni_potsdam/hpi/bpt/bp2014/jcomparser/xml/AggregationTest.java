package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ihdefix on 12.03.2015.
 */
public class AggregationTest {
    private Document document = new DocumentImpl(null);
    private Map<Long, DataClass> dataClasses = new HashMap<>();
    private Element aggregation;
    private Element sourceDataClass;
    private Element targetDataClass;

    @Before
    public void setupAggregation(){
        aggregation = document.createElement("edge");
        aggregation.appendChild(createProperty("sourceMultiplicity","1..*"));
        aggregation.appendChild(createProperty("#id","470092328"));
        aggregation.appendChild(createProperty("#type","net.frapu.code.visualization.domainModel.Aggregation"));
        aggregation.appendChild(createProperty("#sourceNode","801101005"));
        aggregation.appendChild(createProperty("#targetNode","679826034"));

    }

    @Before
    public void setupSourceDataClass(){
        sourceDataClass = document.createElement("node");
        sourceDataClass.appendChild(createProperty("text", "Reise"));
        sourceDataClass.appendChild(createProperty("#attributes", "{0}+Beginn ;{1}+Ende ;{2}+Gesamtkosten ;"));
        sourceDataClass.appendChild(createProperty("#id", "801101005"));
        sourceDataClass.appendChild(createProperty("#type", "net.frapu.code.visualization.domainModel.DomainClass"));
        sourceDataClass.appendChild(createProperty("stereotype", "root_instance"));
        addToDataClasses(sourceDataClass);
    }

    @Before
    public void setupTargetDataClass(){
        targetDataClass = document.createElement("node");
        targetDataClass.appendChild(createProperty("text", "Flug"));
        targetDataClass.appendChild(createProperty("#attributes", "{0}+Abflugsdatum ;{1}+Ankunftsdatum ;{2}+StartFlughafen ;{3}+EndFlughafen ;"));
        targetDataClass.appendChild(createProperty("#id", "679826034"));
        targetDataClass.appendChild(createProperty("#type", "net.frapu.code.visualization.domainModel.DomainClass"));
        targetDataClass.appendChild(createProperty("stereotype", ""));
        addToDataClasses(targetDataClass);
    }

    private void addToDataClasses(Element dataClass){
        DataClass dClass = new DataClass();
        dClass.initializeInstanceFromXML(dataClass);
        dataClasses.put(dClass.getDataClassModelID(), dClass);
    }

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
    public void testSourceDataClass(){
        Assert.assertNotNull("ID has not been set correctly", dataClasses.get(801101005L));
        Assert.assertEquals("Name has not been set correctly", "Reise", dataClasses.get(801101005L).getDataClassName());
        Assert.assertEquals("Attributes have not been set correctly", 3, dataClasses.get(801101005L).getDataAttributes().size());
    }

    @Test
    public void testTargetDataClass(){
        Assert.assertNotNull("ID has not been set correctly", dataClasses.get(679826034L));
        Assert.assertEquals("Name has not been set correctly", "Flug", dataClasses.get(679826034L).getDataClassName());
        Assert.assertEquals("Attributes have not been set correctly", 4, dataClasses.get(679826034L).getDataAttributes().size());
    }

    @Test
    public void testAggregation(){
        Aggregation aggregate = new Aggregation();
        aggregate.setDataClasses(dataClasses);
        aggregate.initializeInstanceFromXML(aggregation);
        Assert.assertEquals("Multiplicity has not been set correctly", Integer.MAX_VALUE, aggregate.getMultiplicity());
        Assert.assertEquals("SourceName has not been set correctly", "Reise", aggregate.getSource().getDataClassName());
        Assert.assertEquals("SourceId has not been set correctly", 801101005L, aggregate.getSource().getDataClassModelID());
        Assert.assertEquals("TargetName has not been set correctly", "Flug", aggregate.getTarget().getDataClassName());
        Assert.assertEquals("TargetId has not been set correctly", 679826034L, aggregate.getTarget().getDataClassModelID());

    }
}
