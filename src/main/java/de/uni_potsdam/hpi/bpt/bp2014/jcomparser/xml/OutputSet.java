package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;

import java.util.LinkedList;
import java.util.List;

public class OutputSet implements IPersistable {
    // The list of (Associations-)Edges which have a (DataObject-) Node as the target and a (Control) Node as the source.
    private List<Edge> associations;
    // The (DataObjects) which are part of the DataNode
    private List<Node> outputs;
    // The task which has the output set
    private Node producer;
    // The Database ID of the OutputSet
    private int databaseId = - 1;

    /**
     * Creates an Output Set for the given Node (task) and the given Edges (edges). The edges should at least contain
     * all outgoing associations of the task, but can contain more edges.
     * @param task - The (Task)Node which has the OutputSet
     * @param edges - The List of Edges
     * @return the newly created OutputSet or null if now Outgoing (DataFlow)-Association was found.
     */
    public static OutputSet createOutputSetForTaskAndEdges(Node task, List<Edge> edges) {
        OutputSet instance = new OutputSet();
        instance.associations = new LinkedList<Edge>();
        instance.outputs = new LinkedList<Node>();
        instance.producer = task;
        for (Edge edge : edges) {
            if (edge.getSourceNodeId() == instance.producer.getId() && edge.getType().contains("Association")) {
                instance.associations.add(edge);
                instance.outputs.add(edge.getTarget());
            }
        }
        if (instance.outputs.isEmpty()) {
            return null;
        }
        return instance;
    }

    @Override
    public int save() {
        Connector connector = new Connector();
        databaseId = connector.insertDataSetIntoDatabase(false);
        updateEdges();
        return databaseId;
    }

    /**
     * Adds the Database ID of the Set to the edges, so that the edges can be saved to the database.
     * Assert that the OutputSet has been written to the Database.
     */
    private void updateEdges() {
        for (Edge edge : associations) {
            edge.setSetId(databaseId);
        }
    }

    public int getDatabaseId() {
        return databaseId;
    }

    public List<Node> getOutputs() {
        return outputs;
    }

    public Node getProducer() {
        return producer;
    }

    public List<Edge> getAssociations() {
        return associations;
    }
}
