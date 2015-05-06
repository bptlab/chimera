package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import com.ibatis.common.jdbc.ScriptRunner;
import de.uni_potsdam.hpi.bpt.bp2014.database.*;
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

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * This class tests the Scenario.
 * This class uses mock objects to prevent the Scenario
 * from communicating with the ProcessEditor Server.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Scenario.class, Fragment.class, DomainModel.class})
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
    private static final String INSERT_TESTDATA_FILE = "src/test/resources/MigrationTest/jenginev2_Migration.sql";
    /**
     * File which contains insert-statements for the database which are needed for testing (the difference to
     * INSERT_TESTDATA_FILE is that the scenario which is already in the database is marked as deleted).
     */
    private static final String INSERT_TESTDATA2_FILE = "src/test/resources/MigrationTest/jenginev2_with_deleted_scenario.sql";

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
                "2049535559", scenarioWOVersion.getDomainModelURI());
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
        PowerMock.expectPrivate(scenarioWFragment, SET_DOMAIN_MODEL_METHOD).andReturn(null);
        PowerMock.expectPrivate(scenarioWFragment, FETCH_DOMAIN_MODEL_METHOD).andReturn(null);
        PowerMock.expectPrivate(scenarioWFragment, CHECK_VERSION_DATABASE).andVoid();
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
        PowerMock.expectPrivate(scenarioWTermination, SET_DOMAIN_MODEL_METHOD).andReturn(null);
        PowerMock.expectPrivate(scenarioWTermination, FETCH_DOMAIN_MODEL_METHOD).andReturn(null);
        PowerMock.expectPrivate(scenarioWTermination, CHECK_VERSION_DATABASE).andVoid();
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
        final Fragment fragment = initializeFragment("src/test/resources/Version.xml");
        fragment.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/bikeFragment.xml")));
        final DomainModel domainModel = initializeDomainModel("src/test/resources/Version.xml");
        domainModel.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Domain_Reise.xml")));
        final Scenario scenarioComplete = initializeCompleteScenario("src/test/resources/Version.xml", Arrays.asList(fragment), domainModel);
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
        final Fragment fragment1 = initializeFragment("src/test/resources/Version.xml");
        fragment1.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/Fragment1.xml")));
        final Fragment fragment2 = initializeFragment("src/test/resources/Version.xml");
        fragment2.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/Fragment2.xml")));
        final DomainModel domainModel = initializeDomainModel("src/test/resources/Version.xml");
        domainModel.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Domain_Reise.xml")));
        final Scenario scenario = initializeCompleteScenario("src/test/resources/Version.xml", Arrays.asList(fragment1, fragment2), domainModel);
        scenario.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/Testszenario.xml")));
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
        final Fragment fragment1 = initializeFragment("src/test/resources/Version.xml");
        fragment1.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/Fragment1.xml")));
        final Fragment fragment2 = initializeFragment("src/test/resources/Version.xml");
        fragment2.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/Fragment2.xml")));
        final DomainModel domainModel = initializeDomainModel("src/test/resources/Version.xml");
        domainModel.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Domain_Reise.xml")));
        final Scenario scenario = initializeCompleteScenario("src/test/resources/Version.xml", Arrays.asList(fragment1, fragment2), domainModel);
        scenario.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/Testszenario.xml")));
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
        final Fragment fragment1 = initializeFragment("src/test/resources/Version.xml");
        fragment1.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/Fragment1.xml")));
        final Fragment fragment2 = initializeFragment("src/test/resources/Version_modified.xml");
        fragment2.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/Fragment2.xml")));
        final DomainModel domainModel = initializeDomainModel("src/test/resources/Version.xml");
        domainModel.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Domain_Reise.xml")));
        final Scenario scenario = initializeCompleteScenario("src/test/resources/Version.xml", Arrays.asList(fragment1, fragment2), domainModel);
        scenario.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/Testszenario.xml")));
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
        final Fragment fragment1 = initializeFragment("src/test/resources/Version.xml");
        fragment1.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/Fragment1.xml")));
        final Fragment fragment2 = initializeFragment("src/test/resources/Version.xml");
        fragment2.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/Fragment2.xml")));
        final DomainModel domainModel = initializeDomainModel("src/test/resources/Version.xml");
        domainModel.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Domain_Reise.xml")));
        final Scenario scenario = initializeCompleteScenario("src/test/resources/Version_modified.xml", Arrays.asList(fragment1, fragment2), domainModel);
        scenario.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/Testszenario.xml")));
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
        final Fragment fragment1 = initializeFragment("src/test/resources/Version.xml");
        fragment1.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/Fragment1.xml")));
        final Fragment fragment2 = initializeFragment("src/test/resources/Version_modified.xml");
        fragment2.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/Fragment2.xml")));
        final Fragment neuesFragment = initializeFragment("src/test/resources/Version.xml");
        neuesFragment.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/NeuesFragment.xml")));
        final DomainModel domainModel = initializeDomainModel("src/test/resources/Version.xml");
        domainModel.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Domain_Reise.xml")));
        final Scenario scenario = initializeCompleteScenario("src/test/resources/Version_modified.xml", Arrays.asList(fragment1, fragment2, neuesFragment), domainModel);
        scenario.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/SzenarioNeuesFragment.xml")));
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
        final Fragment fragment1 = initializeFragment("src/test/resources/Version.xml");
        fragment1.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/Fragment1.xml")));
        final Fragment neuesFragment = initializeFragment("src/test/resources/Version.xml");
        neuesFragment.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/NeuesFragment.xml")));
        final DomainModel domainModel = initializeDomainModel("src/test/resources/Version.xml");
        domainModel.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Domain_Reise.xml")));
        final Scenario scenario = initializeCompleteScenario("src/test/resources/Version_modified.xml", Arrays.asList(fragment1, neuesFragment), domainModel);
        scenario.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/EntferntesFragmentSzenario.xml")));

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

        // initialize the new scenario consisting of the old fragments and one new fragment
        final Fragment fragment1 = initializeFragment("src/test/resources/Version.xml");
        fragment1.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/Fragment1.xml")));
        final Fragment fragment2 = initializeFragment("src/test/resources/Version.xml");
        fragment2.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/Fragment2.xml")));
        final Fragment neuesFragment = initializeFragment("src/test/resources/Version.xml");
        neuesFragment.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/NeuesFragment.xml")));
        final DomainModel domainModel = initializeDomainModel("src/test/resources/Version.xml");
        domainModel.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Domain_Reise.xml")));
        final Scenario scenario = initializeCompleteScenario("src/test/resources/Version_modified.xml", Arrays.asList(fragment1, fragment2, neuesFragment), domainModel);
        scenario.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/SzenarioNeuesFragment.xml")));
        scenario.save();

        Assert.assertEquals("Scenario not migrated properly", scenario.getDatabaseID(), dbScenarioInstance.getScenarioID(902));
        Assert.assertEquals("Scenario not migrated properly", scenario.getDatabaseID(), dbScenarioInstance.getScenarioID(903));

        Assert.assertEquals("Fragment not migrated properly", fragment1.getDatabaseID(), dbFragmentInstance.getFragmentID(4273));
        Assert.assertEquals("Fragment not migrated properly", fragment1.getDatabaseID(), dbFragmentInstance.getFragmentID(4275));
        Assert.assertEquals("Fragment not migrated properly", fragment2.getDatabaseID(), dbFragmentInstance.getFragmentID(4274));
        Assert.assertEquals("Fragment not migrated properly", fragment2.getDatabaseID(), dbFragmentInstance.getFragmentID(4276));

        Assert.assertEquals("ControlNodeInstance not migrated properly", fragment1.getControlNodes().get(214334952L).getDatabaseID(), dbControlNodeInstance.getControlNodeID(6304));
        Assert.assertEquals("ControlNodeInstance not migrated properly", fragment1.getControlNodes().get(214334952L).getDatabaseID(), dbControlNodeInstance.getControlNodeID(6306));

        Assert.assertEquals("ControlNodeInstance not migrated properly", fragment2.getControlNodes().get(426160629L).getDatabaseID(), dbControlNodeInstance.getControlNodeID(6305));
        Assert.assertEquals("ControlNodeInstance not migrated properly", fragment2.getControlNodes().get(426160629L).getDatabaseID(), dbControlNodeInstance.getControlNodeID(6307));

        Assert.assertEquals("DataObjectInstance not migrated properly", scenario.getDataObjects().get("bike").getDatabaseId(), dbDataObjectInstance.getDataObjectID(731));
        Assert.assertEquals("DataObjectInstance not migrated properly", scenario.getDataObjects().get("bike").getDatabaseId(), dbDataObjectInstance.getDataObjectID(733));
        Assert.assertEquals("DataObjectInstance not migrated properly", scenario.getDataObjects().get("Flug").getDatabaseId(), dbDataObjectInstance.getDataObjectID(730));
        Assert.assertEquals("DataObjectInstance not migrated properly", scenario.getDataObjects().get("Flug").getDatabaseId(), dbDataObjectInstance.getDataObjectID(732));
    }

    /**
     * Test that a scenario is newly saved both when the version of the datamodel has been changed or the datamodel is a new one.
     * @throws Exception java.lang.Exception
     */
    @Test
    public void testDomainModelVersionImpactOnSaving() throws Exception {
        refillDatabase(INSERT_TESTDATA_FILE);
        final Fragment fragment1 = initializeFragment("src/test/resources/Version.xml");
        fragment1.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/Fragment1.xml")));
        final Fragment fragment2 = initializeFragment("src/test/resources/Version.xml");
        fragment2.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/Fragment2.xml")));
        DomainModel domainModel = initializeDomainModel("src/test/resources/Version_modified.xml");
        domainModel.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Domain_Reise.xml")));
        Scenario scenario = initializeCompleteScenario("src/test/resources/Version.xml", Arrays.asList(fragment1, fragment2), domainModel);
        scenario.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/Testszenario.xml")));
        Assert.assertFalse("Even though the domainModel has been changed, the scenario isn't saved as a new one", scenario.save() == -1);
        assertTrue("Instances should not be migrated.", !scenario.isMigrationNecessary());

        domainModel = initializeDomainModel("src/test/resources/Version.xml");
        domainModel.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/Domain_Reise_modifiedID.xml")));
        scenario = initializeCompleteScenario("src/test/resources/Version.xml", Arrays.asList(fragment1, fragment2), domainModel);
        scenario.initializeInstanceFromXML(getDocumentFromXmlFile(
                new File("src/test/resources/MigrationTest/Testszenario.xml")));
        Assert.assertFalse("Even though there is a new domainModel the scenario isn't saved as a new one", scenario.save() == -1);
        assertTrue("Instances should not be migrated.", !scenario.isMigrationNecessary());

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
