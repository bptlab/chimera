package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import jersey.repackaged.com.google.common.collect.Sets;

import java.util.*;

public class OutputSet extends Set implements IPersistable {

    /**
     * Creates an Output Set for the given Node (task) and the given Edges (edges). The edges should at least contain
     * all outgoing associations of the task, but can contain more edges.
     *
     * @param task  The task which has an output set
     * @param edges The list of all edges that are part of the fragment the task belongs to
     *              (the list has to contain at least all the outgoing associations of the task)
     * @return The newly created OutputSets or null if no outgoing (DataFlow)-Associations were found.
     */
    public static List<OutputSet> createOutputSetForTaskAndEdges(
            final Node task,
            final List<Edge> edges) {
        List<Edge> associations = new LinkedList<>();
        for (Edge edge : edges) {
            if (edge.getSourceNodeId() == task.getId()
                    && edge.getType().contains("Association")) {
                associations.add(edge);
            }
        }
        if (associations.isEmpty()) {
            return null;
        }
        Map<String, java.util.Set<Edge>> orderedAssociations = new HashMap<>();
        for (Edge edge : associations) {
            String targetNodeLabel = edge.getTarget().getText();
            if (orderedAssociations.get(targetNodeLabel) == null) {
                java.util.Set<Edge> value = new HashSet<>();
                value.add(edge);
                orderedAssociations.put(targetNodeLabel, value);
            } else {
                orderedAssociations.get(targetNodeLabel).add(edge);
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
        List<OutputSet> outputSets = new LinkedList<>();
        for (List<Edge> edgeSet : cartProd) {
            OutputSet instance = new OutputSet();
            instance.associations = edgeSet;
            instance.dataNodes = new LinkedList<>();
            instance.node = task;
            for (Edge e : edgeSet) {
                instance.dataNodes.add(e.getTarget());
            }
            outputSets.add(instance);
        }
        return outputSets;
    }
}
