package de.uni_potsdam.hpi.bpt.bp2014.database.data;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataConditions;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class is the representation of a dataNode in the database.
 * It provides the functionality to get all dataObjects
 * belonging to a dataSet as well as the states they can have.
 */
public class DbDataNode extends DbObject {
	private static Logger log = Logger.getLogger(DbDataNode.class);

	/**
	 * This method returns all database ID's of all dataObjects belonging to a dataSet.
	 *
	 * @param dataSetId This is the database ID of a dataSet.
	 * @return a list of database ID's of all dataObjects belonging to this dataSet.
	 */
	public LinkedList<Integer> getDataObjectIdsForDataSets(int dataSetId) {
		String sql =
				"Select dataobject_id FROM datanode, datasetconsistsofdatanode "
						+ "WHERE datanode.id = "
						+ "datasetconsistsofdatanode.datanode_id "
						+ "AND dataset_id = " + dataSetId
						+ " ORDER BY dataobject_id";
		return this.executeStatementReturnsListInt(sql, "dataobject_id");
	}

	/**
	 * This method returns all database ID's for all states a dataSet can have.
	 *
	 * @param dataSetId This is the database ID of a dataSet.
	 * @return a list of all database ID's of all states this dataSet can have.
	 */
	public LinkedList<Integer> getDataStatesForDataSets(int dataSetId) {
		String sql =
				"Select state_id FROM datanode, datasetconsistsofdatanode "
						+ "WHERE datanode.id = "
						+ "datasetconsistsofdatanode.datanode_id "
						+ "AND dataset_id = " + dataSetId
						+ " ORDER BY dataobject_id";
		return this.executeStatementReturnsListInt(sql, "state_id");
	}

    public DataConditions getInputSetFor(int inputSetId) {
        throw new RuntimeException("To be implemented");
    }

    public int getDataClassIdForDataNode(int dataNodeId) {
        String sql = "SELECT dataclass_id FROM datanode " +
                "WHERE datanode.id = %d";
        return this.executeStatementReturnsInt(String.format(sql, dataNodeId), "dataclass_id");
    }


    public List<Integer> getDataobjectIdsForSet(int dataSetId) {
        String sql =
                "SELECT dataobject_id, state_id "
                        + "FROM datanode, datasetconsistsofdatanode "
                        + "WHERE datanode.id = "
                        + "datasetconsistsofdatanode.datanode_id "
                        + "AND dataset_id = " + dataSetId
                        + " ORDER BY dataobject_id";
        return this.executeStatementReturnsListInt(sql, "dataobject_id");
    }

	public Map<Integer, Integer> getDataObjectIdToState(int dataSetId) {
		String sql = "SELECT dataobject_id, state_id "
						+ "FROM datanode, datasetconsistsofdatanode "
						+ "WHERE datanode.id = "
						+ "datasetconsistsofdatanode.datanode_id "
						+ "AND dataset_id = " + dataSetId
						+ " ORDER BY dataobject_id";
		return executeStatementReturnsMapIntInt(sql, "dataobject_id", "state_id");
    }
}
