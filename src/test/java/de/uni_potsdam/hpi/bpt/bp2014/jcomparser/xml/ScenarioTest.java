package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

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

import com.ibatis.common.jdbc.ScriptRunner;

import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataObjectInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbFragmentInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ExecutionService;


/**
 * This class tests the Scenario.
 * This class uses mock objects to prevent the Scenario
 * from communicating with the ProcessEditor Server.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Scenario.class, DatabaseFragment.class, DomainModel.class})
public class  ScenarioTest extends TestSetUp {
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
     * This checks if a version of fragment or scenario already exists in the database.
     * Method can't be called without initialized fragments.
     */
    private static final String CHECK_VERSION_DATABASE
            = "checkIfVersionAlreadyInDatabase";

    /**
     * This scenario will be used to be tested.
     * It will be initialized without a Version.
     */
    private Scenario scenarioWOVersion;
    /**
     * This scenario will be used for testing.
     * It will be initialized with a Version Number.
     * Extracted from the Version_XML.
     */
    private Scenario scenarioWVersion;
    /**
     * This scenario will be used for testing
     * It will be initialized with a Fragment.
     */
    private Scenario scenarioWFragment;
    /**
     * This scenario will be used for testing
     * It will be initialized with a terminationCondition.
     */
    private Scenario scenarioWTermination;
    /**
     * File which contains insert-statements for the database which are needed for testing.
     */
    private static final String INSERT_TESTDATA_FILE = "src/test/resources/jenginev2_Migration.sql";
    /**
     * File which contains insert-statements for the database which are needed for testing (the difference to
     * INSERT_TESTDATA_FILE is that the scenario which is already in the database is marked as deleted).
     */
    private static final String INSERT_TESTDATA2_FILE = "src/test/resources/jenginev2_with_deleted_scenario.sql";

    /**
     * Before each Test, create an empty Scenario and mock necessary methods.
     * @throws Exception java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        scenarioWOVersion = PowerMock.createPartialMock(Scenario.class,
                GENERATE_FRAGMENTS_METHOD,
                SET_VERSION_METHOD,
                CREATE_DO_METHOD,
                CHECK_VERSION_DATABASE,
                SET_DOMAIN_MODEL_METHOD,
                FETCH_DOMAIN_MODEL_METHOD);
        scenarioWVersion = PowerMock.createPartialMock(Scenario.class,
                GENERATE_FRAGMENTS_METHOD,
                CREATE_DO_METHOD,
                FETCH_VERSION_METHOD,
                CHECK_VERSION_DATABASE,
                SET_DOMAIN_MODEL_METHOD,
                FETCH_DOMAIN_MODEL_METHOD);
        scenarioWFragment = PowerMock.createPartialMock(Scenario.class,
                SET_VERSION_METHOD,
                CREATE_FRAGMENT_METHOD,
                SET_DOMAIN_MODEL_METHOD,
                FETCH_DOMAIN_MODEL_METHOD,
                CHECK_VERSION_DATABASE);
        scenarioWTermination = PowerMock.createPartialMock(Scenario.class,
                FETCH_VERSION_METHOD,
                SET_VERSION_METHOD,
                CREATE_FRAGMENT_METHOD,
                SET_DOMAIN_MODEL_METHOD,
                FETCH_DOMAIN_MODEL_METHOD,
                CHECK_VERSION_DATABASE);
    }

    /**
     * This Test asserts parsing without exceptions.
     * @throws Exception java.lang.Exception
     */
    @Test
    public void testInitializeFromXMLRunsWithoutException() throws Exception {
        Document bikeScenario = getDocumentFromXmlFile(new File("src/test/resources/BikeScenario.xml"));
        PowerMock.expectPrivate(scenarioWOVersion, GENERATE_FRAGMENTS_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWOVersion, SET_VERSION_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWOVersion, CREATE_DO_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWOVersion, CHECK_VERSION_DATABASE).andVoid();
        PowerMock.expectPrivate(scenarioWOVersion, SET_DOMAIN_MODEL_METHOD).andReturn(null);
        PowerMock.expectPrivate(scenarioWOVersion, FETCH_DOMAIN_MODEL_METHOD).andReturn(null);
        PowerMock.replay(scenarioWOVersion);
        scenarioWOVersion.initializeInstanceFromXML(bikeScenario.getDocumentElement());
        PowerMock.verify(scenarioWOVersion);
    }

    /**
     * This method tests if the given URL of an XML is correctly set.
     *
     * @throws Exception java.lang.Exception
     */
    @Test
    public void testSetDomainURL() throws Exception {
        Document bikeScenario = getDocumentFromXmlFile(new File("src/test/resources/BikeScenario.xml"));
        PowerMock.expectPrivate(scenarioWOVersion, GENERATE_FRAGMENTS_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWOVersion, SET_VERSION_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWOVersion, CREATE_DO_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWOVersion, CHECK_VERSION_DATABASE).andVoid();
        PowerMock.expectPrivate(scenarioWOVersion, SET_DOMAIN_MODEL_METHOD).andReturn(null);
        PowerMock.expectPrivate(scenarioWOVersion, FETCH_DOMAIN_MODEL_METHOD).andReturn(null);
        PowerMock.replay(scenarioWOVersion);
        scenarioWOVersion.initializeInstanceFromXML(bikeScenario.getDocumentElement());
        Assert.assertEquals("The URI has not been set correctly",
                "2049535559", scenarioWOVersion.getDomainModelID());
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
     * @throws Exception java.lang.Exception
     */
    @Test
    public void testMetaData() throws Exception {
        Document bikeScenario = getDocumentFromXmlFile(new File("src/test/resources/BikeScenario.xml"));
        PowerMock.expectPrivate(scenarioWOVersion, GENERATE_FRAGMENTS_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWOVersion, SET_VERSION_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWOVersion, CREATE_DO_METHOD).andVoid();
        PowerMock.expectPrivate(scenarioWOVersion, CHECK_VERSION_DATABASE).andVoid();
        PowerMock.expectPrivate(scenarioWOVersion, SET_DOMAIN_MODEL_METHOD).andReturn(null);
        PowerMock.expectPrivate(scenarioWOVersion, FETCH_DOMAIN_MODEL_METHOD).andReturn(null);
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
        PowerMock.expectPrivate(scenarioWVersion, SET_DOMAIN_MODEL_METHOD).andReturn(null);
        PowerMock.expectPrivate(scenarioWVersion, FETCH_DOMAIN_MODEL_METHOD).andReturn(null);
        PowerMock.expectPrivate(scenarioWVersion, FETCH_VERSION_METHOD)
                .andAnswer(new IAnswer<Node>() {
                    @Override
                    public Node answer() throws Throwable {
                        return getDocumentFromXmlFile(new File("src/test/resources/Version0.xml")).getDocumentElement();
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
        PowerMock.expectPrivate(scenarioWFragment, SET_DOMAIN_MODEL_METHOD).andReturn(null);
        PowerMock.expectPrivate(scenarioWFragment, FETCH_DOMAIN_MODEL_METHOD).andReturn(null);
        PowerMock.expectPrivate(scenarioWFragment, CHECK_VERSION_DATABASE).andVoid();
        PowerMock.expectPrivate(scenarioWFragment, CREATE_FRAGMENT_METHOD, "1386518929")
                .andAnswer(new IAnswer<DatabaseFragment>() {
                    @Override
                    public DatabaseFragment answer() throws Throwable {
                        DatabaseFragment fragment = PowerMock.createPartialMock(DatabaseFragment.class,
                                FETCH_VERSION_METHOD);
                        PowerMock.expectPrivate(fragment, FETCH_VERSION_METHOD)
                                .andAnswer(new IAnswer<org.w3c.dom.Element>() {
                                    @Override
                                    public org.w3c.dom.Element answer() throws Throwable {
                                        return getDocumentFromXmlFile(new File("src/test/resources/Version0.xml")).getDocumentElement();
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
        PowerMock.expectPrivate(scenarioWTermination, SET_DOMAIN_MODEL_METHOD).andReturn(null);
        PowerMock.expectPrivate(scenarioWTermination, FETCH_DOMAIN_MODEL_METHOD).andReturn(null);
        PowerMock.expectPrivate(scenarioWTermination, CHECK_VERSION_DATABASE).andVoid();
        PowerMock.expectPrivate(scenarioWTermination, CREATE_FRAGMENT_METHOD, "1386518929")
                .andAnswer(new IAnswer<DatabaseFragment>() {
                    @Override
                    public DatabaseFragment answer() throws Throwable {
                        DatabaseFragment fragment = PowerMock.createPartialMock(DatabaseFragment.class,
                                FETCH_VERSION_METHOD);
                        PowerMock.expectPrivate(fragment, FETCH_VERSION_METHOD)
                                .andAnswer(new IAnswer<org.w3c.dom.Element>() {
                                    @Override
                                    public org.w3c.dom.Element answer() throws Throwable {
                                        return getDocumentFromXmlFile(new File("src/test/resources/Version0.xml")).getDocumentElement();
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
         Assert.assertEquals("Termination Condition not set correctly", 1, scenarioWTermination.getTerminationCondition().size());
        Assert.assertEquals("Termination Condition not set correctly", 1, scenarioWTermination.getTerminationCondition().get(0).size());
        Assert.assertEquals("Termination Condition not set correctly", "assembled", scenarioWTermination.getTerminationCondition().get(0).get(expectedDataObject));
        PowerMock.verify(scenarioWTermination);
    }

    /**
     * Test if the scenario is created and initialized correctly.
     * @throws Exception java.lang.Exception
     */
    @Test
    public void testSaveCompleteScenario() throws Exception {
        ScriptRunner runner = new ScriptRunner(Connection.getInstance().connect(), false, false);
        runner.runScript(new FileReader(TRUNCATE_TABLES_FILE));
        final DatabaseFragment fragment = initializeFragment("src/test/resources/Version0.xml");
        fragment.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/bikeFragment.xml")));
        final DomainModel domainModel = initializeDomainModel("src/test/resources/Version0.xml");
        domainModel.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Domain_Reise.xml")));
        final Scenario scenarioComplete = initializeCompleteScenario("src/test/resources/Version0.xml", Arrays.asList(fragment), domainModel);
        scenarioComplete.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/BikeScenario.xml")));
        scenarioComplete.save();
        assertTrue(scenarioComplete.getDatabaseID() > 0);
    }

    /**
     * Assure that a unchanged scenario is not written to the database once again.
     * @throws Exception java.lang.Exception
     */
    @Test
    public void testSameScenarioNotSavedTwice() throws Exception {
        refillDatabase(INSERT_TESTDATA_FILE);
        final DatabaseFragment fragmentA = initializeFragment("src/test/resources/Version0.xml");
        fragmentA.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/FragmentA.xml")));
        final DatabaseFragment fragmentB = initializeFragment("src/test/resources/Version0.xml");
        fragmentB.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/FragmentB.xml")));
        final DomainModel domainModel = initializeDomainModel("src/test/resources/Version0.xml");
        domainModel.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/Domainmodel.xml")));
        final Scenario scenario = initializeCompleteScenario("src/test/resources/Version1.xml", Arrays.asList(fragmentA, fragmentB), domainModel);
        scenario.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/Scenario.xml")));
        assertTrue("Even though the scenario is the same, it has been saved again", scenario.save() == -1);
        assertTrue("Instances should not be migrated.", !scenario.isMigrationNecessary());
    }

    /**
     * Assure that a unchanged scenario is written to the database once again, if the old scenario in the database which
     * seems to be the same has been deleted.
     * @throws Exception java.lang.Exception
     */
    @Test
    public void testSameScenarioNotDeletedSavedAgain() throws Exception {
        refillDatabase(INSERT_TESTDATA2_FILE);
        final DatabaseFragment fragmentA = initializeFragment("src/test/resources/Version0.xml");
        fragmentA.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/FragmentA.xml")));
        final DatabaseFragment fragmentB = initializeFragment("src/test/resources/Version0.xml");
        fragmentB.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/FragmentB.xml")));
        final DomainModel domainModel = initializeDomainModel("src/test/resources/Version0.xml");
        domainModel.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/Domainmodel.xml")));
        final Scenario scenario = initializeCompleteScenario("src/test/resources/Version1.xml", Arrays.asList(fragmentA, fragmentB), domainModel);
        scenario.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/Scenario.xml")));
        assertTrue("Oh, scenario should have been saved!", scenario.save() > 0);
        assertTrue("Instances should not be migrated.", !scenario.isMigrationNecessary());
    }

    /**
     * Assure that when a fragment of a scenario has been modified and the older version of the scenario
     * is already in the database, it gets saved as a new scenario.
     * @throws Exception java.lang.Exception
     */
    @Test
    public void testModificationInFragmentScenarioNewlySaved() throws Exception {
        refillDatabase(INSERT_TESTDATA_FILE);
        final DatabaseFragment fragmentA = initializeFragment("src/test/resources/Version0.xml");
        fragmentA.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/FragmentA.xml")));
        final DatabaseFragment fragmentB = initializeFragment("src/test/resources/Version1.xml");
        fragmentB.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/FragmentB.xml")));
        final DomainModel domainModel = initializeDomainModel("src/test/resources/Version0.xml");
        domainModel.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/Domainmodel.xml")));
        final Scenario scenario = initializeCompleteScenario("src/test/resources/Version1.xml", Arrays.asList(fragmentA, fragmentB), domainModel);
        scenario.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/Scenario.xml")));
        assertTrue("Scenario with modified fragment is not saved.", scenario.save() > 0);
        assertTrue("Instances should not be migrated.", !scenario.isMigrationNecessary());
    }

    /**
     * Assure that when a scenario itself has been modified and the older version of the scenario
     * is already in the database, it gets saved as a new scenario.
     */
    @Test
    public void testModificationInScenarioScenarioNewlySaved() throws Exception {
        refillDatabase(INSERT_TESTDATA_FILE);
        final DatabaseFragment fragmentA = initializeFragment("src/test/resources/Version0.xml");
        fragmentA.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/FragmentA.xml")));
        final DatabaseFragment fragmentB = initializeFragment("src/test/resources/Version0.xml");
        fragmentB.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/FragmentB.xml")));
        final DomainModel domainModel = initializeDomainModel("src/test/resources/Version0.xml");
        domainModel.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/Domainmodel.xml")));
        final Scenario scenario = initializeCompleteScenario("src/test/resources/Version2.xml", Arrays.asList(fragmentA, fragmentB), domainModel);
        scenario.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/Scenario.xml")));
        assertTrue("Modified scenario is not saved.", scenario.save() > 0);
        assertTrue("Instances should not be migrated.", !scenario.isMigrationNecessary());
    }

    /**
     * Assure that when a fragment of a scenario has been modified and the older version of the scenario
     * is already in the database, it gets saved as a new scenario, even when fragments were added (no migration!).
     * @throws Exception java.lang.Exception
     */
    @Test
    public void testNoMigrationWhenFragmentAddedAndModificationsExist() throws Exception {
        refillDatabase(INSERT_TESTDATA_FILE);
        final DatabaseFragment fragmentA = initializeFragment("src/test/resources/Version0.xml");
        fragmentA.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/FragmentA.xml")));
        final DatabaseFragment fragmentB = initializeFragment("src/test/resources/Version1.xml");
        fragmentB.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/FragmentB.xml")));
        final DatabaseFragment neuesFragment = initializeFragment("src/test/resources/Version0.xml");
        neuesFragment.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/NeuesFragment.xml")));
        final DomainModel domainModel = initializeDomainModel("src/test/resources/Version0.xml");
        domainModel.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/Domainmodel.xml")));
        final Scenario scenario = initializeCompleteScenario("src/test/resources/Version2.xml", Arrays.asList(fragmentA, fragmentB, neuesFragment), domainModel);
        scenario.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/SzenarioNeuesFragment.xml")));
        assertTrue("Scenario with modified fragment is not saved", scenario.save() > 0);
        assertTrue("Migration should not be initiated", !scenario.isMigrationNecessary());
    }

    /**
     * Assure that when a fragment of a scenario has been deleted and the older version of the scenario
     * is already in the database, it gets saved as a new scenario, even when fragments were added (no migration!).
     * @throws Exception java.lang.Exception
     */
    @Test
    public void testNoMigrationWhenFragmentDeletedDespiteNewFragment() throws Exception {
        refillDatabase(INSERT_TESTDATA_FILE);
        final DatabaseFragment fragmentA = initializeFragment("src/test/resources/Version0.xml");
        fragmentA.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/FragmentA.xml")));
        final DatabaseFragment neuesFragment = initializeFragment("src/test/resources/Version0.xml");
        neuesFragment.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/NeuesFragment.xml")));
        final DomainModel domainModel = initializeDomainModel("src/test/resources/Version0.xml");
        domainModel.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/Domainmodel.xml")));
        final Scenario scenario = initializeCompleteScenario("src/test/resources/Version2.xml", Arrays.asList(fragmentA, neuesFragment), domainModel);
        scenario.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/EntferntesFragmentSzenario.xml")));

        assertTrue("Scenario with deleted fragment is not saved", scenario.save() > 0);
        assertTrue("Migration should not be initiated when fragment deleted", !scenario.isMigrationNecessary());
    }

    /**
     * Assure that migration is initiated when a scenario is written to the database and a new one is initialized
     * which is only changed in so far, that new fragments are addded.
     * @throws Exception java.lang.Exception
     */
    @Test
    public void testMigration() throws Exception {
        refillDatabase(INSERT_TESTDATA_FILE);

        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        DbFragmentInstance dbFragmentInstance = new DbFragmentInstance();
        DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
        DbDataObjectInstance dbDataObjectInstance = new DbDataObjectInstance();
        DbDataAttributeInstance dbDataAttributeInstance = new DbDataAttributeInstance();

        // initialize the new scenario consisting of the old fragments and one new fragment
        final DatabaseFragment fragmentA = initializeFragment("src/test/resources/Version0.xml");
        fragmentA.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/FragmentA.xml")));
        final DatabaseFragment fragmentB = initializeFragment("src/test/resources/Version0.xml");
        fragmentB.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/FragmentB.xml")));
        final DatabaseFragment neuesFragment = initializeFragment("src/test/resources/Version0.xml");
        neuesFragment.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/NeuesFragment.xml")));
        final DomainModel domainModel = initializeDomainModel("src/test/resources/Version0.xml");
        domainModel.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/Domainmodel.xml")));
        final Scenario scenario = initializeCompleteScenario("src/test/resources/Version2.xml", Arrays.asList(fragmentA, fragmentB, neuesFragment), domainModel);
        scenario.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/SzenarioNeuesFragment.xml")));
        scenario.save();

        Assert.assertEquals("Scenario not migrated properly", scenario.getDatabaseID(), dbScenarioInstance.getScenarioID(1));

        Assert.assertEquals("Fragment not migrated properly", fragmentA.getDatabaseID(), dbFragmentInstance.getFragmentID(1));
        Assert.assertEquals("Fragment not migrated properly", fragmentB.getDatabaseID(), dbFragmentInstance.getFragmentID(2));

        Assert.assertEquals("AbstractControlNodeInstance not migrated properly", fragmentA.getControlNodes().get(826790323L).getDatabaseID(), dbControlNodeInstance.getControlNodeID(2));
        Assert.assertEquals("AbstractControlNodeInstance not migrated properly", fragmentA.getControlNodes().get(517729148L).getDatabaseID(), dbControlNodeInstance.getControlNodeID(3));
        Assert.assertEquals("AbstractControlNodeInstance not migrated properly", fragmentA.getControlNodes().get(1569336784L).getDatabaseID(), dbControlNodeInstance.getControlNodeID(1));
        Assert.assertEquals("AbstractControlNodeInstance not migrated properly", fragmentA.getControlNodes().get(2081480666L).getDatabaseID(), dbControlNodeInstance.getControlNodeID(4));
        Assert.assertEquals("AbstractControlNodeInstance not migrated properly", fragmentB.getControlNodes().get(826790323L).getDatabaseID(), dbControlNodeInstance.getControlNodeID(5));

        Assert.assertEquals("DataObjectInstance not migrated properly", scenario.getDataObjects().get("SubDO").getDatabaseId(), dbDataObjectInstance.getDataObjectID(1));
        Assert.assertEquals("DataObjectInstance not migrated properly", scenario.getDataObjects().get("DO").getDatabaseId(), dbDataObjectInstance.getDataObjectID(2));
        Assert.assertEquals("DataObjectInstance not migrated properly", (int)scenario.getDataObjects().get("SubDO").getStates().get("init"), dbDataObjectInstance.getStateID(1));
        Assert.assertEquals("DataObjectInstance not migrated properly", (int)scenario.getDataObjects().get("DO").getStates().get("state1"), dbDataObjectInstance.getStateID(2));

        DataAttribute attr1 = null, attr2 = null, attr3 = null;
        for (DataAttribute attr : scenario.getDomainModel().getDataClasses().get(2068589604L).getDataAttributes()) {
            if (attr.getDataAttributeName().equals("Attr1"))
                attr1 = attr;
            else if (attr.getDataAttributeName().equals("Attr2"))
                attr2 = attr;
        }
        for (DataAttribute attr : scenario.getDomainModel().getDataClasses().get(395565279L).getDataAttributes()) {
            if (attr.getDataAttributeName().equals("Attr3"))
                attr3 = attr;
        }

        Assert.assertEquals("DataAttributeInstance not migrated properly", attr1.getDataAttributeID(), dbDataAttributeInstance.getDataAttributeID(2));
        Assert.assertEquals("DataAttributeInstance not migrated properly", attr2.getDataAttributeID(), dbDataAttributeInstance.getDataAttributeID(3));
        Assert.assertEquals("DataAttributeInstance not migrated properly", attr3.getDataAttributeID(), dbDataAttributeInstance.getDataAttributeID(1));
    }

    /**
     * Test that a scenario is newly saved both when the version of the datamodel has been changed or the datamodel is a new one.
     * @throws Exception java.lang.Exception
     */
    @Test
    public void testDomainModelVersionImpactOnSaving() throws Exception {
        refillDatabase(INSERT_TESTDATA_FILE);

        //Launch a scenario to test, whether a reload from the db is required after the scenario-version has changed
        ExecutionService ex = ExecutionService.getInstance(358512);
        ex.startNewScenarioInstance();
        Assert.assertEquals(false, ex.isNewVersionAvailable());

        final DatabaseFragment fragmentA = initializeFragment("src/test/resources/Version0.xml");
        fragmentA.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/FragmentA.xml")));
        final DatabaseFragment fragmentB = initializeFragment("src/test/resources/Version0.xml");
        fragmentB.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/FragmentB.xml")));
        DomainModel domainModel = initializeDomainModel("src/test/resources/Version1.xml");
        domainModel.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/Domainmodel.xml")));
        Scenario scenario = initializeCompleteScenario("src/test/resources/Version1.xml", Arrays.asList(fragmentA, fragmentB), domainModel);
        scenario.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Testscenario/Scenario.xml")));
        Assert.assertFalse("Even though the domainModel has been changed, the scenario isn't saved as a new one", scenario.save() == -1);
        assertTrue("Instances should not be migrated.", !scenario.isMigrationNecessary());

        Assert.assertEquals(true, ex.isNewVersionAvailable());
    }

    /**
     * Emptys the database and fills it from INSERT_TESTDATA_FILE afterwards.
     * @throws Exception java.lang.Exception
     * @param fileLocation Location of the file which is loaded into the database
     */
     private void refillDatabase(String fileLocation) throws Exception {
        emptyDatabase();
        ScriptRunner runner = new ScriptRunner(Connection.getInstance().connect(), false, false);
        runner.runScript(new FileReader(fileLocation));
    }
}
