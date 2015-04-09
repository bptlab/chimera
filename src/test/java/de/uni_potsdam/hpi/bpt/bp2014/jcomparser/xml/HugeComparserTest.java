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
@PrepareForTest({Scenario.class, Fragment.class, DomainModel.class})
public class HugeComparserTest extends TestSetUp {

    private class DbScenario {
        int id;
        String name;
        long modelID;
        int modelversion;
        long datamodelID;
        int datamodelversion;

        public DbScenario(int id, String name, long modelID, int modelversion, long datamodelID, int datamodelversion) {
            this.id = id;
            this.name = name;
            this.modelID = modelID;
            this.modelversion = modelversion;
            this.datamodelID = datamodelID;
            this.datamodelversion = datamodelversion;
        }

        @Override
        public boolean equals (Object object) {
            DbScenario scenario = (DbScenario) object;
            if (scenario.id == id &&
                    scenario.name.equals(name) &&
                    scenario.modelID == modelID &&
                    scenario.modelversion == modelversion &&
                    scenario.datamodelID == datamodelID &&
                    scenario.datamodelversion == datamodelversion)
                return true;
            return false;
        }
    }

    private class DbFragment {
        int id;
        String name;
        int scenarioID;
        long modelID;
        int modelversion;

        public DbFragment(int id, String name, int scenarioID, long modelID, int modelversion) {
            this.id = id;
            this.name = name;
            this.scenarioID = scenarioID;
            this.modelID = modelID;
            this.modelversion = modelversion;
        }
        @Override
        public boolean equals (Object object) {
            DbFragment fragment = (DbFragment) object;
            if (fragment.id == id &&
                    fragment.name.equals(name) &&
                    fragment.scenarioID == scenarioID &&
                    fragment.modelversion == modelversion &&
                    fragment.modelID == modelID &&
                    fragment.modelversion == modelversion)
                return true;
            return false;
            }
        }

    private class DbDataClass {
        int id;
        String name;

        public DbDataClass(int id, String name) {
            this.id = id;
            this.name = name;
        }
        @Override
        public boolean equals (Object object) {
            DbDataClass dataClass = (DbDataClass) object;
            if (dataClass.id == id &&
                    dataClass.name.equals(name))
                return true;
            return false;
        }
    }

    private class DbDataAttribute {
        int id;
        String name;
        String type;
        String defaultValue;
        int dataClassID;

