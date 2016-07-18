package de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import org.apache.log4j.Logger;

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
	private int id = -1;

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
		if (type == null || this.id != controlNodeId) {
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
		if (label == null || this.id != controlNodeId) {
			String sql = "SELECT label FROM controlnode WHERE id = " + controlNodeId;
			log.info(sql);
			label = this.executeStatementReturnsString(sql, "label");
			this.id = controlNodeId;
		}
		return label;
	}

    public boolean existControlNode(int controlNodeId, int scenarioId) {
        String existControlNode = "SELECT * FROM controlnode, fragment WHERE " +
                "controlnode.fragment_id = fragment.id AND " +
                "controlnode.id = %d AND fragment.scenario_id = %d;";
        existControlNode = String.format(existControlNode, controlNodeId, scenarioId);
        return this.executeExistStatement(existControlNode);
    }
}
