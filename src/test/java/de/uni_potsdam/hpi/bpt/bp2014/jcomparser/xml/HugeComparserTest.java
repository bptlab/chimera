package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Scenario.class,Fragment.class, DomainModel.class})
public class HugeComparserTest extends TestSetUp {

    private static java.sql.Connection conn;
    private static Statement stmt = null;
    // DatabaseIDs of scenarioComponents which are needed furthermore for checking other databaseEntries
    private int scenario;
    private int fragA;
    private int fragB;
    private int dODataClassID;
    private int subDODataClassID;
    private int initDOID;
    private int state1DOID;
    private int initSubDOID;
    private int endSubDOID;
    private int dOID;
    private int subDOID;
    private int startEventFragAID;
    private int firstAndGwFragAID;
    private int refFragAID;
    private int a1FragAID;
    private int secondAndFragA;
    private int endEventFragA;
    private int startEventFragB;
    private int firstXORFragB;
    private int b2FragB;
    private int secondXORFragB;
    private int endEventFragB;
    private int refFragB;
    private int b1FragB;
    private int dOInitFragADataNodeID;
    private int dOState1FragADataNodeID;
    private int dOState1FragBDataNodeID;
    private int subDOInitDataNodeID;
    private int subDOEndDataNodeID;
    private int dOInitFragBDataNodeID;

    @Before
    public void setUp() throws Exception{
        emptyDatabase();
        conn = Connection.getInstance().connect();
        Fragment fragA = initializeFragment("src/test/resources/Version.xml");
        Fragment fragB = initializeFragment("src/test/resources/Version.xml");
        DomainModel domainModel = initializeDomainModel("src/test/resources/Version.xml");
        fragA.initializeInstanceFromXML(
                getDocumentFromXmlFile(new File("src/test/resources/HugeTest/FragmentA.xml")));
        fragB.initializeInstanceFromXML(
                getDocumentFromXmlFile(new File("src/test/resources/HugeTest/FragmentB.xml")));
        domainModel.initializeInstanceFromXML(
                getDocumentFromXmlFile(new File("src/test/resources/HugeTest/Domainmodel.xml")));
        Scenario scenario = initializeCompleteScenario("src/test/resources/Version_modified.xml",
                Arrays.asList(fragA, fragB),
                domainModel);
        scenario.initializeInstanceFromXML(
                getDocumentFromXmlFile(new File("src/test/resources/HugeTest/Scenario.xml")));
        scenario.save();
    }

    @Test
    public void test() throws Exception{
        testScenario();
        testFragments();
        testDataClass();
        //testDataAttributes();
        testAggregation();
        testStates();
        testDataObjects();
        testDataNodes();
        testControlNodes();
        testDataSetAndDataFlow();
        testControlFlow();
        testReferences();
    }
    //TODO: IDS NIRGENDS KLEINER ALS 0 SICHERSTELLEN (Szenario schon erledigt)
    private void testScenario() throws Exception {
        ResultSet scenarios = getDbEntries("scenario");
        scenarios.next();
        Assert.assertEquals("Scenario name not set correctly", "Scenario", scenarios.getString("name"));
        Assert.assertEquals("Scenario modelID not set correctly", 358512L, scenarios.getLong("modelid"));
        Assert.assertEquals("Scenario modelversion not set correctly", 1, scenarios.getInt("modelversion"));
        Assert.assertEquals("Scenario datamodelID not set correctly", 790983467L, scenarios.getLong("datamodelid"));
        Assert.assertEquals("Scenario datamodelVersion not set correctly", 0, scenarios.getLong("datamodelversion"));
        scenario = scenarios.getInt("id");
        Assert.assertTrue("ScenarioID smaller than 1", scenario > 0);
        Assert.assertFalse("Too many scenarios inserted", scenarios.next());
        scenarios.close();
    }

