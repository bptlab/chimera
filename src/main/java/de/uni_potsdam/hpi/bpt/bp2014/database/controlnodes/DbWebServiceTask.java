package de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * This class implements the WebServiceTask.
 */
public class DbWebServiceTask extends DbObject {
    private static Logger log = Logger.getLogger(DbWebServiceTask.class);

    /**
	 * Returns the Link for the webservice task.
	 *
	 * @param controlNodeId The controlnode_id of the webservice task.
	 * @return a String with the url.
	 */
	public String getUrl(int controlNodeId) {
		String sql = "SELECT url FROM webservicetask "
				+ "WHERE controlnode_id = " + controlNodeId;
		return this.executeStatementReturnsString(sql, "url");
	}

    /**
	 * Get a List with all ids from the attributes which get changed by the webservice task.
	 * @param controlNodeId The controlnode_id of the webservice task.
	 * @return a List with all Attribute ids for the webservice task.
	 */
	public List<Integer> getAttributeIdsForControlNode(int controlNodeId) {
		String sql =
				"SELECT DISTINCT dataattribute_id FROM pathmapping "
						+ "WHERE controlnode_id = " + controlNodeId;
		return this.executeStatementReturnsListInt(sql, "dataattribute_id");
	}

	/**
	 * Updates the url for the given webservice task.
	 * @param controlNodeId The controlnode_id of the webservice task.
	 * @param url          The Link for the webservice task.
	 */
	public void updateWebServiceTaskUrl(int controlNodeId, String url) {
		String sql = "UPDATE webservicetask "
				+ "SET url = '" + url
				+ "' WHERE controlnode_id = " + controlNodeId;
		executeUpdateStatement(sql);
	}

	/**
	 * Updates the method for the given webservice task.
	 * @param controlNodeId The controlnode_id of the webservice task.
	 * @param method        The method GET/PUT/POST.
	 */
	public void updateWebServiceTaskMethod(int controlNodeId, String method) {
		String sql =
				"UPDATE webservicetask "
						+ "SET method = '" + method
						+ "' WHERE controlnode_id = " + controlNodeId;
		executeUpdateStatement(sql);
	}

	/**
	 * Updates the post for the given webservice task.
	 * @param controlNodeId The controlnode_id of the webservice task.
	 * @param body          A JSON that is used by the PUT/POST.
	 */
	public void updateWebServiceTaskBody(int controlNodeId, String body) {
		String sql = "UPDATE webservicetask "
				+ "SET body = '%s' "
				+ "WHERE controlnode_id = %d";
		executeUpdateStatement(String.format(sql, body, controlNodeId));
	}

	/**
	 * Deletes all entries in the database table webservicetaskattribute
	 * for the given webservice task and the given data attribute.
	 * @param controlNodeID   The controlnode_id of the webservice task.
	 * @param dataAttributeID The id of the data attribute.
	 */
	public void deleteWebServiceTaskAtribute(int controlNodeID, int dataAttributeID) {
		String sql = "DELETE FROM pathmapping "
				+ "WHERE controlnode_id = " + controlNodeID
				+ " AND dataattribute_id = " + dataAttributeID;
		executeUpdateStatement(sql);
	}

	/**
	 * Get all WebServiceTasks for one scenario.
	 *
	 * @param scenarioID The databaseID of the scenario.
	 * @return controlNodeIDs of WebServiceTasks that belong to the scenario.
	 */
	public LinkedList<Integer> getWebServiceTasks(int scenarioID) {
		String sql = "SELECT controlnode.id "
				+ "FROM fragment, controlnode "
				+ "WHERE fragment.id = controlnode.fragment_id "
				+ "AND fragment.scenario_id = " + scenarioID + " "
				+ "AND controlnode.type = 'WebServiceTask'";
		return executeStatementReturnsListInt(sql, "controlnode.id");
	}

	/**
	 * Get the method GET/PUT/POST.
	 *
	 * @param controlNodeId The controlnode_id of the webservice task.
	 * @return a String whith GET/PUT/POST.
	 */
	public String getMethod(int controlNodeId) {
		String sql =
				"SELECT method FROM webservicetask "
						+ "WHERE `controlnode_id` = " +	controlNodeId;
		return this.executeStatementReturnsString(sql, "method");
	}

	/**
	 * Returns the JSON String for the POST/PUT.
	 *
	 * @param controlNodeId The controlnode_id of the webservice task.
	 * @return The JSON String for the POST/PUT.
	 */
	public String getPOSTBody(int controlNodeId) {
		String sql =
				"SELECT body FROM webservicetask "
						+ "WHERE `controlnode_id` = " + controlNodeId;
		return this.executeStatementReturnsString(sql, "body");
	}

	/**
	 * Checks if the webServiceTask has a body that is used in a POST/PUT request.
	 *
	 * @param controlNodeId The id of the webservice task.
	 * @return true if it has a body, false if not.
	 */
	public boolean doesWebServiceTaskHaveBody(int controlNodeId) {
		String sql = "SELECT * FROM webservicetask "
				+ "WHERE controlnode_id = %d "
				+ "AND body <> NULL "
				+ "AND body <> ''";
		return executeExistStatement(String.format(sql, controlNodeId));
	}

	/**
	 * Return a list of all dataattribute IDs and their names that are accessible
	 * in any dataobject of the outputset of the webserviceTask.
	 *
	 * @param webserviceNodeId The controlnode_id of the webservice task.
	 * @return A map of all dataattributeIDs and its names
	 */
	public Map<Integer, String> getOutputAttributesForWebservice(int webserviceNodeId) {
		// TODO
		String sql = "SELECT dataattribute.id as did, dataattribute.name as dname"
				+ " FROM pathmapping, dataattribute "
				+ "WHERE pathmapping.controlnode_id = %d "
				+ "AND dataattribute.id = pathmapping.dataattribute_id";
		return executeStatementReturnsMap(sql, "did", "dname");
	}
}