        public DbDataAttribute(int id, String name, String type, String defaultValue, int dataClassID) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.defaultValue = defaultValue;
            this.dataClassID = dataClassID;
        }
        @Override
        public boolean equals (Object object) {
            DbDataAttribute dataAttribute = (DbDataAttribute) object;
            if (dataAttribute.id == id &&
                    dataAttribute.name.equals(name) &&
                    dataAttribute.type.equals(type) &&
                    dataAttribute.defaultValue.equals(defaultValue) &&
                    dataAttribute.dataClassID == dataClassID)
                return true;
            return false;
        }
    }

    private class DbAggregation {
        int dataClassID1;
        int dataClassID2;
        int multiplicity;

        public DbAggregation(int dataClassID1, int dataClassID2, int multiplicity) {
            this.dataClassID1 = dataClassID1;
            this.dataClassID2 = dataClassID2;
            this.multiplicity = multiplicity;
        }

        @Override
        public boolean equals (Object object) {
            DbAggregation aggregation = (DbAggregation) object;
            if (aggregation.dataClassID1 == dataClassID1 &&
                    aggregation.dataClassID2 == dataClassID2 &&
                    aggregation.multiplicity ==  multiplicity)
                return true;
            return false;
        }
    }

    private class DbState {
        String name;
        int dataClassID;

        public DbState(String name, int dataClassID) {
            this.name = name;
            this.dataClassID = dataClassID;
        }
        @Override
        public boolean equals (Object object) {
            DbState state = (DbState) object;
            if (state.name.equals(name) &&
                    state.dataClassID == dataClassID)
                return true;
            return false;
        }
    }

    private class DbDataObject {
        int id;
        String name;
        int dataClassID;
        int scenarioID;
        int startStateID;

        public DbDataObject(int id, String name, int dataClassID, int scenarioID, int startStateID) {
            this.id = id;
            this.name = name;
            this.dataClassID = dataClassID;
            this.scenarioID = scenarioID;
            this.startStateID = startStateID;
        }
        @Override
        public boolean equals (Object object) {
            DbDataObject dataObject = (DbDataObject) object;
            if (dataObject.id == id &&
                    dataObject.name.equals(name) &&
                    dataObject.dataClassID == dataClassID &&
                    dataObject.scenarioID == scenarioID &&
                    dataObject.startStateID == startStateID)
                return true;
            return false;
        }
    }

    private class DbControlNode {
        String name;
        String type;
        int fragmentID;
        long modelID;

        public DbControlNode(String name, String type, int fragmentID, long modelID) {
            this.name = name;
            this.type = type;
            this.fragmentID = fragmentID;
            this.modelID = modelID;
        }

        @Override
        public boolean equals (Object object) {
            DbControlNode controlNode = (DbControlNode) object;
            if (controlNode.name.equals(name) &&
                    controlNode.type.equals(type) &&
                    controlNode.fragmentID == fragmentID &&
                    controlNode.modelID == modelID)
                return true;
            return false;
        }
    }

    private class DbDataNode {
        int scenarioID;
        int stateID;
        int dataClassID;
        int dataObjectID;
        long modelID;

        public DbDataNode(int scenarioID, int stateID, int dataClassID, int dataObjectID, long modelID) {
            this.scenarioID = scenarioID;
            this.stateID = stateID;
            this.dataClassID = dataClassID;
            this.dataObjectID = dataObjectID;
            this.modelID = modelID;
        }
        @Override
        public boolean equals (Object object) {
            DbDataNode dataNode = (DbDataNode) object;
            if (dataNode.scenarioID == scenarioID &&
                    dataNode.stateID == stateID &&
                    dataNode.dataClassID == dataClassID &&
                    dataNode.dataObjectID == dataObjectID &&
                    dataNode.modelID == modelID)
                return true;
            return false;
        }
    }

    private class DbDataSetAndFlow {
        // table dataflow
        int controlNodeID;
        short inputDataFlow;
        // table dataset
        short inputDataSet;
        // table datasetconsistsofdatanode
        int dataNodeID;

        public DbDataSetAndFlow(int controlNodeID, short inputDataFlow, short inputDataSet, int dataNodeID) {
            this.controlNodeID = controlNodeID;
            this.inputDataFlow = inputDataFlow;
            this.inputDataSet = inputDataSet;
            this.dataNodeID = dataNodeID;
        }

        @Override
        public boolean equals (Object object) {
            DbDataSetAndFlow set = (DbDataSetAndFlow) object;
            if (set.controlNodeID == controlNodeID &&
                    set.inputDataFlow == inputDataFlow &&
                    set.inputDataSet == inputDataSet &&
                    set.dataNodeID == dataNodeID)
                return true;
            return false;
        }
    }

    private class DbControlFlow {
        int id1;
        int id2;
        String condition;

        public DbControlFlow(int id1, int id2, String condition) {
            this.id1 = id1;
            this.id2 = id2;
            this.condition = condition;
        }

        @Override
        public boolean equals (Object object) {
            DbControlFlow flow = (DbControlFlow) object;
            if (flow.id1 == id1 &&
                    flow.id2 == id2 &&
                    flow.condition.equals(condition))
                return true;
            return false;
        }
    }

    private class DbReference {
        int id1;
        int id2;

        public DbReference(int id1, int id2) {
            this.id1 = id1;
            this.id2 = id2;
        }

        @Override
        public boolean equals (Object object) {
            DbReference flow = (DbReference) object;
            if (flow.id1 == id1 &&
                    flow.id2 == id2)
                return true;
            return false;
        }
    }

    private static java.sql.Connection conn;
    private static Statement stmt = null;
    private List<DbScenario> scenarios;
    private Map<String, DbFragment> fragments;
    private Map<String, DbDataClass> dataClasses;
    private List<DbDataAttribute> dataAttributes;
    private List<DbAggregation> aggregations;
    private List<DbState> states;
    private Map<String, DbDataObject> dataObjects;
    private Map<Long, DbControlNode> controlNodes;
    private Map<Long, DbDataNode> dataNodes;
    private List<DbDataSetAndFlow> sets;
    private List<DbControlFlow> controlFlows;
    private List<DbReference> references;
    // stateIDs
    private int subDOInit;
    private int subDOEnd;
    private int dOInit;
    private int dOState1;
    // dataNodeIDs
    private int dOInitFragA;
    private int dOState1FragA;
    private int dOState1FragB;
    private int subDOInitFragB;
    private int subDOEndFragB;
    private int dOInitFragB;
    // controlNodeIDs
    private int startEventFagA;
    private int firstANDFragA;
    private int refFragA;
    private int a1FragA;
    private int secondANDFragA;
    private int endEventFragA;
    private int startEventFragB;
    private int firstXORFragB;
    private int b2FragB;
    private int secondXORFragB;
    private int endEventFragB;
    private int refFragB;
    private int b1FragB;

    @Before
    public void setUp() throws Exception {
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
        scenarios = new LinkedList<>();
        fragments = new HashMap<>();
        dataClasses = new HashMap<>();
        dataAttributes = new LinkedList<>();
        states = new LinkedList<>();
        dataObjects = new HashMap<>();
        controlNodes = new HashMap<>();
        dataNodes = new HashMap<>();
        aggregations = new LinkedList<>();
        sets = new LinkedList<>();
        controlFlows = new LinkedList<>();
        references = new LinkedList<>();
    }

    @Test
    public void test() throws Exception {
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
        testTerminationCondition();

    }
    private void testScenario() throws Exception {
        ResultSet scenarios = getDbEntries("scenario");
        while (scenarios.next()) {
            DbScenario scenario = new DbScenario(
                    scenarios.getInt("id"),
                    scenarios.getString("name"),
                    scenarios.getLong("modelid"),
                    scenarios.getInt("modelversion"),
                    scenarios.getLong("datamodelid"),
                    scenarios.getInt("datamodelversion"));
            this.scenarios.add(scenario);
        }
        Assert.assertTrue("Scenario not inserted correctly", this.scenarios.contains(new DbScenario(1, "Scenario", 358512L, 1, 790983467L, 0)));
        Assert.assertTrue("Too many scenarios inserted", this.scenarios.size() == 1);
        scenarios.close();
    }

    private void testFragments() throws Exception {
        ResultSet fragments = getDbEntries("fragment");
        while (fragments.next()) {
            DbFragment fragment = new DbFragment(
                fragments.getInt("id"),
                fragments.getString("name"),
                fragments.getInt("scenario_id"),
                fragments.getLong("modelid"),
                fragments.getInt("modelversion"));
            this.fragments.put(fragments.getString("name"), fragment);
        }
        Assert.assertTrue("Fragment not inserted correctly", this.fragments.values().contains(new DbFragment(1, "FragmentA", scenarios.get(0).id, 1084827857L, 0)));
        Assert.assertTrue("Fragment not inserted correctly", this.fragments.values().contains(new DbFragment(2, "FragmentB", scenarios.get(0).id, 894096069L, 0)));
        Assert.assertTrue("Too many fragments inserted", this.fragments.size() == 2);
        fragments.close();
    }

    private void testDataClass() throws Exception {
        ResultSet dataClasses = getDbEntries("dataclass");
        while (dataClasses.next()) {
            DbDataClass dataClass = new DbDataClass(
                    dataClasses.getInt("id"),
                    dataClasses.getString("name"));
            this.dataClasses.put(dataClasses.getString("name"), dataClass);
        }
        Assert.assertTrue("DataClass not inserted correctly", this.dataClasses.values().contains(new DbDataClass(1, "DO")));
        Assert.assertTrue("DataClass not inserted correctly", this.dataClasses.values().contains(new DbDataClass(2, "SubDO")));
        Assert.assertTrue("Too many dataClasses inserted", this.dataClasses.size() == 2);
        dataClasses.close();
    }

    private void testDataAttributes() throws Exception {
        ResultSet dataAttributes = getDbEntries("dataattribute");
        while (dataAttributes.next()) {
            DbDataAttribute dataClass = new DbDataAttribute(
                    dataAttributes.getInt("id"),
                    dataAttributes.getString("name"),
                    dataAttributes.getString("type"),
                    dataAttributes.getString("default"),
                    dataAttributes.getInt("dataclass_id"));
            this.dataAttributes.add(dataClass);
        }
        Assert.assertTrue("DataAttributes not inserted correctly", this.dataAttributes.contains(new DbDataAttribute(1, "Attr1", "", "", dataClasses.get("DO").id)));
        Assert.assertTrue("DataAttributes not inserted correctly", this.dataAttributes.contains(new DbDataAttribute(2, "Attr2", "", "", dataClasses.get("DO").id)));
        Assert.assertTrue("DataAttributes not inserted correctly", this.dataAttributes.contains(new DbDataAttribute(3, "Attr3", "", "", dataClasses.get("SubDO").id)));
        Assert.assertTrue("Too many dataAttributes have been inserted", this.dataAttributes.size() == 3);
        dataAttributes.close();
    }

    private void testAggregation() throws Exception {
        ResultSet aggregations = getDbEntries("aggregation");
        while (aggregations.next()) {
            DbAggregation aggregation = new DbAggregation (
                    aggregations.getInt("dataclass_id1"),
                    aggregations.getInt("dataclass_id2"),
                    aggregations.getInt("multiplicity"));
            this.aggregations.add(aggregation);
        }
        Assert.assertTrue("Aggregations not inserted correctly", this.aggregations.contains(new DbAggregation(dataClasses.get("DO").id, dataClasses.get("SubDO").id, Integer.MAX_VALUE)));
        Assert.assertTrue("Too many Aggregations have been inserted", this.aggregations.size() == 1);
        aggregations.close();
    }

    private void testStates() throws Exception {
        ResultSet states = getDbEntries("state");
        while (states.next()) {
            DbState state = new DbState (
                    states.getString("name"),
                    states.getInt("olc_id"));
            this.states.add(state);
            int id = states.getInt("id");
            Assert.assertTrue("ID in table 'state' smaller than 1", id > 0);
            if (state.name.equals("end"))
                subDOEnd = id;
            else if (state.name.equals("state1"))
                dOState1 = id;
            else if (state.name.equals("init")) {
                if (state.dataClassID == dataClasses.get("DO").id)
                    dOInit = id;
                else
                    subDOInit = id;
            }
        }
        Assert.assertTrue("State in table 'state' not inserted correctly", this.states.contains(new DbState("state1", dataClasses.get("DO").id)));
        Assert.assertTrue("State in table 'state' not inserted correctly", this.states.contains(new DbState("init", dataClasses.get("DO").id)));
        Assert.assertTrue("State in table 'state' not inserted correctly", this.states.contains(new DbState("end", dataClasses.get("SubDO").id)));
        Assert.assertTrue("State in table 'state' not inserted correctly", this.states.contains(new DbState("init", dataClasses.get("SubDO").id)));
        Assert.assertTrue("Too many states have been inserted", this.states.size() == 4);
        states.close();
    }

    private void testDataObjects() throws Exception {
        ResultSet dataObjects = getDbEntries("dataobject");
        while (dataObjects.next()) {
            DbDataObject dO = new DbDataObject(
                    dataObjects.getInt("id"),
                    dataObjects.getString("name"),
                    dataObjects.getInt("dataclass_id"),
                    dataObjects.getInt("scenario_id"),
                    dataObjects.getInt("start_state_id"));
            this.dataObjects.put(dataObjects.getString("name"), dO);
        }
        Assert.assertTrue("DataObject in table 'dataobject' not inserted correctly", this.dataObjects.values().contains(new DbDataObject(1, "SubDO", dataClasses.get("SubDO").id, scenarios.get(0).id, subDOInit)));
        Assert.assertTrue("DataObject in table 'dataobject' not inserted correctly", this.dataObjects.values().contains(new DbDataObject(2, "DO", dataClasses.get("DO").id, scenarios.get(0).id, dOInit)));
        Assert.assertTrue("Too many dataObjects have been inserted", this.dataObjects.size() == 2);
        dataObjects.close();
    }

    private void testDataNodes() throws Exception {
        ResultSet dataNodes = getDbEntries("datanode");
        while (dataNodes.next()) {
            DbDataNode dataNode = new DbDataNode(
                    dataNodes.getInt("scenario_id"),
                    dataNodes.getInt("state_id"),
                    dataNodes.getInt("dataclass_id"),
                    dataNodes.getInt("dataobject_id"),
                    dataNodes.getLong("modelid"));
            int id = dataNodes.getInt("id");
            switch (dataNodes.getString("modelid")) {
                case "135409402":
                    dOInitFragA = id;
                    break;
                case "650069438":
                    dOState1FragA = id;
                    break;
                case "611573211":
                    dOState1FragB = id;
                    break;
                case "1368161079":
                    subDOInitFragB = id;
                    break;
                case "1517694277":
                    subDOEndFragB = id;
                    break;
                case "155099451":
                    dOInitFragB = id;
                    break;
                default: Assert.fail("Wrong modelID in table 'datanode' inserted");
            }

            this.dataNodes.put(dataNodes.getLong("modelid"), dataNode);
        }
        Assert.assertTrue("DataNode in table 'datanode' not inserted correctly", this.dataNodes.values().contains(new DbDataNode(scenarios.get(0).id, subDOEnd, dataClasses.get("SubDO").id, dataObjects.get("SubDO").id, 1517694277L)));
        Assert.assertTrue("DataNode in table 'datanode' not inserted correctly", this.dataNodes.values().contains(new DbDataNode(scenarios.get(0).id, subDOInit, dataClasses.get("SubDO").id, dataObjects.get("SubDO").id, 1368161079L)));
        Assert.assertTrue("DataNode in table 'datanode' not inserted correctly", this.dataNodes.values().contains(new DbDataNode(scenarios.get(0).id, dOState1, dataClasses.get("DO").id, dataObjects.get("DO").id, 650069438L)));
        Assert.assertTrue("DataNode in table 'datanode' not inserted correctly", this.dataNodes.values().contains(new DbDataNode(scenarios.get(0).id, dOInit, dataClasses.get("DO").id, dataObjects.get("DO").id, 135409402L)));
        Assert.assertTrue("DataNode in table 'datanode' not inserted correctly", this.dataNodes.values().contains(new DbDataNode(scenarios.get(0).id, dOInit, dataClasses.get("DO").id, dataObjects.get("DO").id, 155099451L)));
        Assert.assertTrue("DataNode in table 'datanode' not inserted correctly", this.dataNodes.values().contains(new DbDataNode(scenarios.get(0).id, dOState1, dataClasses.get("DO").id, dataObjects.get("DO").id, 611573211L)));
        Assert.assertTrue("Too many dataNodes have been inserted", this.dataNodes.size() == 6);
        dataNodes.close();
    }

    private void testControlNodes() throws Exception {
        ResultSet controlNodes = getDbEntries("controlnode");
        while (controlNodes.next()) {
            DbControlNode controlNode = new DbControlNode(
                    controlNodes.getString("label"),
                    controlNodes.getString("type"),
                    controlNodes.getInt("fragment_id"),
                    controlNodes.getLong("modelid"));
            //! referenced activities hold the same modelid -> we change the id in the instance of DbControlNode:
            // referenced activity in FragmentA: 826790323 to 8267903230
            // referenced activity in FragmentB: 826790323 to 8267903231
            if (controlNodes.getLong("modelid") == 826790323L && controlNodes.getInt("fragment_id") == fragments.get("FragmentA").id)
                controlNode.modelID = 8267903230L;
            else if (controlNodes.getLong("modelid") == 826790323L && controlNodes.getInt("fragment_id") == fragments.get("FragmentB").id)
                controlNode.modelID = 8267903231L;
            this.controlNodes.put(controlNode.modelID, controlNode);
            int id = controlNodes.getInt("id");
            Assert.assertTrue("ControlNodeID in table 'controlnode' smaller than 1", id > 0);
            switch (Long.toString(controlNode.modelID)) {
                case "1069345757":
                    startEventFagA = id;
                    break;
                case "1569336784":
                    firstANDFragA = id;
                    break;
                case "8267903230":
                    refFragA = id;
                    break;
                case "517729148":
                    a1FragA = id;
                    break;
                case "2081480666":
                    secondANDFragA = id;
                    break;
                case "1914825610":
                    endEventFragA = id;
                    break;
                case "509231813":
                    startEventFragB = id;
                    break;
                case "1689490932":
                    firstXORFragB = id;
                    break;
                case "1821614206":
                    b2FragB = id;
                    break;
                case "564114893":
                    secondXORFragB = id;
                    break;
                case "2136115766":
                    endEventFragB = id;
                    break;
                case "8267903231":
                    refFragB = id;
                    break;
                case "1407636184":
                    b1FragB = id;
                    break;
                default:
                    Assert.fail("Wrong modelID in table 'controlnode' inserted");
            }
        }
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode("", "Startevent", fragments.get("FragmentA").id, 1069345757L)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode("Ref", "Activity", fragments.get("FragmentA").id, 8267903230L)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode("A1", "Activity", fragments.get("FragmentA").id, 517729148L)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode("", "AND", fragments.get("FragmentA").id, 1569336784L)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode("", "AND", fragments.get("FragmentA").id, 2081480666L)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode("", "Endevent", fragments.get("FragmentA").id, 1914825610L)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode("Ref", "Activity", fragments.get("FragmentB").id, 8267903231L)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode("", "XOR", fragments.get("FragmentB").id, 564114893L)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode("", "Endevent", fragments.get("FragmentB").id, 2136115766L)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode("", "XOR", fragments.get("FragmentB").id, 1689490932L)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode("B2", "EmailTask", fragments.get("FragmentB").id, 1821614206)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode("B1", "Activity", fragments.get("FragmentB").id, 1407636184)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode("", "Startevent", fragments.get("FragmentB").id, 509231813L)));
        Assert.assertTrue("Too many controlNodes have been inserted", this.controlNodes.size() == 13);
        controlNodes.close();
    }

    private void testDataSetAndDataFlow() throws Exception {
        ResultSet dataSets = getDataSetJoins();
        while (dataSets.next()) {
            DbDataSetAndFlow set = new DbDataSetAndFlow(
                    dataSets.getInt("dataflow.controlnode_id"),
                    dataSets.getShort("dataflow.input"),
                    dataSets.getShort("dataset.input"),
                    dataSets.getInt("datasetconsistsofdatanode.datanode_id"));
            this.sets.add(set);
            Assert.assertTrue("DataSetID in table 'dataset', 'dataflow' or 'datasetconsistsofdatanode' smaller than 1", dataSets.getInt("dataset.id") > 0);
        }
        Assert.assertTrue("DataSet in table 'dataset', 'dataflow' or 'datasetconsistsofdatanode' not inserted correctly", this.sets.contains(new DbDataSetAndFlow(a1FragA, (short)1, (short)1, dOInitFragA)));
        Assert.assertTrue("DataSet in table 'dataset', 'dataflow' or 'datasetconsistsofdatanode' not inserted correctly", this.sets.contains(new DbDataSetAndFlow(a1FragA, (short)0, (short)0, dOState1FragA)));
        Assert.assertTrue("DataSet in table 'dataset', 'dataflow' or 'datasetconsistsofdatanode' not inserted correctly", this.sets.contains(new DbDataSetAndFlow(refFragB, (short)1, (short)1, dOState1FragB)));
        Assert.assertTrue("DataSet in table 'dataset', 'dataflow' or 'datasetconsistsofdatanode' not inserted correctly", this.sets.contains(new DbDataSetAndFlow(b1FragB, (short)1, (short)1, subDOInitFragB)));
        Assert.assertTrue("DataSet in table 'dataset', 'dataflow' or 'datasetconsistsofdatanode' not inserted correctly", this.sets.contains(new DbDataSetAndFlow(b2FragB, (short)0, (short)0, dOInitFragB)));
        Assert.assertTrue("DataSet in table 'dataset', 'dataflow' or 'datasetconsistsofdatanode' not inserted correctly", this.sets.contains(new DbDataSetAndFlow(b1FragB, (short)0, (short)0, subDOEndFragB)));
        Assert.assertTrue("Too many datasets in table 'dataset', 'dataflow' or 'datasetconsistsofdatanode' have been inserted", this.sets.size() == 6);

        dataSets.close();
    }

    private void testControlFlow() throws Exception {
        ResultSet controlFlows = getDbEntries("controlflow");
        while (controlFlows.next()) {
            DbControlFlow flow = new DbControlFlow(
                    controlFlows.getInt("controlnode_id1"),
                    controlFlows.getInt("controlnode_id2"),
                    controlFlows.getString("condition"));
            this.controlFlows.add(flow);
        }
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(startEventFagA, firstANDFragA, "")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(firstANDFragA, refFragA, "")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(firstANDFragA, a1FragA, "")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(a1FragA, secondANDFragA, "")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(refFragA, secondANDFragA, "")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(secondANDFragA, endEventFragA, "")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(firstXORFragB, b2FragB, "")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(b2FragB, secondXORFragB, "")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(secondXORFragB, endEventFragB, "")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(startEventFragB, refFragB, "")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(refFragB, firstXORFragB, "")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(firstXORFragB, b1FragB, "DO.Attr1 > 0")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(b1FragB, secondXORFragB, "")));
        Assert.assertTrue("Too many controlfows in table 'controlflow' have been inserted", this.controlFlows.size() == 13);
        controlFlows.close();
    }

    private void testReferences() throws Exception {
        ResultSet references = getDbEntries("reference");
        while (references.next()) {
            DbReference reference = new DbReference(
                    references.getInt("controlnode_id1"),
                    references.getInt("controlnode_id2"));
            this.references.add(reference);
        }
        Assert.assertTrue("Reference in table 'reference' not inserted correctly", this.references.contains(new DbReference(refFragB, refFragA)));
        Assert.assertTrue("Reference in table 'reference' not inserted correctly", this.references.contains(new DbReference(refFragA, refFragB)));
        Assert.assertTrue("Too many references in table 'reference' have been inserted", this.references.size() == 2);
        references.close();
    }

    private void testTerminationCondition() throws Exception {
        ResultSet terminationCondition = getDbEntries("terminationcondition");
        terminationCondition.next();
        Assert.assertEquals("ConditionsetID in table 'terminationcondition' not inserted correctly", 1, terminationCondition.getInt("conditionset_id"));
        Assert.assertEquals("DataobjectID in table 'terminationcondition' not inserted correctly", dataObjects.get("SubDO").id, terminationCondition.getInt("dataobject_id"));
        Assert.assertEquals("StateID in table 'terminationcondition' not inserted correctly", subDOEnd, terminationCondition.getInt("state_id"));
        Assert.assertEquals("ScenarioID in table 'terminationcondition' not inserted correctly", scenarios.get(0).id, terminationCondition.getInt("scenario_id"));
        Assert.assertFalse("Too many entries in table 'terminationcondition'", terminationCondition.next());
        terminationCondition.close();
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