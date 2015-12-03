package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import org.easymock.IAnswer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;


/**
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DomainModel.class})
public class DomainModelTest  {

    /**
     * This is the domainModel used for testing.
     */
    private DomainModel domainModel;

    /**
     * This mocks the domainModel needed for the tests.
     */
    @Before
    public void setupDomainModel(){
        try {
            String jsonDomainModel = new JSONObject()
                    .put("name", "Domain_Reise")
                    .put("_id", 269479299L)
                    .put("revision", 0)
                    .put("dataclasses", new JSONArray()
                            .put(new JSONObject()
                                    .put("name", "Reise")
                                    .put("_id", 801101005L)
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
                                                    .put("datatype", "Float")))
                            )
                            .put(new JSONObject()
                                    .put("name", "Flug")
                                    .put("_id", 679826034L)
                                    .put("is_root", false)
                                    .put("attributes", new JSONArray()
                                            .put(new JSONObject()
                                                    .put("name", "Abflugsdatum")
                                                    .put("datatype", "String"))
                                            .put(new JSONObject()
                                                    .put("name", "Ankunftsdatum")
                                                    .put("datatype", "String"))
                                            .put(new JSONObject()
                                                    .put("name", "StartFlughafen")
                                                    .put("datatype", "String"))
                                            .put(new JSONObject()
                                                    .put("name", "EndFlughafen")
                                                    .put("datatype", "String")))
                            )
                            .put(new JSONObject()
                                    .put("name", "bike")
                                    .put("_id", 940990347L)
                                    .put("is_root", false)
                                    .put("attributes", new JSONArray()
                                            .put(new JSONObject()
                                                    .put("name", "Name")
                                                    .put("datatype", "String"))
                                            .put(new JSONObject()
                                                    .put("name", "Rating")
                                                    .put("datatype", "String"))
                                            .put(new JSONObject()
                                                    .put("name", "Adresse")
                                                    .put("datatype", "String"))
                                            .put(new JSONObject()
                                                    .put("name", "AnzahlNaechte")
                                                    .put("datatype", "Integer")))
                            )
                            .put(new JSONObject()
                                    .put("name", "Person")
                                    .put("_id", 1175017344L)
                                    .put("is_root", false)
                                    .put("attributes", new JSONArray()
                                            .put(new JSONObject()
                                                    .put("name", "Name")
                                                    .put("datatype", "String"))
                                            .put(new JSONObject()
                                                    .put("name", "Anschrift")
                                                    .put("datatype", "String"))
                                            .put(new JSONObject()
                                                    .put("name", "Alter")
                                                    .put("datatype", "Integer")))
                            )
                    )
                    .put("aggregations", new JSONArray()
                            .put(new JSONObject()
                                    .put("sourceMultiplicity", "1..*")
                                    .put("targetMultiplicity", "1..*")
                                    .put("sourceNode", 801101005L)
                                    .put("targetNode", 679826034L))
                            .put(new JSONObject()
                                    .put("sourceMultiplicity", "1..*")
                                    .put("targetMultiplicity", "1..*")
                                    .put("sourceNode", 801101005L)
                                    .put("targetNode", 940990347L))
                            .put(new JSONObject()
                                    .put("sourceMultiplicity", "1..*")
                                    .put("targetMultiplicity", "1..*")
                                    .put("sourceNode", 801101005L)
                                    .put("targetNode", 1175017344L))
                    )
                    .toString();
            domainModel = new DomainModel();
            domainModel.initializeInstanceFromJson(jsonDomainModel);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method checks if the deserialization sets the correct domainModelID.
     */
    @Test
    public void testSimpleDomainModelDeserialization(){
        Assert.assertEquals("The DomainModelID has not been set or is wrong", 269479299, domainModel.getDomainModelModelID());
    }

    /**
     * This checks if the dataClasses have been set.
     */
    @Test
    public void testDataClassesSetCorrectly(){
        Assert.assertNotNull("The first DataClass has not been set correctly", domainModel.getDataClasses().get(801101005L));
        Assert.assertNotNull("The second DataClass has not been set correctly", domainModel.getDataClasses().get(679826034L));
        Assert.assertNotNull("The third DataClass has not been set correctly", domainModel.getDataClasses().get(940990347L));
        Assert.assertNotNull("The fourth DataClass has not been set correctly", domainModel.getDataClasses().get(1175017344L));

    }

    /**
     * This method checks if the aggregations have been set.
     */
    @Test
    public void testAggregationSetCorrectly() {
        Assert.assertEquals("The aggregation has not been set correctly", 3, domainModel.getAggregations().size());
        Assert.assertNotNull("The sourceNode has not been set correctly", domainModel.getAggregations().get(0).getDataClasses().get(801101005L));
        for (int i = 0; i < domainModel.getAggregations().size(); i++) {
            Assert.assertEquals("The multiplicity has not been set correctly", Integer.MAX_VALUE, domainModel.getAggregations().get(i).getSourceMultiplicity());
        }
    }

    /**
     * This method checks if the version has been set correctly.
     */
    @Test
    public void testVersionSetCorrectly(){
        Assert.assertEquals("The version has not been set correctly", 0, domainModel.getVersionNumber());
    }
}
