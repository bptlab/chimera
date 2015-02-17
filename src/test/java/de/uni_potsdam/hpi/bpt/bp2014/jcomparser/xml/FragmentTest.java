package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class tests the functionality of the xml-Fragment-class.
 */
public class FragmentTest {
    /**
     * the created fragment that needs to be tested
     */
    private Fragment fragment;
    /**
     * this Document contains the xml-content for the fragment
     */
    private Document fragmentDoc;

    /**
     * setUp all necessary variables for the test
     */
    @Before
    public void setup() {
        setupFragment();
    }
    /**
     * initialize the fragment from file "TestFragment1.xml" in the resources-folder
     */
    public void setupFragment() {
        try {
            File fragmentXML = new File("src/main/resources/TestFragment2.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            fragmentDoc = dBuilder.parse(fragmentXML);
            fragmentDoc.getDocumentElement().normalize();
            fragment = new Fragment();
            fragment.initializeInstanceFromXML(fragmentDoc.getDocumentElement());
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSimpleFragmentDeserialization(){
        Assert.assertEquals("The fragmentName has not been set correctly", "TestFragment2", fragment.getFragmentName());
        Assert.assertEquals("The fragmentID has not been set correctly", 1763775575, fragment.getFragmentID());
    }
    /**
     * rudimentary test for checking if the controlNodes are set "correctly" (simply check if there is a node with
     * the ID from the fragmentDoc in the controlNodes-Map of the fragment)
     */
    @Test
    public void testControlNodesSetCorrectly(){
        Assert.assertNotNull("The startEvent has not been set correctly", fragment.getControlNodes().get(1453251009));
        Assert.assertNotNull("The activity has not been set correctly", fragment.getControlNodes().get(1080362683));
        Assert.assertNotNull("The dataNode has not been set correctly", fragment.getControlNodes().get(1397906076));
        Assert.assertNotNull("The endEvent has not been set correctly", fragment.getControlNodes().get(735424916));
    }
    /**
     * rudimentary test for checking if the edges are set "correctly" (simply check if there is an edge with
     * the ID from the fragmentDoc in the edges-List of the fragment)
     */
    @Test
    public void testEdgesSetCorrectly(){
        Assert.assertEquals("The sequenceFlow1 has not been set correctly", 1263903479, fragment.getEdges().get(0).getId());
        Assert.assertEquals("The sequenceFlow2 has not been set correctly", 38665338, fragment.getEdges().get(1).getId());
        Assert.assertEquals("The association has not been set correctly", 2084573198, fragment.getEdges().get(2).getId());
    }
    /**
     *
     */
    @Test
    public void testSetsSetCorrectly(){
        Assert.assertEquals("The inputSet has not been set correctly", 0, fragment.getInputSets().size());
        Assert.assertEquals("The outputSet has not been set correctly", 1, fragment.getOutputSets().size());
        Assert.assertEquals("The producerNode of the outputSet has not been set correctly", 1080362683, fragment.getOutputSets().get(0).getProducer().getId());
    }
}