    private void testFragments() throws Exception {
        ResultSet fragments = getDbEntries("fragment");
        List<Long> fragmentModelIDs = new LinkedList<>();
        fragmentModelIDs.add(1084827857L);
        fragmentModelIDs.add(894096069L);
        int countFragments = 0;
        while (fragments.next()) {
            countFragments++;
            long modelID = fragments.getLong("modelid");
            if (fragmentModelIDs.contains(modelID)) {
                if (modelID == 1084827857L) {
                    Assert.assertEquals("Fragment name not set correctly", "FragmentA", fragments.getString("name"));
                    Assert.assertEquals("Fragment scenarioID not set correctly", scenario, fragments.getInt("scenario_id"));
                    Assert.assertEquals("Fragment modelVersion not set correctly", 0, fragments.getInt("modelversion"));
                    Assert.assertTrue("Fragment id smaller than 1", fragments.getInt("id") > 0);
                    fragA = fragments.getInt("id");
                } else if (modelID == 894096069L) {
                    Assert.assertEquals("Fragment name not set correctly", "FragmentB", fragments.getString("name"));
                    Assert.assertEquals("Fragment scenarioID not set correctly", scenario, fragments.getInt("scenario_id"));
                    Assert.assertEquals("Fragment modelVersion not set correctly", 0, fragments.getInt("modelversion"));
                    Assert.assertTrue("Fragment id smaller than 1",fragments.getInt("id") > 0);
                    fragB = fragments.getInt("id");
                }
                fragmentModelIDs.remove(modelID);
            }
            else {
                Assert.fail("A fragment with a wrong modelID has been inserted");
            }
        }
        Assert.assertEquals("Too many/less fragments inserted", 2, countFragments);
        fragments.close();
    }

    private void testDataClass() throws Exception {
        ResultSet dataClasses = getDbEntries("dataclass");
        List<String> dataClassNames = new LinkedList<>();
        dataClassNames.add("DO");
        dataClassNames.add("SubDO");
        int countDataClasses = 0;
        while (dataClasses.next()) {
            countDataClasses++;
            String dataClassName = dataClasses.getString("name");
            if (dataClassNames.contains(dataClassName)) {
                switch (dataClassName) {
                    case "DO":
                        dODataClassID = dataClasses.getInt("id");
                        Assert.assertTrue("DataClassID smaller than 1", dODataClassID > 0);
                        break;
                    case "SubDO":
                        subDODataClassID = dataClasses.getInt("id");
                        Assert.assertTrue("DataClassID smaller than 1", subDODataClassID > 0);
                        break;
                }
                dataClassNames.remove(dataClassName);
            }
            else {
                    Assert.fail("A wrong dataclass has been inserted");
            }
        }
        Assert.assertEquals("Too many/less dataClasses inserted", 2, countDataClasses);
        dataClasses.close();
    }

    private void testDataAttributes() throws Exception {
        ResultSet dataAttributes = getDbEntries("dataattribute");
        List<String> attrNames = new LinkedList<>();
        attrNames.add("Attr1");
        attrNames.add("Attr2");
        attrNames.add("Attr3");
        int countAttributes = 0;
        while (dataAttributes.next()) {
            String name = dataAttributes.getString("name");
            if (attrNames.contains(name)) {
                switch (name) {
                    case "Attr1":
                        Assert.assertEquals("DataAttribute dataclassID not set correctly", dODataClassID, dataAttributes.getInt("dataclass_id"));
                        Assert.assertTrue("DataattributeID smaller than 1", dataAttributes.getInt("id") > 0);
                        break;
                    case "Attr2":
                        Assert.assertEquals("DataAttribute dataclassID not set correctly", dODataClassID, dataAttributes.getInt("dataclass_id"));
                        Assert.assertTrue("DataattributeID smaller than 1", dataAttributes.getInt("id") > 0);
                        break;
                    case "Attr3":
                        Assert.assertEquals("DataAttribute dataclassID not set correctly", subDODataClassID, dataAttributes.getInt("dataclass_id"));
                        Assert.assertTrue("DataattributeID smaller than 1", dataAttributes.getInt("id") > 0);
                        break;
                }
            }
            else {
                Assert.fail("A wrong dataAttribute has been inserted");
            }
        }
        Assert.assertEquals("Too many/less dataAttributes inserted", 3, countAttributes);
        dataAttributes.close();
    }

