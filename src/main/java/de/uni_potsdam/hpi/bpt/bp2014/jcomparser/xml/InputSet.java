package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;

import java.util.*;

/**
 * A class which represents an InputSet.
 */
public class InputSet extends Set implements IPersistable {

    /**
     * Creates all new InputSets for a task and a list of edges
     *
     * @param task  The task which has an input set
     * @param edges The list of all edges that are part of the fragment the task belongs to
     *              (the list has to contain at least all the ingoing associations of the task)
     * @return The newly created InputSets or null if no ingoing (DataFlow)-Associations were found.
     */
    public static List<InputSet> createInputSetForTaskAndEdges(
        final Node task,
        final List<Edge> edges) {
        List<Edge> associations = new LinkedList<>();
        for (Edge edge : edges) {
            if (edge.getTargetNodeId() == task.getId()
                    && edge.getType().contains("Association")) {
                associations.add(edge);
            }
        }
        if (associations.isEmpty()) {
            return null;
        }
        Map<String, List<Edge>> orderedAssociations = new HashMap<>();
        for (Edge edge : associations) {
            String sourceNodeLabel = edge.getSource().getText();
            if (orderedAssociations.get(sourceNodeLabel) == null) {
                List<Edge> value = new LinkedList<>();
                value.add(edge);
                orderedAssociations.put(sourceNodeLabel, value);
            }
            else {
                orderedAssociations.get(sourceNodeLabel).add(edge);
            }
        }
        List<List<Edge>> cartProd = cartesianProduct(orderedAssociations);
        List<InputSet> inputSets = new LinkedList<>();
        for (List<Edge> edgeSet : cartProd) {
            InputSet instance = new InputSet();
            instance.associations = edgeSet;
            instance.dataObjects = new LinkedList<>();
            instance.node = task;
            for (Edge e : edgeSet) {
                instance.dataObjects.add(e.getSource());
            }
            inputSets.add(instance);
        }
        return inputSets;
    }

    @Override
    public int save() {
        Connector connector = new Connector();
        databaseId = connector.insertDataSetIntoDatabase(true);
        updateEdges();
        return databaseId;
    }
}
