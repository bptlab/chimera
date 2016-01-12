package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.Edge;

import java.util.List;

/**
 * This class represents a set.
 */
public class Set implements IPersistable {
    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    /**
	 * A List of all (DataFlow-) Edges.
	 * The edges have any of the (Data-) Nodes of the
	 * Input-Set as source and the activity of the InputSet as the Target
	 */
	private List<Edge> associations;
	/**
	 * All DataNodes which are part of this Set.
	 */
	private List<Node> dataNodes;
	/**
	 * The Activity (Node) which has this set.
	 */
	private Node node;
	/**
	 * The databaseID of the InputSet.
	 */
	private int databaseId;

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

	public void setDataNodes(List<Node> dataNodes) {
		this.dataNodes = dataNodes;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public List<Edge> getAssociations() {
		return associations;
	}

	public void setAssociations(List<Edge> associations) {
		this.associations = associations;
	}

	@Override public int save() {
		Connector connector = new Connector();
		databaseId = connector.insertDataSetIntoDatabase(this.getClass().getName()
				.equals("de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.InputSet"));
		updateEdges();
		return databaseId;
	}


}