    private void testAggregation() throws Exception {
        ResultSet aggregation = getDbEntries("aggregation");
        aggregation.next();
        Assert.assertEquals("DataClassID1 not set correctly", dODataClassID, aggregation.getInt("dataclass_id1"));
        Assert.assertEquals("DataClassID2 not set correctly", subDODataClassID, aggregation.getInt("dataclass_id2"));
        Assert.assertEquals("Multiplicity not set correctly", Integer.MAX_VALUE, aggregation.getInt("multiplicity"));
        Assert.assertFalse("Too many aggregations inserted", aggregation.next());
        aggregation.close();
    }

    private void testStates() throws Exception {
        ResultSet states = getDbEntries("state");
        Map<String, List<Integer>> expectedStates = new HashMap();
        List<Integer> expectedClassIDs = new ArrayList<>();
        expectedClassIDs.add(dODataClassID);
        expectedClassIDs.add(subDODataClassID);
        expectedStates.put("init", expectedClassIDs);
        expectedClassIDs = new ArrayList<>();
        expectedClassIDs.add(dODataClassID);
        expectedStates.put("state1", expectedClassIDs);
        expectedClassIDs = new ArrayList<>();
        expectedClassIDs.add(subDODataClassID);
        expectedStates.put("end", expectedClassIDs);
        int countStates = 0;
        while(states.next()) {
            countStates++;
            Assert.assertTrue("StateID smaller than 1", states.getInt("id") > 0);
            int classID = states.getInt("olc_id");
            switch(states.getString("name")) {
                case "init":
                    if (expectedStates.get("init").contains(classID)) {
                        expectedStates.get("init").remove(new Integer(classID));
                        //expectedStates.get("init").remove(classID);
                        if (classID == subDODataClassID)
                            initSubDOID = states.getInt("id");
                        else
                            initDOID = states.getInt("id");
                    }
                    else {
                        Assert.fail("Wrong olc_id in table 'state' has been inserted");
                    }
                    break;
                case "state1":
                    if (expectedStates.get("state1").contains(classID)) {
                        expectedStates.get("state1").remove(new Integer(classID));
                        //expectedStates.get("state1").remove(classID);
                        state1DOID = states.getInt("id");
                    }
                    else {
                        Assert.fail("Wrong olc_id in table 'state' has been inserted");
                    }
                    break;
                case "end":
                    if (expectedStates.get("end").contains(classID)) {
                        expectedStates.get("end").remove(new Integer(classID));
                        //expectedStates.get("init").remove(classID);
                        endSubDOID = states.getInt("id");
                    }
                    else {
                        Assert.fail("Wrong olc_id in table 'state' has been inserted");
                    }
                    break;
                default:
                    Assert.fail("Wrong name in table 'state' has been inserted");
            }
        }
        Assert.assertTrue("Too many/less states inserted", countStates == 4);
        states.close();
    }

    private void testDataObjects() throws Exception {
        ResultSet dataObjects = getDbEntries("dataobject");
        int countDataObjects = 0;
        while (dataObjects.next()) {
            Assert.assertTrue("DataObjectID smaller than 1", dataObjects.getInt("id") > 0);
            countDataObjects++;
            switch (dataObjects.getString("name")) {
                case "DO":
                    Assert.assertEquals("DataclassID in table 'dataObjects' not set correctly", dODataClassID, dataObjects.getInt("dataclass_id"));
                    Assert.assertEquals("ScenarioID in table 'dataObjects' not set correctly", scenario, dataObjects.getInt("scenario_id"));
                    Assert.assertEquals("StartStateID in table 'dataObjects' not set correctly", initDOID, dataObjects.getInt("start_state_id"));
                    dOID = dataObjects.getInt("id");
                    break;
                case "SubDO":
                    Assert.assertEquals("DataclassID in table 'dataObjects' not set correctly", subDODataClassID, dataObjects.getInt("dataclass_id"));
                    Assert.assertEquals("ScenarioID in table 'dataObjects' not set correctly", scenario, dataObjects.getInt("scenario_id"));
                    Assert.assertEquals("StartStateID in table 'dataObjects' not set correctly", initSubDOID, dataObjects.getInt("start_state_id"));
                    subDOID = dataObjects.getInt("id");
                    break;
                default:
            }
        }
        Assert.assertEquals("Too many/less dataObjects inserted", 2, countDataObjects);
        dataObjects.close();
    }

