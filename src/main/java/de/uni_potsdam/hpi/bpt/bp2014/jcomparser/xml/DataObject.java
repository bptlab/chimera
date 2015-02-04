package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class represents a DataObject. A DataObject is an entity which is shown inside the Scneario and represented
 * by one or more DataNodes inside all of the Fragments.
 */
public class DataObject implements IPersistable {

    // A list of all dataNodes which represent the DataObject
    private List<Node> dataNodes;
    // Saves all states with their name (key) and their databaseId (value)
    private Map<String, Integer> states;
    // The database Id of the data class related to this data Object
    private int classId = -1;
    // The database Id of the scenario
    private int scenarioId = -1;
    // The database ID of the data Object
    private int databaseId;
    // Teh database ID of the initial State
    private Integer initState;

    /**
     * Creates a new DataObject with an empty set of states and an empty set of nodes.
     */
    public DataObject() {
        dataNodes = new LinkedList<Node>();
        states = new HashMap<String, Integer>();
    }

    /**
     * Creates a new DataObject with a given set of nodes. The states will be extracted and added automatically.
     * @param dataNodes the initial set of nodes. All nodes may have the type DataObject.
     */
    public DataObject(List<Node> dataNodes) {
        this.dataNodes = new LinkedList<Node>(dataNodes);
        initializeStates();
    }

    /**
     * This method extracts all the dataStates from the added DataNodes.
     */
    private void initializeStates() {
        Connector connector = new Connector();
        classId = connector.insertDataClassIntoDatabase(dataNodes.get(0).getText());
        for (Node dataNode : dataNodes) {
            if (!states.containsKey(dataNode.getState())) {
                int stateId = connector.insertStateIntoDatabase(dataNode.getState(), classId);
                states.put(dataNode.getState(), stateId);
            }
        }
    }

    /**
     * Adds a new dataNode to the dataObject
     * If the state has not been written to the database it will be added
     *
     * @param dataNode the new node which will be added
     */
    public void addDataNode(Node dataNode) {
        if (dataNode.isDataNode()) {
            dataNodes.add(dataNode);
        }
        addState(dataNode.getState());
    }

    /**
     * Adds a state to the list of states. If there is no class associated with the data Object a new "Dummy"-Class will
     * be created
     *
     * @param state - The name of the state which will be added to the States of the DataObject
     */
    private void addState(String state) {
        Connector connector = new Connector();
        if (0 >= classId) {
            classId = connector.insertDataClassIntoDatabase(dataNodes.get(0).getText());
        }
        if (!states.containsKey(state)) {
            int stateId = connector.insertStateIntoDatabase(state, classId);
            states.put(state, stateId);
        }
    }

    @Override
    public int save() {
        if (0 >= scenarioId || 0 >= classId) {
            return -1;
        }
        Connector connector = new Connector();
        // We assume, that every DataObject starts with the state "init"
        initState = new Integer(states.get("init"));
        databaseId = connector.insertDataObjectIntoDatabase(dataNodes.get(0).getText(), classId, scenarioId, initState);
        saveDataNodes();
        return databaseId;
    }

    /**
     * Saves the data Nodes to the database. Also the databaseID will be set for each node.
     *
     */
    private void saveDataNodes() {
        Connector connector = new Connector();
        for (Node dataNode : dataNodes) {
            int nodeId = connector.insertDataNodeIntoDatabase(
                    scenarioId,
                    states.get(dataNode.getState()),
                    classId,
                    initState);
            dataNode.setDatabaseID(nodeId);
        }
    }

    public int getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }

    /**
     * Returnes the list of dataNodes. Be aware that it is no copy. (It is more a composition than an aggregation)
     * This means, if you change the list you change the dataObject
     *
     * @return the list of DataNodes inside the DataObject
     */
    public List<Node> getDataNodes() {
        return dataNodes;
    }

    /**
     * Returns the databaseID of the inital State. (We assume that the initial State is ("init")
     *
     * @return
     */
    public Integer getInitState() {
        return initState;
    }
}
