package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataObject implements IPersistable {
    // A list of all dataNodes which represent the DataObject
    private List<ControlNode> dataNodes;
    // Saves all states with their name (key) and their databaseId (value)
    private Map<String, Integer> states;
    // The database Id of the data class related to this data Object
    private int classId = -1;
    // The database Id of the scenario
    private int scenarioid = -1;
    // The database Id of the DataObject
    private int databaseId;

    public DataObject() {
        dataNodes = new LinkedList<ControlNode>();
        states = new HashMap<String, Integer>();
    }

    public DataObject(List<ControlNode> dataNodes) {
        this.dataNodes = new LinkedList<ControlNode>(dataNodes);
        initializeStates();
    }

    private void initializeStates() {
        Connector connector = new Connector();
        classId = connector.insertDataClassIntoDatabase(dataNodes.get(0).getText());
        for (ControlNode dataNode : dataNodes) {
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
    public void addDataNode(ControlNode dataNode) {
        if (dataNode.isDataNode()) {
            dataNodes.add(dataNode);
        }
        addState(dataNode.getState());
    }

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
    public int writeToDatabase() {
        if (0 >= scenarioid || 0 >= classId) {
            return -1;
        }
        Connector connector = new Connector();
        // We assume, that every DataObject starts with the state "init"
        databaseId = connector.insertDataObjectIntoDatabase(dataNodes.get(0).getText(), classId, scenarioid, states.get("init"));
        return 0;
    }

    public int getScenarioid() {
        return scenarioid;
    }

    public void setScenarioid(int scenarioid) {
        this.scenarioid = scenarioid;
    }
}
