package de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes;

import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is the representation of a controlNode in the database.
 * It provides the functionality to get the startEvent of a fragment as well as the type and
 * label of a controlNode.
 * Moreover it is possible to check if two controlNodes have the same output.
 */
public class DbControlNode extends DbObject {
	private static Logger log = Logger.getLogger(DbControlNode.class);
	private String type = null;
	private String label = null;
	private int controlNodeId = -1;

	/**
	 * This method returns the database Id of a startEvent in the context of a fragment.
	 *
	 * @param fragmentId This is the database ID of a fragment.
	 * @return -1 if something went wrong else the database ID of the startEvent.
	 */
	public int getStartEventID(int fragmentId) {
		String sql = "SELECT id FROM controlnode "
				+ "WHERE type = 'StartEvent' "
				+ "AND fragment_id = " + fragmentId;
		log.info(sql);
		return this.executeStatementReturnsInt(sql, "id");
	}

    /**
     *
     * @param controlNodeId Id of the control node
     * @return fragment id the control node belongs to.
     */
    public int getFragmentId(int controlNodeId) {
        String sql = "SELECT * FROM controlnode WHERE id = " + controlNodeId;
        return this.executeStatementReturnsInt(sql, "fragment_id");
    }

	/**
	 * This method returns the type of a controlNode.
	 *
	 * @param controlNodeId This is the database ID of a controlNode.
	 * @return the type of the controlNode as a String.
	 */
	public String getType(int controlNodeId) {
		if (type == null || this.controlNodeId != controlNodeId) {
			String sql = "SELECT type FROM controlnode WHERE id = " + controlNodeId;
			log.info(sql);
			type = this.executeStatementReturnsString(sql, "type");
		} else {
			log.info("Used cached type instead of querying "
					+ "the database for contolNodeId=" + controlNodeId);
		}
		return type;
	}

	/**
	 * This method returns the name of a controlNode.
	 *
	 * @param controlNodeId This is the database ID of a controlNode.
	 * @return the label of the controlNode as a String.
	 */
	public String getLabel(int controlNodeId) {
		if (label == null || this.controlNodeId != controlNodeId) {
			String sql = "SELECT label FROM controlnode WHERE id = " + controlNodeId;
			log.info(sql);
			label = this.executeStatementReturnsString(sql, "label");
			this.controlNodeId = controlNodeId;
		}
		return label;
	}

	/**
	 * This method checks if 2 controlNodes have the same output
	 * (mostly used for shared activities).
	 *
	 * @param controlNodeId1 This is the database ID of the first controlNode.
	 * @param controlNodeId2 This is the database ID of the second controlNode.
	 * @return true if they have the same output else false.
	 */
	public Boolean controlNodesHaveSameOutputs(int controlNodeId1, int controlNodeId2) {
		java.sql.Connection conn = Connection.getInstance().connect();
		Statement stmt = null;
		ResultSet rs;
		if (conn == null) {
			return false;
		}

		try {
			stmt = conn.createStatement();
			//returns nothing if they have same the output, else something
			String sql = "(SELECT dataobject_id, state_id "
					+ "FROM datasetconsistsofdatanode, "
					+ "datanode, dataflow "
					+ "WHERE dataflow.dataset_id = "
					+ "datasetconsistsofdatanode.dataset_id "
					+ "AND datasetconsistsofdatanode.datanode_id = "
					+ "datanode.id AND controlnode_id = " + controlNodeId1
					+ " AND dataflow.input = 0 "
					+ "AND (dataobject_id, state_id) NOT IN "
					+ "(SELECT dataobject_id, state_id "
					+ "FROM datasetconsistsofdatanode, datanode, dataflow "
					+ "WHERE dataflow.dataset_id = "
					+ "datasetconsistsofdatanode.dataset_id "
					+ "AND datasetconsistsofdatanode.datanode_id = "
					+ "datanode.id "
					+ "AND controlnode_id = " + controlNodeId2
					+ " AND dataflow.input = 0)) UNION "
					+ "(SELECT dataobject_id, state_id "
					+ "FROM datasetconsistsofdatanode, datanode, dataflow "
					+ "WHERE dataflow.dataset_id = "
					+ "datasetconsistsofdatanode.dataset_id "
					+ "AND datasetconsistsofdatanode.datanode_id = "
					+ "datanode.id AND controlnode_id = " + controlNodeId2
					+ " AND dataflow.input = 0 "
					+ "AND (dataobject_id, state_id) NOT IN "
					+ "(SELECT dataobject_id, state_id "
					+ "FROM datasetconsistsofdatanode, datanode, dataflow "
					+ "WHERE dataflow.dataset_id = "
					+ "datasetconsistsofdatanode.dataset_id "
					+ "AND datasetconsistsofdatanode.datanode_id = "
					+ "datanode.id AND controlnode_id = " + controlNodeId1
					+ " AND " +	"dataflow.input = 0))";
			log.info(sql);
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				rs.close();
				stmt.close();
				conn.close();
				return false; //not same output
			} else {
				rs.close();
				stmt.close();
				conn.close();
				return true; //same output
			}
			//Clean-up environment

		} catch (SQLException se) {
			//Handle errors for JDBC
			log.error("SQL Error!: ", se);
		} finally {
			//finally block used to close resources
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException se2) {
				log.error("SQL Error!: ", se2);
			}
			try {
				conn.close();
			} catch (SQLException se) {
				log.error("SQL Error!: ", se);
			}
		}
		return false;
	}
}
