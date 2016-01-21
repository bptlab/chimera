package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.AbstractControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataNode;

import java.util.List;

/**
 * This class represents a set.
 */
public class Set implements IPersistable {
    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }


    /**
	 * All DataNodes which are part of this Set.
	 */
	protected List<DataNode> dataNodes;
	/**
	 * The Activity (Node) which has this set.
	 */
	protected AbstractControlNode controlNode;
	/**
	 * The databaseID of the InputSet.
	 */
    protected int databaseId;


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

	@Override public int save() {
		Connector connector = new Connector();
		databaseId = connector.insertDataSetIntoDatabase(this.getClass().getName()
				.equals("de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.InputSet"));
		return databaseId;
	}


}
