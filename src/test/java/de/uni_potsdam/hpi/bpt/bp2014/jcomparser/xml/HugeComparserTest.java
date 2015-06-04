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
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Scenario.class, Fragment.class, DomainModel.class})
public class HugeComparserTest extends TestSetUp {
    /**
     *
     */
    private class DbScenario {
        int id;
        String name;
        long modelID;
        int modelversion;
        long datamodelID;
        int datamodelversion;
        short deleted;

        /**
         *
         * @param id
         * @param name
         * @param modelID
         * @param modelversion
         * @param datamodelID
         * @param datamodelversion
         * @param deleted
         */
        public DbScenario(int id, String name, long modelID, int modelversion, long datamodelID, int datamodelversion, short deleted) {
            this.id = id;
            this.name = name;
            this.modelID = modelID;
            this.modelversion = modelversion;
            this.datamodelID = datamodelID;
            this.datamodelversion = datamodelversion;
            this.deleted = deleted;
        }

        @Override
        public boolean equals (Object object) {
            DbScenario scenario = (DbScenario) object;
            if (scenario.name.equals(name) &&
                    scenario.modelID == modelID &&
                    scenario.modelversion == modelversion &&
                    scenario.datamodelID == datamodelID &&
                    scenario.datamodelversion == datamodelversion &&
                    scenario.deleted == deleted)
                return true;
            return false;
        }
    }