    private void testDataNodes() throws Exception {
        ResultSet dataNodes = getDbEntries("datanode");
        List<String> expectedModelIDs = new LinkedList<>();
        expectedModelIDs.add("135409402");
        expectedModelIDs.add("650069438");
        expectedModelIDs.add("611573211");
        expectedModelIDs.add("1368161079");
        expectedModelIDs.add("1517694277");
        expectedModelIDs.add("155099451");

        while (dataNodes.next()) {
            Assert.assertTrue("DataNodeID smaller than 1", dataNodes.getInt("id") > 0);
            if (expectedModelIDs.contains(Long.toString(dataNodes.getLong("modelid")))) {
                switch (Long.toString(dataNodes.getLong("modelid"))) {
                    case "135409402":
                        Assert.assertEquals("Wrong dataObjectID for dataNode inserted", dOID, dataNodes.getInt("dataobject_id"));
                        Assert.assertEquals("Wrong dataclassID for dataNode inserted", dODataClassID, dataNodes.getInt("dataclass_id"));
                        Assert.assertEquals("Wrong stateID for dataNode inserted", initDOID, dataNodes.getInt("state_id"));
                        Assert.assertEquals("Wrong scenarioID for dataNode inserted", scenario, dataNodes.getInt("scenario_id"));
                        expectedModelIDs.remove("135409402");
                        dOInitFragADataNodeID = dataNodes.getInt("id");
                        break;
                    case "650069438":
                        Assert.assertEquals("Wrong dataObjectID for dataNode inserted", dOID, dataNodes.getInt("dataobject_id"));
                        Assert.assertEquals("Wrong dataclassID for dataNode inserted", dODataClassID, dataNodes.getInt("dataclass_id"));
                        Assert.assertEquals("Wrong stateID for dataNode inserted", state1DOID, dataNodes.getInt("state_id"));
                        Assert.assertEquals("Wrong scenarioID for dataNode inserted", scenario, dataNodes.getInt("scenario_id"));
                        expectedModelIDs.remove("650069438");
                        dOState1FragADataNodeID = dataNodes.getInt("id");
                        break;
                    case "611573211":
                        Assert.assertEquals("Wrong dataObjectID for dataNode inserted", dOID, dataNodes.getInt("dataobject_id"));
                        Assert.assertEquals("Wrong dataclassID for dataNode inserted", dODataClassID, dataNodes.getInt("dataclass_id"));
                        Assert.assertEquals("Wrong stateID for dataNode inserted", state1DOID, dataNodes.getInt("state_id"));
                        Assert.assertEquals("Wrong scenarioID for dataNode inserted", scenario, dataNodes.getInt("scenario_id"));
                        expectedModelIDs.remove("611573211");
                        dOState1FragBDataNodeID = dataNodes.getInt("id");
                        break;
                    case "1368161079":
                        Assert.assertEquals("Wrong dataObjectID for dataNode inserted", subDOID, dataNodes.getInt("dataobject_id"));
                        Assert.assertEquals("Wrong dataclassID for dataNode inserted", subDODataClassID, dataNodes.getInt("dataclass_id"));
                        Assert.assertEquals("Wrong stateID for dataNode inserted", initSubDOID, dataNodes.getInt("state_id"));
                        Assert.assertEquals("Wrong scenarioID for dataNode inserted", scenario, dataNodes.getInt("scenario_id"));
                        expectedModelIDs.remove("1368161079");
                        subDOInitDataNodeID = dataNodes.getInt("id");
                        break;
                    case "1517694277":
                        Assert.assertEquals("Wrong dataObjectID for dataNode inserted", subDOID, dataNodes.getInt("dataobject_id"));
                        Assert.assertEquals("Wrong dataclassID for dataNode inserted", subDODataClassID, dataNodes.getInt("dataclass_id"));
                        Assert.assertEquals("Wrong stateID for dataNode inserted", endSubDOID, dataNodes.getInt("state_id"));
                        Assert.assertEquals("Wrong scenarioID for dataNode inserted", scenario, dataNodes.getInt("scenario_id"));
                        expectedModelIDs.remove("1517694277");
                        subDOEndDataNodeID = dataNodes.getInt("id");
                        break;
                    case "155099451":
                        Assert.assertEquals("Wrong dataObjectID for dataNode inserted", dOID, dataNodes.getInt("dataobject_id"));
                        Assert.assertEquals("Wrong dataclassID for dataNode inserted", dODataClassID, dataNodes.getInt("dataclass_id"));
                        Assert.assertEquals("Wrong stateID for dataNode inserted", initDOID, dataNodes.getInt("state_id"));
                        Assert.assertEquals("Wrong scenarioID for dataNode inserted", scenario, dataNodes.getInt("scenario_id"));
                        expectedModelIDs.remove("155099451");
                        dOInitFragBDataNodeID = dataNodes.getInt("id");
                        break;
                    default:
                        Assert.fail("Modelid in table 'datanode' is wrong or has been inserted multiple times");
                }
            }
        }
        dataNodes.close();
    }

