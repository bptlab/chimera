package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;

import java.util.LinkedList;
import java.util.List;

/**
 * A class which represents a InputSet
 */
public class InputSet implements IPersistable {
    private List<Edge> associations;
    private List<Node> inputs;
    // The task which has the input set
    private Node consumer;
    private int databaseId;

    public static InputSet createInputSetForTaskAndEdges(Node task, List<Edge> edges) {
        InputSet instance = new InputSet();
        instance.associations = new LinkedList<Edge>();
        instance.inputs = new LinkedList<Node>();
        instance.consumer = task;
        for (Edge edge : edges) {
            if (edge.getTargetNodeId() == instance.consumer.getId() && edge.getType().contains("Association")) {
                instance.associations.add(edge);
                instance.inputs.add(edge.getSource());
            }
        }
        if (instance.inputs.isEmpty()) {
            return null;
        }
        return instance;
    }

    @Override
    public int save() {
        Connector connector = new Connector();
        databaseId = connector.insertDataSetIntoDatabase(true);
        updateEdges();
        return databaseId;
    }

    private void updateEdges() {
        for (Edge edge : associations) {
            edge.setSetId(databaseId);
        }
    }

    public int getDatabaseId() {
        return databaseId;
    }

    public List<Node> getInputs() {
        return inputs;
    }
}
