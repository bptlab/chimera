package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import com.ibatis.common.jdbc.ScriptRunner;
import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import org.easymock.IAnswer;
import org.junit.AfterClass;
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
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * This class tests the Scenario.
 * This class uses mock objects to prevent the Scenario
 * from communicating with the ProcessEditor Server.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Scenario.class,Fragment.class})
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
     * This Method fetches The Version from the PE-Server.
     */
    private static final String FETCH_VERSION_METHOD = "fetchVersionXML";

    /**
     * This creates and initializes a fragment.
     */
    private static final String CREATE_FRAGMENT_METHOD
            = "createAndInitializeFragment";

    /**
     * This checks if a version of fragment or scenario already exists in the database.
     * Method can't be called without initialized fragments.
     */
    private static final String CHECK_VERSION_DATABASE
            = "checkIfVersionAlreadyInDatabase";

    /**
     * This scenario will be used to be tested.
     * It will be initialized without a Version.
     */
    Scenario scenarioWOVersion;
    /**
     * This scenario will be used for testing.
     * It will be initialized with a Version Number.
     * Extracted from the Version_XML.
     */
    Scenario scenarioWVersion;
    /**
     * This scenario will be used for testing
     * It will be initialized with a Fragment.
     */
    Scenario scenarioWFragment;
    /**
     * This scenario will be used for testing
     * It will be initialized with a terminationCondition.
     */
    Scenario scenarioWTermination;
    /**
     * This scenario is used for testing.
     * All elements are initialized, in order
     * to write them to the database.
     */
    Scenario scenarioComplete;

    private static final String DEVELOPMENT_SQL_SEED_FILE = "src/main/resources/JEngineV2.sql";
    private static final String TEST_SQL_SEED_FILE = "src/test/resources/emptyDatabase.sql";
    /**
     * Before each Test, create an empty Scenario and mock necessary methods.
     */
    @Before
    public void setUp() throws Exception {
        setUpDatabase();
        scenarioWOVersion = PowerMock.createPartialMock(Scenario.class,
                GENERATE_FRAGMENTS_METHOD,
                SET_VERSION_METHOD,
                CREATE_DO_METHOD,
                CHECK_VERSION_DATABASE);
        scenarioWVersion = PowerMock.createPartialMock(Scenario.class,
                GENERATE_FRAGMENTS_METHOD,
                CREATE_DO_METHOD,
                FETCH_VERSION_METHOD,
                CHECK_VERSION_DATABASE);
        scenarioWFragment = PowerMock.createPartialMock(Scenario.class,
                SET_VERSION_METHOD,
                CREATE_FRAGMENT_METHOD);
        scenarioComplete = PowerMock.createPartialMock(Scenario.class,
                FETCH_VERSION_METHOD,
                CREATE_FRAGMENT_METHOD);
        scenarioWTermination = PowerMock.createPartialMock(Scenario.class,
                FETCH_VERSION_METHOD,
                SET_VERSION_METHOD,
                CREATE_FRAGMENT_METHOD);
    }

    /**
     * This Test asserts parsing without exceptions.
     *
     * @throws Exception
     */
    @Test
    public void testInitializeFromXMLRunsWithoutException() throws Exception {
        Document bikeScenario = getDocumentFromXmlFile(new File("src/test/resources/BikeScenario.xml"));
        PowerMock.expectPrivate(scenarioWOVersion, GENERATE_FRAGMENTS_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWOVersion, SET_VERSION_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWOVersion, CREATE_DO_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWOVersion, CHECK_VERSION_DATABASE).andVoid();
        PowerMock.replay(scenarioWOVersion);
        scenarioWOVersion.initializeInstanceFromXML(bikeScenario.getDocumentElement());
        PowerMock.verify(scenarioWOVersion);
    }

    /**
     * Tests if the url is set correctly inside the constructor.
     */
    @Test
    public void testConstructorWithURL() {
        Scenario scenario = new Scenario("processeditor");
        Assert.assertEquals("The Server URL is not saved correctly",
                "processeditor", scenario.getProcesseditorServerUrl());
    }

    /**
     * This Test asserts that MetaInformation about the Scenario are Set
     * correctly.
     *
     * @throws Exception
     */
    @Test
    public void testMetaData() throws Exception {
        Document bikeScenario = getDocumentFromXmlFile(new File("src/test/resources/BikeScenario.xml"));
        PowerMock.expectPrivate(scenarioWOVersion, GENERATE_FRAGMENTS_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWOVersion, SET_VERSION_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWOVersion, CREATE_DO_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWOVersion, CHECK_VERSION_DATABASE).andVoid();
        PowerMock.replay(scenarioWOVersion);
        scenarioWOVersion.initializeInstanceFromXML(bikeScenario.getDocumentElement());
        Assert.assertEquals("The name of the scenario has not been set correctly",
                "bikeScenario",
                scenarioWOVersion.getScenarioName());
        Assert.assertEquals("The id of the scenario has not been set correctly",
                514683112L,
                scenarioWOVersion.getScenarioID());
        PowerMock.verify(scenarioWOVersion);
    }

    /**
     * This Methods tests whether the version is set correctly or not.
     *
     * @throws Exception occurs while creating the MockObject.
     */
    @Test
    public void testVersion() throws Exception {
        Document bikeScenario = getDocumentFromXmlFile(new File("src/test/resources/BikeScenario.xml"));
        PowerMock.expectPrivate(scenarioWVersion, GENERATE_FRAGMENTS_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWVersion, CHECK_VERSION_DATABASE).andVoid();
        PowerMock.expectPrivate(scenarioWVersion, FETCH_VERSION_METHOD)
                .andAnswer(new IAnswer<Node>() {
                    @Override
                    public Node answer() throws Throwable {
                        return getDocumentFromXmlFile(new File("src/test/resources/Version.xml")).getDocumentElement();
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
     * This Methods Tests if DataObjects are created correctly.
     *
     * @throws Exception occurs if creation of MockObject failed.
     */
    @Test
    public void testDataObjectCreation() throws Exception {
        Document bikeScenario = getDocumentFromXmlFile(new File("src/test/resources/BikeScenario.xml"));
        PowerMock.expectPrivate(scenarioWFragment, SET_VERSION_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWFragment, CREATE_FRAGMENT_METHOD, "1386518929")
                .andAnswer(new IAnswer<Fragment>() {
                    @Override
                    public Fragment answer() throws Throwable {
                        Fragment fragment = PowerMock.createPartialMock(Fragment.class,
                                FETCH_VERSION_METHOD);
                        PowerMock.expectPrivate(fragment, FETCH_VERSION_METHOD)
                                .andAnswer(new IAnswer<org.w3c.dom.Element>() {
                                    @Override
                                    public org.w3c.dom.Element answer() throws Throwable {
                                        return getDocumentFromXmlFile(new File("src/test/resources/Version.xml")).getDocumentElement();
                                    }
                                });
                        PowerMock.replay(fragment);
                        fragment.initializeInstanceFromXML(getDocumentFromXmlFile(new File("src/test/resources/bikeFragment.xml")));
                        PowerMock.verify(fragment);
                        return fragment;
                    }
                });
        PowerMock.replay(scenarioWFragment);
        scenarioWFragment.initializeInstanceFromXML(bikeScenario);
        assertNotNull("Map of dataobjects has not been initialized",
                scenarioWFragment.getDataObjects());
        Assert.assertEquals("A Wrong number of data Objects has been initialized",
                1, scenarioWFragment.getDataObjects().size());
        assertNotNull("Data Object has wrong key",
                scenarioWFragment.getDataObjects().get("bike"));
        PowerMock.verify(scenarioWFragment);
    }


    /**
     * This Methods Tests if the TerminationCondition is set correctly.
     *
     * @throws Exception occurs if creation of MockObject failed.
     */
    @Test
    public void testTerminationCondition() throws Exception {
        Document bikeScenario = getDocumentFromXmlFile(new File("src/test/resources/BikeScenarioWTermination.xml"));
        PowerMock.expectPrivate(scenarioWTermination, SET_VERSION_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWTermination, CREATE_FRAGMENT_METHOD, "1386518929")
                .andAnswer(new IAnswer<Fragment>() {
                    @Override
                    public Fragment answer() throws Throwable {
                        Fragment fragment = PowerMock.createPartialMock(Fragment.class,
                                FETCH_VERSION_METHOD);
                        PowerMock.expectPrivate(fragment, FETCH_VERSION_METHOD)
                                .andAnswer(new IAnswer<org.w3c.dom.Element>() {
                                    @Override
                                    public org.w3c.dom.Element answer() throws Throwable {
                                        return getDocumentFromXmlFile(new File("src/test/resources/Version.xml")).getDocumentElement();
                                    }
                                });
                        PowerMock.replay(fragment);
                        fragment.initializeInstanceFromXML(
                                getDocumentFromXmlFile(new File("src/test/resources/bikeFragment.xml")));
                        return fragment;
                    }
                });
        PowerMock.replay(scenarioWTermination);
        scenarioWTermination.initializeInstanceFromXML(bikeScenario);
        DataObject expectedDataObject = scenarioWTermination.getDataObjects().get("bike");
        de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.Node expectedDataNode = null;
        for (de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.Node currentNode : expectedDataObject.getDataNodes()) {
            if (currentNode.getState().equals("assembled")) {
                expectedDataNode = currentNode;
                break;
            }
        }
        Assert.assertEquals(expectedDataObject, scenarioWTermination.getTerminatingDataObject());
        Assert.assertEquals(expectedDataNode, scenarioWTermination.getTerminatingDataNode());
        PowerMock.verify(scenarioWTermination);
    }

    /**
     * Test if the scenario is created and initialized correctly.
     */
    @Test
    public void testSaveCompleteScenario() throws Exception {
        final Fragment fragment = PowerMock.createPartialMock(Fragment.class,
                FETCH_VERSION_METHOD);
        PowerMock.expectPrivate(fragment, FETCH_VERSION_METHOD)
                .andAnswer(new IAnswer<Node>() {
                    @Override
                    public Node answer() throws Throwable {
                        return getDocumentFromXmlFile(
                                new File("src/test/resources/Version.xml"))
                                .getDocumentElement();
                    }
                });
        PowerMock.replay(fragment);
        fragment.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/bikeFragment.xml")));
        Document bikeScenario = getDocumentFromXmlFile(
                new File("src/test/resources/BikeScenario.xml"));
        PowerMock.expectPrivate(scenarioComplete, FETCH_VERSION_METHOD)
                .andAnswer(new IAnswer<Node>() {
                    @Override
                    public Node answer() throws Throwable {
                        return getDocumentFromXmlFile(
                                new File("src/test/resources/Version.xml"))
                                .getDocumentElement();
                    }
                });
        PowerMock.expectPrivate(scenarioComplete, CREATE_FRAGMENT_METHOD, "1386518929")
                .andAnswer(new IAnswer<Fragment>() {
                    @Override
                    public Fragment answer() throws Throwable {
                        return fragment;
                    }
                });
        PowerMock.replay(scenarioComplete, fragment, Fragment.class);
        scenarioComplete.initializeInstanceFromXML(bikeScenario);
        scenarioComplete.save();
        assertTrue(scenarioComplete.getDatabaseID() > 0);
        PowerMock.verify(scenarioComplete, fragment);
    }

    /**
     * Casts a XML from its String Representation to a w3c Document.
     *
     * @param xml The String representation of the XML.
     * @return The from String created Document.
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

    /**
     * Refill database from DEVELOPMENT_SQL_SEED_FILE after clearing it.
     */
    @AfterClass
    public static void resetDatabase() throws IOException, SQLException {
        clearDatabase();
        ScriptRunner runner = new ScriptRunner(Connection.getInstance().connect(), false, false);
        runner.runScript(new FileReader(DEVELOPMENT_SQL_SEED_FILE));
    }

    /**
     * Sets up the database for ScenarioTests.
     *
     * @throws IOException  An Error while reading the SQL-File occurred.
     * @throws SQLException An Error while executing the SQL-Script occurred.
     */
    @Before
    public void setUpDatabase() throws IOException, SQLException {
        clearDatabase();
        ScriptRunner runner = new ScriptRunner(Connection.getInstance().connect(), false, false);
        runner.runScript(new FileReader(TEST_SQL_SEED_FILE));
    }

    /**
     * Drops and recreates the database.
     */
    protected static void clearDatabase() {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        if (conn == null) {
            return;
        }
        try {
            //Execute a querystmt = conn.createStatement();
            stmt = conn.createStatement();
            stmt.execute("DROP DATABASE JEngineV2");
            stmt.execute("CREATE DATABASE JEngineV2");
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