    private void testControlNodes() throws Exception {
        ResultSet controlNodes = getDbEntries("controlnode");
        List<String> expectedModelIDs = new LinkedList<>();
        // FragmentA
        expectedModelIDs.add("1069345757");
        expectedModelIDs.add("1569336784");
        expectedModelIDs.add("826790323");
        expectedModelIDs.add("517729148");
        expectedModelIDs.add("2081480666");
        expectedModelIDs.add("1914825610");
        // FragmentB
        expectedModelIDs.add("509231813");
        expectedModelIDs.add("1689490932");
        expectedModelIDs.add("1821614206");
        expectedModelIDs.add("564114893");
        expectedModelIDs.add("2136115766");
        expectedModelIDs.add("826790327");
        expectedModelIDs.add("1407636184");
        while(controlNodes.next()) {
            Assert.assertTrue("ControlNOdeID smaller than 1", controlNodes.getInt("id") > 0);
            if (expectedModelIDs.contains(Long.toString(controlNodes.getLong("modelid")))) {
                switch (Long.toString(controlNodes.getLong("modelid"))) {
                    case "1069345757":
                        Assert.assertEquals("Wrong fragmentID for controlNode inserted", fragA, controlNodes.getInt("fragment_id"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "Startevent", controlNodes.getString("type"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "", controlNodes.getString("label"));
                        startEventFragAID = controlNodes.getInt("id");
                        expectedModelIDs.remove("1069345757");
                        break;
                    case "1569336784":
                        Assert.assertEquals("Wrong fragmentID for controlNode inserted", fragA, controlNodes.getInt("fragment_id"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "AND", controlNodes.getString("type"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "", controlNodes.getString("label"));
                        firstAndGwFragAID = controlNodes.getInt("id");
                        expectedModelIDs.remove("1569336784");
                        break;
                    case "826790323":
                        Assert.assertEquals("Wrong fragmentID for controlNode inserted", fragA, controlNodes.getInt("fragment_id"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "Activity", controlNodes.getString("type"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "Ref", controlNodes.getString("label"));
                        refFragAID = controlNodes.getInt("id");
                        expectedModelIDs.remove("826790323");
                        break;
                    case "517729148":
                        Assert.assertEquals("Wrong fragmentID for controlNode inserted", fragA, controlNodes.getInt("fragment_id"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "Activity", controlNodes.getString("type"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "A1", controlNodes.getString("label"));
                        a1FragAID = controlNodes.getInt("id");
                        expectedModelIDs.remove("517729148");
                        break;
                    case "2081480666":
                        Assert.assertEquals("Wrong fragmentID for controlNode inserted", fragA, controlNodes.getInt("fragment_id"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "AND", controlNodes.getString("type"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "", controlNodes.getString("label"));
                        secondAndFragA = controlNodes.getInt("id");
                        expectedModelIDs.remove("2081480666");
                        break;
                    case "1914825610":
                        Assert.assertEquals("Wrong fragmentID for controlNode inserted", fragA, controlNodes.getInt("fragment_id"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "Endevent", controlNodes.getString("type"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "", controlNodes.getString("label"));
                        endEventFragA = controlNodes.getInt("id");
                        expectedModelIDs.remove("1914825610");
                        break;
                    case "509231813":
                        Assert.assertEquals("Wrong fragmentID for controlNode inserted", fragB, controlNodes.getInt("fragment_id"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "Startevent", controlNodes.getString("type"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "", controlNodes.getString("label"));
                        startEventFragB = controlNodes.getInt("id");
                        expectedModelIDs.remove("509231813");
                        break;
                    case "1689490932":
                        Assert.assertEquals("Wrong fragmentID for controlNode inserted", fragB, controlNodes.getInt("fragment_id"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "XOR", controlNodes.getString("type"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "", controlNodes.getString("label"));
                        firstXORFragB = controlNodes.getInt("id");
                        expectedModelIDs.remove("1689490932");
                        break;
                    case "1821614206":
                        Assert.assertEquals("Wrong fragmentID for controlNode inserted", fragB, controlNodes.getInt("fragment_id"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "EmailTask", controlNodes.getString("type"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "B2", controlNodes.getString("label"));
                        b2FragB = controlNodes.getInt("id");
                        expectedModelIDs.remove("1821614206");
                        break;
                    case "564114893":
                        Assert.assertEquals("Wrong fragmentID for controlNode inserted", fragB, controlNodes.getInt("fragment_id"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "XOR", controlNodes.getString("type"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "", controlNodes.getString("label"));
                        secondXORFragB = controlNodes.getInt("id");
                        expectedModelIDs.remove("564114893");
                        break;
                    case "2136115766":
                        Assert.assertEquals("Wrong fragmentID for controlNode inserted", fragB, controlNodes.getInt("fragment_id"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "Endevent", controlNodes.getString("type"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "", controlNodes.getString("label"));
                        endEventFragB = controlNodes.getInt("id");
                        expectedModelIDs.remove("2136115766");
                        break;
                    case "826790327":
                        Assert.assertEquals("Wrong fragmentID for controlNode inserted", fragB, controlNodes.getInt("fragment_id"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "Activity", controlNodes.getString("type"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "Ref", controlNodes.getString("label"));
                        refFragB = controlNodes.getInt("id");
                        expectedModelIDs.remove("826790327");
                        break;
                    case "1407636184":
                        Assert.assertEquals("Wrong fragmentID for controlNode inserted", fragB, controlNodes.getInt("fragment_id"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "Activity", controlNodes.getString("type"));
                        Assert.assertEquals("Wrong type for controlNode inserted", "B1", controlNodes.getString("label"));
                        b1FragB = controlNodes.getInt("id");
                        expectedModelIDs.remove("1407636184");
                        break;
                    default:
                        Assert.fail("Modelid in table 'controlnode' is wrong or has been inserted multiple times");
                }
            }
        }
        controlNodes.close();
    }

    private void testDataSetAndDataFlow() throws Exception {
        ResultSet dataSets = getDataSetJoins();
        List<Integer> dataNodeIDs = new LinkedList<>();
        dataNodeIDs.add(dOInitFragADataNodeID);
        dataNodeIDs.add(dOState1FragADataNodeID);
        dataNodeIDs.add(dOState1FragBDataNodeID);
        dataNodeIDs.add(subDOInitDataNodeID);
        dataNodeIDs.add(subDOEndDataNodeID);
        dataNodeIDs.add(dOInitFragBDataNodeID);
        while (dataSets.next()){
            Assert.assertTrue("DataSetID smaller than 1", dataSets.getInt("dataset.id") > 0);
            int dataNodeID = dataSets.getInt("datasetconsistsofdatanode.datanode_id");
            if (dataNodeIDs.contains(dataNodeID)) {
                if (dataNodeID == dOInitFragADataNodeID) {
                    Assert.assertEquals("Input in table 'dataset' not set correctly", 1, dataSets.getShort("dataset.input"));
                    Assert.assertEquals("ControlNodeID in table 'dataflow' not set correctly", a1FragAID, dataSets.getInt("dataflow.controlnode_id"));
                    Assert.assertEquals("Input in table 'dataflow' not set correctly", 1, dataSets.getShort("dataflow.input"));
                    dataNodeIDs.remove(new Integer(dOInitFragADataNodeID));
                }
                else if (dataNodeID == dOState1FragADataNodeID) {
                    Assert.assertEquals("Input in table 'dataset' not set correctly", 0, dataSets.getShort("dataset.input"));
                    Assert.assertEquals("ControlNodeID in table 'dataflow' not set correctly", a1FragAID, dataSets.getInt("dataflow.controlnode_id"));
                    Assert.assertEquals("Input in table 'dataflow' not set correctly", 0, dataSets.getShort("dataflow.input"));
                    dataNodeIDs.remove(new Integer(dOState1FragADataNodeID));
                }
                else if (dataNodeID == dOState1FragBDataNodeID) {
                    Assert.assertEquals("Input in table 'dataset' not set correctly", 1, dataSets.getShort("dataset.input"));
                    Assert.assertEquals("ControlNodeID in table 'dataflow' not set correctly", refFragB, dataSets.getInt("dataflow.controlnode_id"));
                    Assert.assertEquals("Input in table 'dataflow' not set correctly", 1, dataSets.getShort("dataflow.input"));
                    dataNodeIDs.remove(new Integer(dOState1FragBDataNodeID));
                }
                else if (dataNodeID == subDOInitDataNodeID) {
                    Assert.assertEquals("Input in table 'dataset' not set correctly", 1, dataSets.getShort("dataset.input"));
                    Assert.assertEquals("ControlNodeID in table 'dataflow' not set correctly", b1FragB, dataSets.getInt("dataflow.controlnode_id"));
                    Assert.assertEquals("Input in table 'dataflow' not set correctly", 1, dataSets.getShort("dataflow.input"));
                    dataNodeIDs.remove(new Integer(subDOInitDataNodeID));
                }
                else if (dataNodeID == subDOEndDataNodeID) {
                    Assert.assertEquals("Input in table 'dataset' not set correctly", 0, dataSets.getShort("dataset.input"));
                    Assert.assertEquals("ControlNodeID in table 'dataflow' not set correctly", b1FragB, dataSets.getInt("dataflow.controlnode_id"));
                    Assert.assertEquals("Input in table 'dataflow' not set correctly", 0, dataSets.getShort("dataflow.input"));
                    dataNodeIDs.remove(new Integer(subDOEndDataNodeID));
                }
                else if (dataNodeID == dOInitFragBDataNodeID) {
                    Assert.assertEquals("Input in table 'dataset' not set correctly", 0, dataSets.getShort("dataset.input"));
                    Assert.assertEquals("ControlNodeID in table 'dataflow' not set correctly", b2FragB, dataSets.getInt("dataflow.controlnode_id"));
                    Assert.assertEquals("Input in table 'dataflow' not set correctly", 0, dataSets.getShort("dataflow.input"));
                    dataNodeIDs.remove(new Integer(dOInitFragBDataNodeID));
                }
                else {
                    // dead branch
                    Assert.fail();
                }
            }
            else {
                Assert.fail("Something wrong in table 'dataflow', 'datasetconsistsofdatanode' or 'dataset' (Too many entries?)");
            }
        }
        dataSets.close();
    }

    private void testControlFlow() throws Exception {
        ResultSet controlFlows = getDbEntries("controlflow");

        Map<Integer, Integer> expectedControlFlow = new HashMap<>();
        expectedControlFlow.put(startEventFragAID, firstAndGwFragAID);
        expectedControlFlow.put(a1FragAID, secondAndFragA);
        expectedControlFlow.put(refFragAID, secondAndFragA);
        expectedControlFlow.put(secondAndFragA, endEventFragA);

        expectedControlFlow.put(startEventFragB, refFragB);
        expectedControlFlow.put(refFragB, firstXORFragB);
        expectedControlFlow.put(b1FragB, secondXORFragB);
        expectedControlFlow.put(b2FragB, secondXORFragB);
        expectedControlFlow.put(secondXORFragB, endEventFragB);

        List<Integer> expectedTargetsOfFirstAndGwFragAID = new LinkedList<>();
        expectedTargetsOfFirstAndGwFragAID.add(a1FragAID);
        expectedTargetsOfFirstAndGwFragAID.add(refFragAID);

        List<Integer> expectedTargetsOfFirstXORFragB = new LinkedList<>();
        expectedTargetsOfFirstXORFragB.add(b1FragB);
        expectedTargetsOfFirstXORFragB.add(b2FragB);

        while (controlFlows.next()) {
            if (controlFlows.getInt("controlnode_id1") == firstAndGwFragAID) {
                Assert.assertTrue("Wrong controlnodeID in table 'controlnode", expectedTargetsOfFirstAndGwFragAID.contains(controlFlows.getInt("controlnode_id2")));
                expectedTargetsOfFirstAndGwFragAID.remove(new Integer(controlFlows.getInt("controlnode_id2")));
            }
            else if (controlFlows.getInt("controlnode_id1") == firstXORFragB) {
                Assert.assertTrue("Wrong controlnodeID in table 'controlnode", expectedTargetsOfFirstXORFragB.contains(controlFlows.getInt("controlnode_id2")));
                expectedTargetsOfFirstXORFragB.remove(new Integer(controlFlows.getInt("controlnode_id2")));
                if (controlFlows.getInt("controlnode_id2") == b1FragB) {
                    Assert.assertEquals("Condition in table 'controlflow' not set correctly", "DO.Attr1 > 0", controlFlows.getString("condition"));
                }
            }
            else if (expectedControlFlow.get(controlFlows.getInt("controlnode_id1")) == controlFlows.getInt("controlnode_id2")) {
                expectedControlFlow.remove(controlFlows.getInt("controlnode_id1"), controlFlows.getInt("controlnode_id2"));
                Assert.assertEquals("Condition in table 'controlflow' not set correctly", "", controlFlows.getString("condition"));
            }
            else {
                Assert.fail("Controlnodes in table 'controlflow' not set correctly (or too many entries inserted)");
            }
        }
        controlFlows.close();
    }

    private void testReferences() throws Exception {
        ResultSet references = getDbEntries("reference");
        Map<Integer, Integer> expectedReferences = new HashMap<>();
        expectedReferences.put(refFragAID, refFragB);
        expectedReferences.put(refFragB, refFragAID);
        while (references.next()) {
            if (expectedReferences.get(references.getInt("controlnode_id1")) == references.getInt("controlnode_id2")) {
                expectedReferences.remove(references.getInt("controlnode_id1"), references.getInt("controlnode_id2"));
            }
            else {
                Assert.fail("References not set correctly");
            }
        }
        references.close();
    }

    private ResultSet getDbEntries (String tablename) {
        String select = "SELECT * " +
                "FROM " + tablename;
        ResultSet rs = null;
        if (conn == null) {
            return null;
        }
        try {
            //Execute a query
            stmt = conn.createStatement();
            rs = stmt.executeQuery(select);
            //Clean-up environment
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        return rs;
    }

    private ResultSet getDataSetJoins () {
        String select = "SELECT * " +
                "FROM dataflow, dataset, datasetconsistsofdatanode " +
                "WHERE dataflow.dataset_id = dataset.id AND " +
                "dataset.id = datasetconsistsofdatanode.dataset_id";
        ResultSet rs = null;
        if (conn == null) {
            return null;
        }
        try {
            //Execute a query
            stmt = conn.createStatement();
            rs = stmt.executeQuery(select);
            //Clean-up environment
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        return rs;
    }

    @AfterClass
    public static void closeConnections() {
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
