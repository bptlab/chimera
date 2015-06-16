package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;

import java.util.*;

public class Set implements IPersistable{
    /**
     * A List of all (DataFlow-) Edges.
     * The edges have any of the (Data-) Nodes of the
     * Input-Set as source and the activity of the InputSet as the Target
     */
    public List<Edge> associations;
    /**
     * All DataNodes which are part of this Set
     */
    public List<Node> dataNodes;
    /**
     * The Activity (Node) which has this set
     */
    public Node node;
    /**
     * The databaseID of the InputSet
     */
    public int databaseId;

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
    public List<Node> getDataNodes() {
        return dataNodes;
    }

    public Node getNode() {
        return node;
    }

    public List<Edge> getAssociations() {
        return associations;
    }

    @Override
    public int save() {
        Connector connector = new Connector();
        databaseId = connector.insertDataSetIntoDatabase(this.getClass().getName().equals("de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.InputSet"));
        updateEdges();
        return databaseId;
    }
}
