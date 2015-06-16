package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class represents a DataObject.
 * A DataObject is an entity which is shown inside the Scenario
 * and represented by one or more DataNodes inside all of the Fragments.
 */
public class DataObject implements IPersistable {

    /**
     * A list of all dataNodes which represent the DataObject.
     */
    private List<Node> dataNodes;
    /**
     * Saves all states with their name (key) and their databaseId (value).
     */
    private Map<String, Integer> states;
    /**
     * The database Id of the data class related to this data Object.
     */
    private int classId = -1;
    /**
     * The database Id of the scenario.
     */
    private int scenarioId = -1;
    /**
     * The database ID of the data Object.
     */
    private int databaseId;
    /**
     * The database ID of the initial State.
     */
    private Integer initState;
    /**
     * This is the dataClass belonging to this dataObject.
     */
    private DataClass dataClass;

    /**
     * Creates a new DataObject with a given dataClass.
     *
     * @param dataClass The dataClass the dataObject belongs to
     */
    public DataObject(final DataClass dataClass) {
        dataNodes = new LinkedList<Node>();
        this.dataClass = dataClass;
        states = new HashMap<String, Integer>();
    }

    /**
     * Creates a new DataObject without a given dataClass.
     *
     */
    public DataObject() {
        dataNodes = new LinkedList<Node>();
        states = new HashMap<String, Integer>();
    }

    /**
     * Adds a new dataNode to the dataObject.
     * If the state has not been written to the database it will be added
     *
     * @param dataNode the new node which will be added
     */
    public void addDataNode(final Node dataNode) {
        if (dataNode.isDataNode()) {
            dataNodes.add(dataNode);
        }
        addState(dataNode.getState());
    }

    /**
     * Adds a state to the list of states.
     * As there is no class associated with the DataObject,
     * a new "Dummy"-Class will be created
     *
     * @param state - The name of the state,
     *              which will be added to the States of the DataObject
     */
    private void addState(final String state) {
        Connector connector = new Connector();
        if (!states.containsKey(state)) {
            states.put(state, -1);
        }
    }

    @Override
    public int save() {
        if (0 >= scenarioId) {
            return -1;
        }
        Connector connector = new Connector();
        for (String state : states.keySet()) {
            int stateID = connector.insertStateIntoDatabase(state, dataClass.getDataClassID());
            states.put(state, stateID);
        }
        initState = states.get("init");
        String dataObjectName = dataNodes.get(0).getText();
        databaseId = connector.insertDataObjectIntoDatabase(dataObjectName,
                dataClass.getDataClassID(),
                scenarioId,
                initState);
        saveDataNodes();
        return databaseId;
    }

    /**
     * Saves the data Nodes to the database.
     * Also the databaseID will be set for each node.
     */
    private void saveDataNodes() {
        Connector connector = new Connector();
        for (Node dataNode : dataNodes) {
            int nodeId = connector.insertDataNodeIntoDatabase(
                    scenarioId,
                    states.get(dataNode.getState()),
                    dataClass.getDataClassID(),
                    databaseId,
                    dataNode.getId());
            dataNode.setDatabaseID(nodeId);
        }
    }

    /**
     * Sets the scenario id.
     *
     * @param newScenarioId Should be the database Id of the Scenario.
     */
    public void setScenarioId(final int newScenarioId) {
        this.scenarioId = newScenarioId;
    }

    /**
     * Returns the list of dataNodes.
     * Be aware that it is no copy.
     * (It is more a composition than an aggregation.
     * This means, if you change the list you change the dataObject)
     *
     * @return the list of DataNodes inside the DataObject
     */
    public List<Node> getDataNodes() {
        return dataNodes;
    }

    /**
     * Returns the databaseID of the initial State.
     * (We assume that the initial State is ("init").
     *
     * @return the databaseId of the state "init".
     */
    public Integer getInitState() {
        return initState;
    }

    /**
     * Returns the database id.
     *
     * @return the id (int) which is primary key inside the database.
     */
    public int getDatabaseId() {
        return databaseId;
    }

    /**
     * Returns the map of states.
     * The map contains all states with their name (key)
     * and their Id (value).
     * Any changes will affect the state of the DataObject.
     *
     * @return the map of states.
     */
    public Map<String, Integer> getStates() {
        return states;
    }
}
