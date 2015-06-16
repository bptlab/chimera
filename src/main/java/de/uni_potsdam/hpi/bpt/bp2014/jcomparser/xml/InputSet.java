package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import jersey.repackaged.com.google.common.collect.Sets;

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
        Map<String, java.util.Set<Edge>> orderedAssociations = new HashMap<>();
        for (Edge edge : associations) {
            String sourceNodeLabel = edge.getSource().getText();
            if (orderedAssociations.get(sourceNodeLabel) == null) {
                java.util.Set<Edge> value = new java.util.HashSet<>();
                value.add(edge);
                orderedAssociations.put(sourceNodeLabel, value);
            } else {
                orderedAssociations.get(sourceNodeLabel).add(edge);
            }
        }
        // convert orderedAssociations to appropriate form for calculating the cartesian product (convert Map<String, Set> to List<Set>)
        List<java.util.Set<Edge>> convertedAssociations = new LinkedList<>();
        for (java.util.Set<Edge> coll : orderedAssociations.values()) {
            java.util.Set<Edge> hashSet = new HashSet<>();
            hashSet.addAll(coll);
            convertedAssociations.add(hashSet);
        }
        java.util.Set<List<Edge>> cartProd = Sets.cartesianProduct(convertedAssociations);
        List<InputSet> inputSets = new LinkedList<>();
        for (List<Edge> edgeSet : cartProd) {
            InputSet instance = new InputSet();
            instance.associations = edgeSet;
            instance.dataNodes = new LinkedList<>();
            instance.node = task;
            for (Edge e : edgeSet) {
                instance.dataNodes.add(e.getSource());
            }
            inputSets.add(instance);
        }
        return inputSets;
    }
}
