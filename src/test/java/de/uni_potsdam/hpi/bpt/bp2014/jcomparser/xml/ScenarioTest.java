package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.w3c.dom.*;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

/**
 * This class tests the Scenario.
 * This class uses mock objects to prevent the Scenario
 * from communicating with the ProcessEditor Server.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Scenario.class)
public class ScenarioTest {
    // We need the name of all methods which communicate to the server.
    /**
     * During the initialization of a Scenario we create fragments.
     * Therefore we communicate with the PE-Server, to load the XMLs.
     */
    private static final String GENERATE_FRAGMENTS_METHOD
            = "generateFragmentList";
    /**
     * The version number of the Scenario is saved in an additional XML.
     * Normally we would fetch this XML from the PE-Server.
     */
    private static final String SET_VERSION_METHOD = "setVersionNumber";

    /**
     * This is the name of the DataObjects creation Method.
     * The method needs fragments in Order to initialize DataObjects.
     */
    private static final String CREATE_DO_METHOD = "createDataObjects";

    /**
     * This  Methods fetches The Version from the PE-Server.
     */
    private static final String FETCH_VERSION_METHOD = "fetchVersionXML";

    /**
     * Also we provide a simple scenario as XML.
     */
    private static final String SCENARIO1_XML =
            "<model xmlns=\"http://frapu.net/xsd/ProcessEditor\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" id=\"514683112\" name=\"bikeScenario\" type=\"net.frapu.code.visualization.pcm.PCMScenario\" xsi:schemaLocation=\"http://frapu.net/xsd/ProcessEditor http://frapu.net/xsd/ProcessEditor.xsd\">\n" +
            "<nodes>\n" +
            "<node>\n" +
            "<property name=\"shadow\" value=\"0\"/>\n" +
            "<property name=\"color_background\" value=\"-1\"/>\n" +
            "<property name=\"#nodes\" value=\"\"/>\n" +
            "<property name=\"collapsed\" value=\"0\"/>\n" +
            "<property name=\"x\" value=\"700\"/>\n" +
            "<property name=\"width\" value=\"200\"/>\n" +
            "<property name=\"y\" value=\"500\"/>\n" +
            "<property name=\"text\" value=\"\"/>\n" +
            "<property name=\"stereotype\" value=\"\"/>\n" +
            "<property name=\"#id\" value=\"1340431798\"/>\n" +
            "<property name=\"#type\" value=\"net.frapu.code.visualization.pcm.PCMFragmentCollection\"/>\n" +
            "<property name=\"height\" value=\"500\"/>\n" +
            "</node>\n" +
            "<node>\n" +
            "<property name=\"shadow\" value=\"0\"/>\n" +
            "<property name=\"color_background\" value=\"-1\"/>\n" +
            "<property name=\"#nodes\" value=\"\"/>\n" +
            "<property name=\"collapsed\" value=\"0\"/>\n" +
            "<property name=\"x\" value=\"500\"/>\n" +
            "<property name=\"width\" value=\"200\"/>\n" +
            "<property name=\"y\" value=\"500\"/>\n" +
            "<property name=\"text\" value=\"\"/>\n" +
            "<property name=\"stereotype\" value=\"\"/>\n" +
            "<property name=\"#id\" value=\"1256273478\"/>\n" +
            "<property name=\"#type\" value=\"net.frapu.code.visualization.pcm.PCMDataObjectCollection\"/>\n" +
            "<property name=\"height\" value=\"500\"/>\n" +
            "</node>\n" +
            "<node>\n" +
            "<property name=\"fragment mid\" value=\"1386518929\"/>\n" +
            "<property name=\"shadow\" value=\"0\"/>\n" +
            "<property name=\"color_background\" value=\"-1\"/>\n" +
            "<property name=\"x\" value=\"700\"/>\n" +
            "<property name=\"width\" value=\"100\"/>\n" +
            "<property name=\"y\" value=\"270\"/>\n" +
            "<property name=\"text\" value=\"assembleBike\"/>\n" +
            "<property name=\"stereotype\" value=\"\"/>\n" +
            "<property name=\"#id\" value=\"2060128231\"/>\n" +
            "<property name=\"#type\" value=\"net.frapu.code.visualization.pcm.PCMFragmentNode\"/>\n" +
            "<property name=\"height\" value=\"20\"/>\n" +
            "</node>\n" +
            "<node>\n" +
            "<property name=\"shadow\" value=\"0\"/>\n" +
            "<property name=\"color_background\" value=\"-1\"/>\n" +
            "<property name=\"Data class\" value=\"\"/>\n" +
            "<property name=\"x\" value=\"500\"/>\n" +
            "<property name=\"width\" value=\"100\"/>\n" +
            "<property name=\"y\" value=\"290\"/>\n" +
            "<property name=\"text\" value=\"bike\"/>\n" +
            "<property name=\"stereotype\" value=\"\"/>\n" +
            "<property name=\"#id\" value=\"970817444\"/>\n" +
            "<property name=\"#type\" value=\"net.frapu.code.visualization.pcm.PCMDataObjectNode\"/>\n" +
            "<property name=\"height\" value=\"20\"/>\n" +
            "</node>\n" +
            "</nodes>\n" +
            "<edges/>\n" +
            "<properties>\n" +
            "<property name=\"author\" value=\"\"/>\n" +
            "<property name=\"Termination State\" value=\"[]\"/>\n" +
            "<property name=\"#folder\" value=\"/\"/>\n" +
            "<property name=\"name\" value=\"bikeScenario\"/>\n" +
            "<property name=\"#uri\" value=\"/models/514683112/versions/0\"/>\n" +
            "<property name=\"comment\" value=\"\"/>\n" +
            "<property name=\"#creationDate\" value=\"18. Februar 2015 10:53:43 MEZ\"/>\n" +
            "<property name=\"Termination Data Object\" value=\"\"/>\n" +
            "</properties>\n" +
            "</model>";

    /**
     * The XML describing the different Versions of the Scenario.
     */
    private final static String VERSION_XML =
            "<versions>\n" +
            "<version id=\"0\">\n" +
            "<uri>\n" +
            "http://0:0:0:0:0:0:0:1:1205/models/514683112/versions/0\n" +
            "</uri>\n" +
            "<comment/>\n" +
            "<user>root</user>\n" +
            "<predecessors/>\n" +
            "<successors/>\n" +
            "</version>\n" +
            "</versions>";

    /**
     * This scenario will be used to be tested.
     * It will be initialized without a Version.
     */
    Scenario scenarioWOVersion;
    Scenario scenarioWVersion;

    /**
     * Before each Test, create an empty Scenario and mock necessary methods.
     */
    @Before
    public void setUp() {
        scenarioWOVersion = PowerMock.createPartialMock(Scenario.class,
                GENERATE_FRAGMENTS_METHOD,
                SET_VERSION_METHOD,
                CREATE_DO_METHOD);
        scenarioWVersion = PowerMock.createPartialMock(Scenario.class,
                GENERATE_FRAGMENTS_METHOD,
                CREATE_DO_METHOD,
                FETCH_VERSION_METHOD);
    }

    /**
     * This Test assert parsing without exceptions.
     * @throws Exception
     */
    @Test
    public void testInitializeFromXMLRunsWithoutException() throws Exception {
        Document bikeScenario = stringToDocument(SCENARIO1_XML);
        PowerMock.expectPrivate(scenarioWOVersion, GENERATE_FRAGMENTS_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWOVersion, SET_VERSION_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWOVersion, CREATE_DO_METHOD).andVoid();
        PowerMock.replay(scenarioWOVersion);
        scenarioWOVersion.initializeInstanceFromXML(bikeScenario.getDocumentElement());
        PowerMock.verify(scenarioWOVersion);
    }

    @Test
    public void testConstructorWithURL() {
        Scenario scenario = new Scenario("processeditor");
        Assert.assertEquals("The Server URL is not saved correctly",
                "processeditor", scenario.getProcesseditorServerUrl());
    }

    /**
     * This Test assert that MetaInformation about the Scenario are Set
     * correctly.
     * @throws Exception
     */
    @Test
    public void testMetaData() throws Exception {
        Document bikeScenario = stringToDocument(SCENARIO1_XML);
        PowerMock.expectPrivate(scenarioWOVersion, GENERATE_FRAGMENTS_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWOVersion, SET_VERSION_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWOVersion, CREATE_DO_METHOD).andVoid();
        PowerMock.replay(scenarioWOVersion);
        scenarioWOVersion.initializeInstanceFromXML(bikeScenario.getDocumentElement());
        Assert.assertEquals("The name of the scenario has not been set correctly",
                "bikeScenario",
                scenarioWOVersion.getScenarioName());
        Assert.assertEquals("The id of the scneario has not been set correctly",
                514683112L,
                scenarioWOVersion.getScenarioID());
        PowerMock.verify(scenarioWOVersion);
    }

    @Test
    public void testVersion() throws Exception {
        Document bikeScenario = stringToDocument(SCENARIO1_XML);
        PowerMock.expectPrivate(scenarioWVersion, GENERATE_FRAGMENTS_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWVersion, FETCH_VERSION_METHOD)
                .andAnswer(new IAnswer<Node>() {
                    @Override
                    public Node answer() throws Throwable {
                        return stringToDocument(VERSION_XML).getDocumentElement();
                    }
                });
        PowerMock.expectPrivate(scenarioWVersion, CREATE_DO_METHOD).andVoid();
        PowerMock.replay(scenarioWVersion);
        scenarioWVersion.initializeInstanceFromXML(bikeScenario);
        Assert.assertEquals("The version has not been set correctly",
                0, scenarioWVersion.getVersionNumber());
        PowerMock.verify(scenarioWVersion);
    }

    /**
     * Casts a XML from its String Representation to a w3c Document.
     * @param xml The String representation of the XML.
     * @return The from String created Document.
     */
    private Document stringToDocument(final String xml) {
        try {
            DocumentBuilder db = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(xml)));
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
