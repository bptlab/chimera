package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;

import java.util.*;

public class Set {
    /**
     * A List of all (DataFlow-) Edges.
     * The edges have any of the (Data-) Nodes of the
     * Input-Set as source and the activity of the InputSet as the Target
     */
    public List<Edge> associations;
    /**
     * All DataObject which are part of this Set
     */
    public List<Node> dataObjects;
    /**
     * The Activity (Node) which has this set
     */
    public Node node;
    /**
     * The databaseID of the InputSet
     */
    public int databaseId;


    // according to following link: http://www.java-forum.org/mathematik/67182-kartesisches-produkt-algorithmus.html
    public static List<List<Edge>> cartesianProduct(Map<String, List<Edge>> sets) {
        List<List<Edge>> result = new ArrayList<>();
        List<Edge> cartesianProductElement;
        int n = 1; // cardinality of the cartesian product
        Iterator<?> setIterator = sets.values().iterator();
        // loop to get cardinality n of the Cartesian product
        while (setIterator.hasNext()) {
            List<Edge> set = (List<Edge>) setIterator.next();
            n*= set.size();
        }
        //loop to create all elements of Cartesian product
        for (int i = 0, j = 1; i < n; i++){
            cartesianProductElement = new ArrayList<>();
            setIterator = sets.values().iterator();
            // loop that collects one element of each class
            while (setIterator.hasNext()) {
                List<Edge> set = (List<Edge>) setIterator.next();
                cartesianProductElement.add(set.get((i/j) % set.size()));
                j*= set.size();
            }
            result.add(cartesianProductElement);
        }
        return result;
    }



    /**
     * Adds the databaseId of the Set to the edge.
     * It is necessary towrite it to the Database.
     */
    public void updateEdges() {
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
    public List<Node> getDataObjects() {
        return dataObjects;
    }

    public Node getNode() {
        return node;
    }

    public List<Edge> getAssociations() {
        return associations;
    }
}
