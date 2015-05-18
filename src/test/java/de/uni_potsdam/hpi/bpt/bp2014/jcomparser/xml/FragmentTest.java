package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
/**
 * ********************************************************************************
 * <p/>
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 * <p/>
 * ******************************************************************
 * <p/>
 * Copyright © All Rights Reserved 2014 - 2015
 * <p/>
 * Please be aware of the License. You may found it in the root directory.
 * <p/>
 * **********************************************************************************
 */

/**
 * This class tests the functionality of the xml-Fragment-class.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Fragment.class})
public class FragmentTest {

    // We need the name of all methods which communicate to the server.
    /**
     * This Method fetches the version from the PE-Server.
     */
    private static final String FETCH_VERSION_METHOD
            = "fetchVersionXML";
    /**
     * the created fragment that needs to be tested
     */
    private Fragment fragment;

    /**
     * initialize the fragment from file "TestFragment.xml" in the resources-folder
     */
    @Before
    public void setupFragment() {
        try {
            fragment = PowerMock.createPartialMock(Fragment.class,
                    FETCH_VERSION_METHOD);
            Node fragmentNode = getDocumentFromXmlFile(new File("src/test/resources/TestFragment.xml"));
            PowerMock.expectPrivate(fragment, FETCH_VERSION_METHOD)
                    .andAnswer(new IAnswer<org.w3c.dom.Element>() {
                        @Override
                        public org.w3c.dom.Element answer() throws Throwable {
                            return getDocumentFromXmlFile(new File("src/test/resources/TestFragmentVersion.xml")).getDocumentElement();
                        }
                    });
            PowerMock.replay(fragment);
            fragment.initializeInstanceFromXML(fragmentNode);
            PowerMock.verify(fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * rudimentary test for checking if basic attributes like name and ID
     * have been set correctly.
     */
    @Test
    public void testSimpleFragmentDeserialization(){
        Assert.assertEquals("The fragmentName has not been set correctly", "TestFragment2", fragment.getFragmentName());
        Assert.assertEquals("The fragmentID has not been set correctly", 1763775575L, fragment.getFragmentID());
    }
    /**
     * rudimentary test for checking if the controlNodes are set "correctly" (simply check if there is a node with
     * the ID from the fragmentXML in the controlNodes-Map of the fragment)
     */
    @Test
    public void testControlNodesSetCorrectly(){
        Assert.assertNotNull("The startEvent has not been set correctly", fragment.getControlNodes().get(1453251009L));
        Assert.assertNotNull("The activity has not been set correctly", fragment.getControlNodes().get(1080362683L));
        Assert.assertNotNull("The dataNode has not been set correctly", fragment.getControlNodes().get(1397906076L));
        Assert.assertNotNull("The endEvent has not been set correctly", fragment.getControlNodes().get(735424916L));
    }
    /**
     * rudimentary test for checking if the edges are set "correctly" (simply check if there is an edge with
     * the ID from the fragmentXML in the edges-List of the fragment)
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
        Assert.assertEquals("The producerNode of the outputSet has not been set correctly", 1080362683L, fragment.getOutputSets().get(0).getNode().getId());
    }

    /**
     *
     */
    @Test
    public void testVersionSetCorrectly(){
        Assert.assertEquals("The version has not been set correctly", 1, fragment.getVersion());
    }

    /**
     * Casts a XML from its String representation to a w3c Document.
     *
     * @param xml The String representation of the XML.
     * @return The document created from String.
     */
    private Document getDocumentFromXmlFile(final File xml) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xml);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