    /**
     *
     */
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
            if (fragment.name.equals(name) &&
                    fragment.scenarioID == scenarioID &&
                    fragment.modelversion == modelversion &&
                    fragment.modelID == modelID &&
                    fragment.modelversion == modelversion)
                return true;
            return false;
        }
    }

    /**
     *
     */
    private class DbDataClass {
        int id;
        String name;
        short rootnode;

        public DbDataClass(int id, String name, short rootnode) {
            this.id = id;
            this.name = name;
            this.rootnode = rootnode;
        }
        @Override
        public boolean equals (Object object) {
            DbDataClass dataClass = (DbDataClass) object;
            if (dataClass.name.equals(name) &&
                    dataClass.rootnode == rootnode)
                return true;
            return false;
        }
    }

    /**
     *
     */
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
            if (dataAttribute.name.equals(name) &&
                    dataAttribute.type.equals(type) &&
                    dataAttribute.defaultValue.equals(defaultValue) &&
                    dataAttribute.dataClassID == dataClassID)
                return true;
            return false;
        }
    }

    /**
     *
     */
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

    /**
     *
     */
    private class DbState {
        int id;
        String name;
        int dataClassID;

        public DbState(int id, String name, int dataClassID) {
            this.id = id;
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

    /**
     *
     */
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
            if (dataObject.name.equals(name) &&
                    dataObject.dataClassID == dataClassID &&
                    dataObject.scenarioID == scenarioID &&
                    dataObject.startStateID == startStateID)
                return true;
            return false;
        }
    }

    /**
     *
     */
    private class DbControlNode {
        int id;
        String name;
        String type;
        int fragmentID;
        long modelID;

        public DbControlNode(int id, String name, String type, int fragmentID, long modelID) {
            this.id = id;
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

    /**
     *
     */
    private class DbDataNode {
        int id;
        int scenarioID;
        int stateID;
        int dataClassID;
        int dataObjectID;
        long modelID;

        public DbDataNode(int id, int scenarioID, int stateID, int dataClassID, int dataObjectID, long modelID) {
            this.id = id;
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

    /**
     *
     */
    private class DbDataSetAndFlow {
        int dataSetID;
        // table dataflow
        int controlNodeID;
        short inputDataFlow;
        // table dataset
        short inputDataSet;
        // table datasetconsistsofdatanode
        int dataNodeID;

        public DbDataSetAndFlow(int dataSetID, int controlNodeID, short inputDataFlow, short inputDataSet, int dataNodeID) {
            this.dataSetID = dataSetID;
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

    /**
     *
     */
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

    /**
     *
     */
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

    /**
     *
     */
    private class DbTerminationCondition {
        int conditionset_id;
        int dataobject_id;
        int state_id;
        int scenario_id;

        public DbTerminationCondition(int conditionset_id, int dataobject_id, int state_id, int scenario_id) {
            this.conditionset_id = conditionset_id;
            this.dataobject_id = dataobject_id;
            this.state_id = state_id;
            this.scenario_id = scenario_id;
        }

        @Override
        public boolean equals (Object object) {
            DbTerminationCondition tc = (DbTerminationCondition) object;
            if (tc.conditionset_id == conditionset_id &&
                    tc.dataobject_id == dataobject_id &&
                    tc.state_id == state_id &&
                    tc.scenario_id == scenario_id)
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
    private List<DbTerminationCondition> terminationCondition;

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
        terminationCondition = new LinkedList<>();
    }

    @Test
    public void test() throws Exception {
        testScenario();
        testFragments();
        testDataClass();
        testDataAttributes();
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

    /**
     *
     * @throws Exception
     */
    private void testScenario() throws Exception {
        ResultSet scenarios = getDbEntries("scenario");
        while (scenarios.next()) {
            DbScenario scenario = new DbScenario(
                    scenarios.getInt("id"),
                    scenarios.getString("name"),
                    scenarios.getLong("modelid"),
                    scenarios.getInt("modelversion"),
                    scenarios.getLong("datamodelid"),
                    scenarios.getInt("datamodelversion"),
                    scenarios.getShort("deleted"));
            this.scenarios.add(scenario);
            Assert.assertTrue("ScenarioID smaller than 1", scenario.id > 0);
        }
        Assert.assertTrue("Scenario not inserted correctly", this.scenarios.contains(new DbScenario(1, "Scenario", 358512L, 1, 790983467L, 0, (short) 0)));
        Assert.assertTrue("Too many scenarios inserted", this.scenarios.size() == 1);
        scenarios.close();
    }

    /**
     *
     * @throws Exception
     */
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
            Assert.assertTrue("FragmentID smaller than 1", fragment.id > 0);
        }
        Assert.assertTrue("Fragment not inserted correctly", this.fragments.values().contains(new DbFragment(1, "FragmentA", scenarios.get(0).id, 1084827857L, 0)));
        Assert.assertTrue("Fragment not inserted correctly", this.fragments.values().contains(new DbFragment(2, "FragmentB", scenarios.get(0).id, 894096069L, 0)));
        Assert.assertTrue("Too many fragments inserted", this.fragments.size() == 2);
        fragments.close();
    }

    /**
     *
     * @throws Exception
     */
    private void testDataClass() throws Exception {
        ResultSet dataClasses = getDbEntries("dataclass");
        while (dataClasses.next()) {
            DbDataClass dataClass = new DbDataClass(
                    dataClasses.getInt("id"),
                    dataClasses.getString("name"),
                    dataClasses.getShort("rootnode"));
            this.dataClasses.put(dataClasses.getString("name"), dataClass);
            Assert.assertTrue("DataClassID smaller than 1", dataClass.id > 0);
        }
        Assert.assertTrue("DataClass not inserted correctly", this.dataClasses.values().contains(new DbDataClass(1, "DO", (short) 1)));
        Assert.assertTrue("DataClass not inserted correctly", this.dataClasses.values().contains(new DbDataClass(2, "SubD", (short) 0)));
        Assert.assertTrue("Too many dataClasses inserted", this.dataClasses.size() == 2);
        dataClasses.close();
    }

    /**
     *
     * @throws Exception
     */
    private void testDataAttributes() throws Exception {
        ResultSet dataAttributes = getDbEntries("dataattribute");
        while (dataAttributes.next()) {
            DbDataAttribute dataAttribute = new DbDataAttribute(
                    dataAttributes.getInt("id"),
                    dataAttributes.getString("name"),
                    dataAttributes.getString("type"),
                    dataAttributes.getString("default"),
                    dataAttributes.getInt("dataclass_id"));
            this.dataAttributes.add(dataAttribute);
            Assert.assertTrue("DataAttributeID smaller than 1", dataAttribute.id > 0);
        }
        Assert.assertTrue("DataAttributes not inserted correctly", this.dataAttributes.contains(new DbDataAttribute(1, "Attr1", "", "", dataClasses.get("DO").id)));
        Assert.assertTrue("DataAttributes not inserted correctly", this.dataAttributes.contains(new DbDataAttribute(2, "Attr2", "", "", dataClasses.get("DO").id)));
        Assert.assertTrue("DataAttributes not inserted correctly", this.dataAttributes.contains(new DbDataAttribute(3, "Attr3", "", "", dataClasses.get("SubD").id)));
        Assert.assertTrue("Too many dataAttributes have been inserted", this.dataAttributes.size() == 3);
        dataAttributes.close();
    }

    /**
     *
     * @throws Exception
     */
    private void testAggregation() throws Exception {
        ResultSet aggregations = getDbEntries("aggregation");
        while (aggregations.next()) {
            DbAggregation aggregation = new DbAggregation (
                    aggregations.getInt("dataclass_id1"),
                    aggregations.getInt("dataclass_id2"),
                    aggregations.getInt("multiplicity"));
            this.aggregations.add(aggregation);
        }
        Assert.assertTrue("Aggregations not inserted correctly", this.aggregations.contains(new DbAggregation(dataClasses.get("DO").id, dataClasses.get("SubD").id, Integer.MAX_VALUE)));
        Assert.assertTrue("Too many Aggregations have been inserted", this.aggregations.size() == 1);
        aggregations.close();
    }

    /**
     *
     * @throws Exception
     */
    private void testStates() throws Exception {
        ResultSet states = getDbEntries("state");
        while (states.next()) {
            DbState state = new DbState (
                    states.getInt("id"),
                    states.getString("name"),
                    states.getInt("olc_id"));
            this.states.add(state);
            Assert.assertTrue("StateID smaller than 1", state.id > 0);
        }
        Assert.assertTrue("State in table 'state' not inserted correctly", this.states.contains(new DbState(1, "state1", dataClasses.get("DO").id)));
        Assert.assertTrue("State in table 'state' not inserted correctly", this.states.contains(new DbState(2, "init", dataClasses.get("DO").id)));
        Assert.assertTrue("State in table 'state' not inserted correctly", this.states.contains(new DbState(3, "end", dataClasses.get("SubD").id)));
        Assert.assertTrue("State in table 'state' not inserted correctly", this.states.contains(new DbState(4, "init", dataClasses.get("SubD").id)));
        Assert.assertTrue("Too many states have been inserted", this.states.size() == 4);
        states.close();
    }

    /**
     *
     * @throws Exception
     */
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
            Assert.assertTrue("DataObjectID smaller than 1", dO.id > 0);
        }
        int initDO = 0, initSubDO = 0;
        for (DbState state : states) {
            if (state.name.equals("init") && state.dataClassID == dataClasses.get("DO").id)
                initDO = state.id;
            else if (state.name.equals("init") && state.dataClassID == dataClasses.get("SubD").id)
                initSubDO = state.id;
        }
        Assert.assertTrue("DataObject in table 'dataobject' not inserted correctly", this.dataObjects.values().contains(new DbDataObject(1, "SubDO", dataClasses.get("SubD").id, scenarios.get(0).id, initSubDO)));
        Assert.assertTrue("DataObject in table 'dataobject' not inserted correctly", this.dataObjects.values().contains(new DbDataObject(2, "DO", dataClasses.get("DO").id, scenarios.get(0).id, initDO)));
        Assert.assertTrue("Too many dataObjects have been inserted", this.dataObjects.size() == 2);
        dataObjects.close();
    }

    /**
     *
     * @throws Exception
     */
    private void testDataNodes() throws Exception {
        ResultSet dataNodes = getDbEntries("datanode");
        while (dataNodes.next()) {
            DbDataNode dataNode = new DbDataNode(
                    dataNodes.getInt("id"),
                    dataNodes.getInt("scenario_id"),
                    dataNodes.getInt("state_id"),
                    dataNodes.getInt("dataclass_id"),
                    dataNodes.getInt("dataobject_id"),
                    dataNodes.getLong("modelid"));
            this.dataNodes.put(dataNodes.getLong("modelid"), dataNode);
            Assert.assertTrue("DataNodeID smaller than 1", dataNode.id > 0);
        }
        int endSubDO = 0, initSubDO = 0, state1DO = 0, initDO = 0;
        for (DbState state : states) {
            if (state.name.equals("end"))
                endSubDO = state.id;
            else if (state.name.equals("init") && state.dataClassID == dataClasses.get("SubD").id)
                initSubDO = state.id;
            else if (state.name.equals("init") && state.dataClassID == dataClasses.get("DO").id)
                initDO = state.id;
            else if (state.name.equals("state1"))
                state1DO = state.id;
        }
        Assert.assertTrue("DataNode in table 'datanode' not inserted correctly", this.dataNodes.values().contains(new DbDataNode(1, scenarios.get(0).id, endSubDO, dataClasses.get("SubD").id, dataObjects.get("SubDO").id, 1517694277L)));
        Assert.assertTrue("DataNode in table 'datanode' not inserted correctly", this.dataNodes.values().contains(new DbDataNode(2, scenarios.get(0).id, initSubDO, dataClasses.get("SubD").id, dataObjects.get("SubDO").id, 1368161079L)));
        Assert.assertTrue("DataNode in table 'datanode' not inserted correctly", this.dataNodes.values().contains(new DbDataNode(3, scenarios.get(0).id, state1DO, dataClasses.get("DO").id, dataObjects.get("DO").id, 650069438L)));
        Assert.assertTrue("DataNode in table 'datanode' not inserted correctly", this.dataNodes.values().contains(new DbDataNode(4, scenarios.get(0).id, initDO, dataClasses.get("DO").id, dataObjects.get("DO").id, 135409402L)));
        Assert.assertTrue("DataNode in table 'datanode' not inserted correctly", this.dataNodes.values().contains(new DbDataNode(5, scenarios.get(0).id, initDO, dataClasses.get("DO").id, dataObjects.get("DO").id, 155099451L)));
        Assert.assertTrue("DataNode in table 'datanode' not inserted correctly", this.dataNodes.values().contains(new DbDataNode(6, scenarios.get(0).id, state1DO, dataClasses.get("DO").id, dataObjects.get("DO").id, 611573211L)));
        Assert.assertTrue("Too many dataNodes have been inserted", this.dataNodes.size() == 6);
        dataNodes.close();
    }

    /**
     *
     * @throws Exception
     */
    private void testControlNodes() throws Exception {
        ResultSet controlNodes = getDbEntries("controlnode");
        while (controlNodes.next()) {
            DbControlNode controlNode = new DbControlNode(
                    controlNodes.getInt("id"),
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
            Assert.assertTrue("ControlNodeID smaller than 1", controlNode.id > 0);
        }
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode(1, "", "Startevent", fragments.get("FragmentA").id, 1069345757L)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode(2, "Ref", "Activity", fragments.get("FragmentA").id, 8267903230L)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode(3, "A1", "WebServiceTask", fragments.get("FragmentA").id, 517729148L)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode(4, "", "AND", fragments.get("FragmentA").id, 1569336784L)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode(5, "", "AND", fragments.get("FragmentA").id, 2081480666L)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode(6, "", "Endevent", fragments.get("FragmentA").id, 1914825610L)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode(9, "Ref", "Activity", fragments.get("FragmentB").id, 8267903231L)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode(7, "", "XOR", fragments.get("FragmentB").id, 564114893L)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode(8, "", "Endevent", fragments.get("FragmentB").id, 2136115766L)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode(10, "", "XOR", fragments.get("FragmentB").id, 1689490932L)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode(11, "B2", "EmailTask", fragments.get("FragmentB").id, 1821614206)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode(12, "B1", "Activity", fragments.get("FragmentB").id, 1407636184)));
        Assert.assertTrue("ControlNode in table 'controlnode' not inserted correctly", this.controlNodes.values().contains(new DbControlNode(13, "", "Startevent", fragments.get("FragmentB").id, 509231813L)));
        Assert.assertTrue("Too many controlNodes have been inserted", this.controlNodes.size() == 13);
        controlNodes.close();
    }

    /**
     *
     * @throws Exception
     */
    private void testDataSetAndDataFlow() throws Exception {
        ResultSet dataSets = getDataSetJoins();
        while (dataSets.next()) {
            DbDataSetAndFlow set = new DbDataSetAndFlow(
                    dataSets.getInt("dataset.id"),
                    dataSets.getInt("dataflow.controlnode_id"),
                    dataSets.getShort("dataflow.input"),
                    dataSets.getShort("dataset.input"),
                    dataSets.getInt("datasetconsistsofdatanode.datanode_id"));
            this.sets.add(set);
            Assert.assertTrue("DataSetID smaller than 1", set.dataSetID > 0);
        }
        Assert.assertTrue("DataSet in table 'dataset', 'dataflow' or 'datasetconsistsofdatanode' not inserted correctly", this.sets.contains(new DbDataSetAndFlow(1, controlNodes.get(517729148L).id, (short)1, (short)1, dataNodes.get(135409402L).id)));
        Assert.assertTrue("DataSet in table 'dataset', 'dataflow' or 'datasetconsistsofdatanode' not inserted correctly", this.sets.contains(new DbDataSetAndFlow(2, controlNodes.get(517729148L).id, (short)0, (short)0, dataNodes.get(650069438L).id)));
        Assert.assertTrue("DataSet in table 'dataset', 'dataflow' or 'datasetconsistsofdatanode' not inserted correctly", this.sets.contains(new DbDataSetAndFlow(3, controlNodes.get(8267903231L).id, (short)1, (short)1, dataNodes.get(611573211L).id)));
        Assert.assertTrue("DataSet in table 'dataset', 'dataflow' or 'datasetconsistsofdatanode' not inserted correctly", this.sets.contains(new DbDataSetAndFlow(4, controlNodes.get(1407636184L).id, (short)1, (short)1, dataNodes.get(1368161079L).id)));
        Assert.assertTrue("DataSet in table 'dataset', 'dataflow' or 'datasetconsistsofdatanode' not inserted correctly", this.sets.contains(new DbDataSetAndFlow(5, controlNodes.get(1821614206L).id, (short)0, (short)0, dataNodes.get(155099451L).id)));
        Assert.assertTrue("DataSet in table 'dataset', 'dataflow' or 'datasetconsistsofdatanode' not inserted correctly", this.sets.contains(new DbDataSetAndFlow(6, controlNodes.get(1407636184L).id, (short)0, (short)0, dataNodes.get(1517694277L).id)));
        Assert.assertTrue("Too many datasets in table 'dataset', 'dataflow' or 'datasetconsistsofdatanode' have been inserted", this.sets.size() == 6);

        dataSets.close();
    }

    /**
     *
     * @throws Exception
     */
    private void testControlFlow() throws Exception {
        ResultSet controlFlows = getDbEntries("controlflow");
        while (controlFlows.next()) {
            DbControlFlow flow = new DbControlFlow(
                    controlFlows.getInt("controlnode_id1"),
                    controlFlows.getInt("controlnode_id2"),
                    controlFlows.getString("condition"));
            this.controlFlows.add(flow);
        }
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(controlNodes.get(1069345757L).id, controlNodes.get(1569336784L).id, "")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(controlNodes.get(1569336784L).id, controlNodes.get(8267903230L).id, "")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(controlNodes.get(1569336784L).id, controlNodes.get(517729148L).id, "")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(controlNodes.get(517729148L).id, controlNodes.get(2081480666L).id, "")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(controlNodes.get(8267903230L).id, controlNodes.get(2081480666L).id, "")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(controlNodes.get(2081480666L).id, controlNodes.get(1914825610L).id, "")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(controlNodes.get(1689490932L).id, controlNodes.get(1821614206L).id, "DEFAULT")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(controlNodes.get(1821614206L).id, controlNodes.get(564114893L).id, "")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(controlNodes.get(564114893L).id, controlNodes.get(2136115766L).id, "")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(controlNodes.get(509231813L).id, controlNodes.get(8267903231L).id, "")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(controlNodes.get(8267903231L).id, controlNodes.get(1689490932L).id, "")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(controlNodes.get(1689490932L).id, controlNodes.get(1407636184L).id, "DO.Attr1 > 0")));
        Assert.assertTrue("Controlflow in table 'controlfow' not inserted correctly", this.controlFlows.contains(new DbControlFlow(controlNodes.get(1407636184L).id, controlNodes.get(564114893L).id, "")));
        Assert.assertTrue("Too many controlfows in table 'controlflow' have been inserted", this.controlFlows.size() == 13);
        controlFlows.close();
    }

    /**
     *
     * @throws Exception
     */
    private void testReferences() throws Exception {
        ResultSet references = getDbEntries("reference");
        while (references.next()) {
            DbReference reference = new DbReference(
                    references.getInt("controlnode_id1"),
                    references.getInt("controlnode_id2"));
            this.references.add(reference);
        }
        Assert.assertTrue("Reference in table 'reference' not inserted correctly", this.references.contains(new DbReference(controlNodes.get(8267903230L).id, controlNodes.get(8267903231L).id)));
        Assert.assertTrue("Reference in table 'reference' not inserted correctly", this.references.contains(new DbReference(controlNodes.get(8267903231L).id, controlNodes.get(8267903230L).id)));
        Assert.assertTrue("Too many references in table 'reference' have been inserted", this.references.size() == 2);
        references.close();
    }

    /**
     *
     * @throws Exception
     */
    private void testTerminationCondition() throws Exception {
        ResultSet terminationCondition = getDbEntries("terminationcondition");
        while (terminationCondition.next()) {
            DbTerminationCondition tc = new DbTerminationCondition(
                    terminationCondition.getInt("conditionset_id"),
                    terminationCondition.getInt("dataobject_id"),
                    terminationCondition.getInt("state_id"),
                    terminationCondition.getInt("scenario_id"));
            this.terminationCondition.add(tc);
        }
        int end = 0, state1 = 0, initSubDO = 0;
        for (DbState state : states) {
            if (state.name.equals("init") && state.dataClassID == dataClasses.get("SubD").id)
                initSubDO = state.id;
            else if (state.name.equals("end") && state.dataClassID == dataClasses.get("SubD").id)
                end = state.id;
            else if (state.name.equals("state1") && state.dataClassID == dataClasses.get("DO").id)
                state1 = state.id;
        }
        Assert.assertEquals("TerminationCondition is not set correctly", 3, this.terminationCondition.size());
        Assert.assertTrue("TerminationCondition is not set correctly", this.terminationCondition.contains(new DbTerminationCondition(1, dataObjects.get("SubDO").id, end, scenarios.get(0).id)));Assert.assertTrue("TerminationCondition is not set correctly", this.terminationCondition.contains(new DbTerminationCondition(1, dataObjects.get("DO").id, state1, scenarios.get(0).id)));
        Assert.assertTrue("TerminationCondition is not set correctly", this.terminationCondition.contains(new DbTerminationCondition(1, dataObjects.get("SubDO").id, end, scenarios.get(0).id)));Assert.assertTrue("TerminationCondition is not set correctly", this.terminationCondition.contains(new DbTerminationCondition(1, dataObjects.get("SubDO").id, end, scenarios.get(0).id)));
        Assert.assertTrue("TerminationCondition is not set correctly", this.terminationCondition.contains(new DbTerminationCondition(1, dataObjects.get("SubDO").id, end, scenarios.get(0).id)));Assert.assertTrue("TerminationCondition is not set correctly", this.terminationCondition.contains(new DbTerminationCondition(2, dataObjects.get("SubDO").id, initSubDO, scenarios.get(0).id)));
        terminationCondition.close();
    }

    /**
     *
     * @param tablename
     * @return
     */
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

    /**
     *
     * @return
     */
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
