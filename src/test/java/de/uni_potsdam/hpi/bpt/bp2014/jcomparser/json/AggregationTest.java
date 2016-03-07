//package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;
//
//import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.w3c.dom.Document;
//
//import java.util.HashMap;
//import java.util.Map;
//
//
//
///**
// * These tests assure that the deserialization of an aggregation and its dataClasses works correctly.
// * Therefore, two dataClasses (one of them is a root node) which are connected by an aggregation are generated
// * and deserialized.
// */
//public class AggregationTest {
//    private Document document = new DocumentImpl(null);
//    private Map<Long, DataClass> dataClasses = new HashMap<>();
//    private String aggregation;
//    private String sourceDataClass;
//    private String targetDataClass;
//
//    /**
//     * Set up an aggregation represented as a Node by appending Elements that correspond to the structure of the XML
//     * the ProcessEditor generates.
//     */
//    @Before
//    public void setupAggregation(){
//        // this has changed, aggregations do not have a json object
//        // but are given via data class attributes
//    }
//    /**
//     * Set up a dataClass represented as a Node by appending Elements that correspond to the structure of the XML
//     * the ProcessEditor generates (including attributes etc.). This dataClass is the rootnode of the domainmodel.
//     */
//    @Before
//    public void setupSourceDataClass(){
//        sourceDataClass = new JSONObject()
//                .put("name", "Reise")
//                .put("_id", 801101005L)
//                .put("attributes", new JSONArray()
//                        .put(new JSONObject()
//                                .put("name", "Beginn")
//                                .put("datatype", "String"))
//                        .put(new JSONObject()
//                                .put("name", "Ende")
//                                .put("datatype", "String"))
//                        .put(new JSONObject()
//                                .put("name", "Gesamtkosten")
//                                .put("datatype", "Float"))
//                ).toString();
//        addToDataClasses(sourceDataClass);
//    }
//    /**
//     * Set up a dataClass represented as a Node by appending Elements that correspond to the structure of the XML
//     * the ProcessEditor generates (including attributes etc.). This dataClass is the second participant of the aggregation.
//     */
//    @Before
//    public void setupTargetDataClass(){
//        targetDataClass = new JSONObject()
//                .put("name", "Flug")
//                .put("_id", 679826034L)
//                .put("attributes", new JSONArray()
//                        .put(new JSONObject()
//                                .put("name", "Abflugsdatum")
//                                .put("datatype", "String"))
//                        .put(new JSONObject()
//                                .put("name", "Ankunftsdatum")
//                                .put("datatype", "String"))
//                        .put(new JSONObject()
//                                .put("name", "StartFlughafen")
//                                .put("datatype", "String"))
//                        .put(new JSONObject()
//                                .put("name", "EndFlughafen")
//                                .put("datatype", "String"))
//                ).toString();
//        addToDataClasses(targetDataClass);
//    }
//
//    /**
//     * Initialize a DataClass with help of the XML-representation and adding it to the list of DataClasses of this test.
//     * @param dataClass XML-node of the dataClass (according to the structure of the one the ProcessEditor generates)
//     */
//    private void addToDataClasses(String dataClass){
//        DataClass dClass = new DataClass();
//        dClass.initializeInstanceFromJson(dataClass);
//        dataClasses.put(dClass.getDataClassModelID(), dClass);
//    }
//
//    /**
//     * Test deserialization of the root node dataClass is correct.
//     */
//    @Test
//    public void testSourceDataClass(){
//        Assert.assertNotNull("ID has not been set correctly", dataClasses.get(801101005L));
//        Assert.assertEquals("Name has not been set correctly", "Reise", dataClasses.get(801101005L).getDataClassName());
//        Assert.assertEquals("Attributes have not been set correctly", 3, dataClasses.get(801101005L).getDataAttributes().size());
//    }
//    /**
//     * Test deserialization of the dataClass that is aggregated is correct
//     */
//    @Test
//    public void testTargetDataClass(){
//        Assert.assertNotNull("ID has not been set correctly", dataClasses.get(679826034L));
//        Assert.assertEquals("Name has not been set correctly", "Flug", dataClasses.get(679826034L).getDataClassName());
//        Assert.assertEquals("Attributes have not been set correctly", 4, dataClasses.get(679826034L).getDataAttributes().size());
//    }
//    /**
//     * Test deserialization of the aggregation is correct.
//     */
//    @Test
//    public void testAggregation(){
//        Aggregation aggregate = new Aggregation();
//        aggregate.setDataClasses(dataClasses);
//        aggregate.initializeInstanceFromJson(aggregation);
//        Assert.assertEquals("Source multiplicity has not been set correctly", Integer.MAX_VALUE, aggregate.getSourceMultiplicity());
//        Assert.assertEquals("Target multiplicity has not been set correctly", Integer.MAX_VALUE, aggregate.getTargetMultiplicity());
//        Assert.assertEquals("SourceName has not been set correctly", "Reise", aggregate.getSource().getDataClassName());
//        Assert.assertEquals("SourceId has not been set correctly", 801101005L, aggregate.getSource().getDataClassModelID());
//        Assert.assertEquals("TargetName has not been set correctly", "Flug", aggregate.getTarget().getDataClassName());
//        Assert.assertEquals("TargetId has not been set correctly", 679826034L, aggregate.getTarget().getDataClassModelID());
//    }
//}
