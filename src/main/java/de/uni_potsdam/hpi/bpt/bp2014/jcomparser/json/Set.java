package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.AbstractControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataNode;

import java.util.List;

/**
 * This class represents a set.
 */
public abstract class Set implements IPersistable {
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
    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }
}
