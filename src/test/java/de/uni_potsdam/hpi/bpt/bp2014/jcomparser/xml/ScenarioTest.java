package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
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
    private static final String SET_VERSION_METHOD
            = "setVersionNumber";

    /**
     * This is the name of the DataObjects creation Method.
     * The method needs fragments in Order to initialize DataObjects.
     */
    private static final String CREATE_DO_METHOD
            = "createDataObjects";

    /**
     * This scenario will be used to be tested.
     */
    Scenario scenario;

    /**
     * Before each Test, create an empty Scenario and mock necessary methods.
     */
    @Before
    public void setUp() {
        scenario = PowerMock.createPartialMock(Scenario.class,
                GENERATE_FRAGMENTS_METHOD,
                SET_VERSION_METHOD,
                CREATE_DO_METHOD);
    }

    /**
     * This Test assert parsing without exceptions.
     * @throws Exception
     */
    @Test
    public void testInitializeFromXMLRunsWithoutException() throws Exception {
        Document bikeScenario = getDocumentFromXmlFile(new File("src/test/resources/TestScenario.xml"));
        PowerMock.expectPrivate(scenario, GENERATE_FRAGMENTS_METHOD).andVoid();
        PowerMock.expectPrivate(scenario, SET_VERSION_METHOD).andVoid();
        PowerMock.expectPrivate(scenario, CREATE_DO_METHOD).andVoid();
        PowerMock.replay(scenario);
        scenario.initializeInstanceFromXML(bikeScenario.getDocumentElement());
        PowerMock.verify(scenario);
    }

    /**
     * This Test assert that MetaInformation about the Scenario are Set
     * correctly.
     * @throws Exception
     */
    @Test
    public void testMetaData() throws Exception {
        Document bikeScenario = getDocumentFromXmlFile(new File("src/test/resources/TestScenario.xml"));
        PowerMock.expectPrivate(scenario, GENERATE_FRAGMENTS_METHOD).andVoid();
        PowerMock.expectPrivate(scenario, SET_VERSION_METHOD).andVoid();
        PowerMock.expectPrivate(scenario, CREATE_DO_METHOD).andVoid();
        PowerMock.replay(scenario);
        scenario.initializeInstanceFromXML(bikeScenario.getDocumentElement());
        Assert.assertEquals("The name of the scenario has not been set correctly",
                "bikeScenario",
                scenario.getScenarioName());
        Assert.assertEquals("The id of the scenario has not been set correctly",
                514683112L,
                scenario.getScenarioID());
        PowerMock.verify(scenario);
    }

    /**
     * Casts a XML from its String Representation to a w3c Document.
     * @param xml The String representation of the XML.
     * @return The from String created Document.
     */
    private Document getDocumentFromXmlFile(final File xml) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document scenario = dBuilder.parse(xml);
            scenario.getDocumentElement().normalize();
            return scenario;
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
