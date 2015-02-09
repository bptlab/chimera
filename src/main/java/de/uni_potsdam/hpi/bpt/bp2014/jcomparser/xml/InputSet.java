package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;

import java.util.LinkedList;
import java.util.List;

/**
 * A class which represents an InputSet.
 */
public class InputSet implements IPersistable {
    /**
     * A List of all (DataFlow-) Edges.
     * The edges have any of the (Data-) Nodes of the
     * Input-Set as source and the activity of the InputSet as the Target
     */
    private List<Edge> associations;
    /**
     * All DataObject which are part of this InputSet
     */
    private List<Node> inputs;
    /**
     * The Activity (Node) which has this InputSet
     */
    private Node consumer;
    /**
     * The databaseID of the InputSet
     */
    private int databaseId;

    /**
     * creates a new InputSet for a Task and a List of edges
     *
     * @param task  The task which has an input set
     * @param edges The List of all edges of the control Flow
     * @return The newly Created InputSet
     */
    public static InputSet createInputSetForTaskAndEdges(
            final Node task,
            final List<Edge> edges) {
        InputSet instance = new InputSet();
        instance.associations = new LinkedList<Edge>();
        instance.inputs = new LinkedList<Node>();
        instance.consumer = task;
        for (Edge edge : edges) {
            if (edge.getTargetNodeId() == instance.consumer.getId()
                    && edge.getType().contains("Association")) {
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

    /**
     * Adds the databaseId of the Set to the edge.
     * It is necessary towrite it to the Database.
     */
    private void updateEdges() {
        for (Edge edge : associations) {
            edge.setSetId(databaseId);
        }
    }

    /**
     * Returns the Database Id of the Input Set.
     *
     * @return the Database Id
     */
    public int getDatabaseId() {
        return databaseId;
    }


    /**
     * Returns the list of Inputs.
     * The Inputs are DataNodes. It is not a copy.
     * This means changes will affect the state of the InputSet.
     *
     * @return the list of data nodes which are part of the InputSet
     */
    public List<Node> getInputs() {
        return inputs;
    }

    public Node getConsumer() {
        return consumer;
    }

    public List<Edge> getAssociations() {
        return associations;
    }
}
