package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataClass;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * The Connector has methods to create entries inside the database.
 * Therefore it uses the database.Connection class.
 */
public class Connector extends DbObject {
	private static Logger log = Logger.getLogger(Connector.class);

	/**
	 * A method to write a scenario to the database.
	 * The parameter are all the information needed to
	 * create a scenario entry.
	 *
	 * @param name         The name of the scenario.
	 * @param modelID      The ID used inside the xml representation.
	 * @param modelVersion The version number of the model.
	 * @return returns the database id on success or -1 if insertion failed.
	 */
	public int insertScenarioIntoDatabase(final String name, final String modelID,
			final int modelVersion) {
		String sql = "INSERT INTO scenario "
				+ "(scenario.name, modelid, modelversion) "
				+ "VALUES ('" + name + "', '"
				+ modelID +	"', " + modelVersion + ")";
		return performSQLInsertStatementWithAutoId(sql);
	}

	/**
	 * Inserts the XML belonging to a Fragment into the database.
	 *
	 * @param xml The xml-String of a fragment.
	 * @return the database Auto-Increment-ID of the xml.
     */
	public int insertXmlIntoDatabase(final int fragmentId, final String xml) {
		String sql = "Insert into fragmentxml (fragment_id, xml) "
				+ "VALUES (%d, '%s')";
		return performSQLInsertStatementWithAutoId(
				String.format(sql, fragmentId, xml));
	}

	/**
	 * Inserts a DatabaseFragment Into the Database.
	 * The parameters contain all necessary information.
	 *
	 * @param fragmentName The name of the fragment.
	 * @param scenarioID   The database id of the scenario (foreign key).
	 * @param modelID      The ID used for the fragment inside the xml.
	 * @param modelVersion The version number of the model.
	 * @return returns the database id on success or -1 if insertion failed.
	 */
	public int insertFragmentIntoDatabase(final String fragmentName, int scenarioID,
			final String modelID, final int modelVersion) {
		String sql = "INSERT INTO fragment "
				+ "(fragment.name, scenario_id, modelid, modelversion) "
				+ "VALUES ('" + fragmentName + "', " + scenarioID
				+ ",'" + modelID + "'," + modelVersion + ")";
		return performSQLInsertStatementWithAutoId(sql);
	}

	/**
	 * Inserts a Control Node into the Database.
	 * The Parameters contain all information for the insertion
	 * are given as a parameter
	 *
	 * @param label      The label of the node.
	 * @param type       The type of the node (StartEvent/EndEvent/Task).
	 * @param fragmentID The database ID of the DatabaseFragment.
	 * @param modelID    The modelID of the controlNode from the XML.
	 * @return The newly created database entry.
	 */
	public int insertControlNodeIntoDatabase(final String label, final String type,
			final int fragmentID, final String modelID) {

		String sql = "INSERT INTO controlnode "
				+ "(label, controlnode.type, fragment_id, modelid) "
				+ "VALUES ('" + label + "', '" + type
				+ "', " + fragmentID + ", '" + modelID + "')";
		return performSQLInsertStatementWithAutoId(sql);
	}

	/**
	 * This method inserts a termination condition into the database.
	 * Information necessary for the insertion are given to
	 * the database.
	 *
	 * @param dataObjectId The Id of the dataObj. as a part of the condition.
	 * @param stateId      The State id of the condition.
	 * @param scenarioId   The Id of the scenario which has the term. condition.
	 */
	public void insertTerminationConditionIntoDatabase(
            final int dataObjectId, final int stateId, final int scenarioId,
            final String conditionSetId) {
		String sql = "INSERT INTO terminationcondition "
				+ "(dataobject_id, state_id, scenario_id, conditionset_id)"
				+ " VALUES (%d, %d, %d, '%s');";
        String insertTerminationCondition = String.format(sql, dataObjectId, stateId,
                scenarioId, conditionSetId);
		this.executeInsertStatement(insertTerminationCondition);
	}

	/**
	 * Inserts a control flow into the database.
	 * The parameters contain all necessary information.
	 *
	 * @param sourceID  the database id of the sourceNode.
	 * @param targetID  the database id of the target node
	 * @param condition the condition which is label of the flow.
	 */
	public void insertControlFlowIntoDatabase(final int sourceID, final int targetID,
			final String condition) {

		String sql = "INSERT INTO controlflow "
				+ "VALUES (" + sourceID + ", "
				+ targetID + ", '" + condition + "')";
        this.executeInsertStatement(sql);
	}

	/**
	 * Inserts a DataObject into the database.
	 * The necessary information will be given as parameters.
	 *
	 * @param name         The name of the DataObject.
	 * @param dataClassID  The database Id of the class, which
	 *                     describes the DataObject.
	 * @param scenarioID   The database id of the scenario.
	 * @param startStateID the database id of the initial State.
	 * @return the id of the newly created dataobject entry.
	 */
	public int insertDataObjectIntoDatabase(final String name, final int dataClassID,
			final int scenarioID, final int startStateID) {
		String sql = "INSERT INTO dataobject"
				+ "(dataobject.name, dataclass_id, scenario_id, start_state_id) "
				+ "VALUES ('" + name + "', " + dataClassID + ", "
				+ scenarioID + ", " + startStateID + ")";
		return performSQLInsertStatementWithAutoId(sql);
	}

	/**
	 * This methods inserts a State into the Database.
	 * All needed information will be given as parameters.
	 *
	 * @param name        The name of the state.
	 * @param dataClassId The id of the class which can have the state.
	 * @return the database id of the newly created entry.
	 */
	public int insertStateIntoDatabase(final String name, final int dataClassId) {
		String sql = "INSERT INTO state (state.name, olc_id) "
				+ "VALUES ('" + name + "', " + dataClassId + ")";
		return performSQLInsertStatementWithAutoId(sql);
	}

	/**
	 * Inserts a new Data Class into the database.
	 *
	 * @param name the name of the Class.
	 * @return the id of the newly created entry.
	 */
	public int insertDataClassIntoDatabase(final String name, final int isEvent) {
		String sql = "INSERT INTO dataclass (name, is_event) "
				+ "VALUES ('%s', %d)";
		return performSQLInsertStatementWithAutoId(
				String.format(sql, name, isEvent));
	}

	/**
	 * Inserts a new DataAttribute into the database.
	 *
	 * @param name        is the name of the dataAttribute as a String.
	 * @param dataClassID is the databaseID of the corresponding dataClass.
	 * @param type        is the type of the dataAttribute.
	 * @return the auto-incremented databaseID of the newly added dataAttribute.
	 */
	public int insertDataAttributeIntoDatabase(final String name, final int dataClassID,
			final String type) {
		String sql = "INSERT INTO dataattribute (dataattribute.name, "
				+ "dataclass_id, dataattribute.type, dataattribute.default) "
				+ "VALUES ('" + name + "', "
				+ dataClassID + ", '" + type + "', '')";
		return performSQLInsertStatementWithAutoId(sql);
	}

	/**
	 * Updates the scenario entry with the corresponding domainModel.
	 *
	 * @param editorId      is the modelID of the domainModel which should be saved as a String.
	 * @param versionNumber is the versionNumber of the domainModel as an Integer.
	 * @param scenarioId    is the databaseID of the corresponding scenario.
	 */
	public void insertDomainModelIntoDatabase(final String editorId, final int versionNumber,
			final int scenarioId) {
		DbObject dbObject = new DbObject();
		String sql = "UPDATE scenario "
				+ "SET scenario.datamodelid = '" + editorId
				+ "', scenario.datamodelversion = " + versionNumber
				+ " WHERE id = " + scenarioId + "";
		dbObject.executeUpdateStatement(sql);
	}

	/**
	 * This method inserts an aggregation into the database.
	 *
	 * @param sourceID     is the ID of a dataClass which is the source of the aggregation.
	 * @param targetID     is the ID of a dataClass which is the target of the aggregation.
	 * @param multiplicity is the multiplicity of the aggregation as an Integer.
	 */
	public void insertAggregationIntoDatabase(final int sourceID, final int targetID,
			final int multiplicity) {
		String sql = "INSERT INTO aggregation (dataclass_id1, "
				+ "dataclass_id2, aggregation.multiplicity) "
				+ "VALUES (" + sourceID + ", "
				+ targetID + ", " + multiplicity + ")";
        this.executeInsertStatement(sql);
	}

    /**
     * This method inserts events into the database.
     *
     * @param eventtype type of the event.
     * @param eventquery the event processing query of the event.
     * @param fragmentId the ID of the fragment.
     * @param modelId the ID of the event given by the editor.
     * @param controlNodeDatabaseId the database ID of the controlnode belonging to this event.
     *
     */
    public int insertEventIntoDatabase(String eventtype, String eventquery,
                                       int fragmentId, String modelId, int controlNodeDatabaseId) {
        String sql = "INSERT INTO event "
                + "(event_type, query, fragment_id, model_id, controlnode_id) "
                + "VALUES ('" + eventtype + "', '" + eventquery + "', "
                + fragmentId + ", '" + modelId + "' ," + controlNodeDatabaseId + ")";
        return performSQLInsertStatementWithAutoId(sql);
    }

    public void saveTimerDefinition(String timerDefinition, int fragmentId,
                                   int controlNodeDatabaseId) {
        String insertTimerDefinition = "INSERT INTO timerevent (timerDefinition, fragmentId,"
                + " controlNodeDatabaseId) VALUES ('%s', %d, %d);";
        String filledOutQuery = String.format(insertTimerDefinition, timerDefinition, fragmentId,
            controlNodeDatabaseId);
        this.executeInsertStatement(filledOutQuery);
    }

    /**
     * Boundary events also have an attachedtoref attribute.
     * @param eventtype type of the event.
     * @param eventquery the event processing query of the event.
     * @param fragmentId the ID of the fragment.
     * @param modelId the ID of the event given by the editor.
     * @param controlNodeDatabaseId the database ID of the controlnode belonging to this event.
     * @param activityDbId the ID of the Activity that the event is attached to.
     */
    public void insertBoundaryEventIntoDatabase(String eventtype, String eventquery,
                int fragmentId, String modelId, int controlNodeDatabaseId, int activityDbId) {
        insertEventIntoDatabase(eventtype, eventquery, fragmentId, modelId, controlNodeDatabaseId);
        String sql = "INSERT INTO boundaryeventref (controlnode_id, attachedtoref)"
                + "VALUES (" + controlNodeDatabaseId + ", " + activityDbId + ")";
        this.executeInsertStatement(sql);
    }

	/**
	 * This Methods inserts a new DataNode into the Database.
	 * All necessary information are given as a parameter.
	 *
	 * @param scenarioID   The database Id of the corresponding scenario.
	 * @param stateID      The database Id of the state of the Node.
	 * @param dataClassID  The data class which describes the data object.
	 * @param dataObjectID The data Object which is represented by the node.
	 * @param modelID      The modelID of the dataNode in the XML.
	 * @return the autoincrement id of the newly created entry.
	 */
	public int insertDataNodeIntoDatabase(final int scenarioID, final int stateID,
			final int dataClassID, final int dataObjectID, final String modelID) {
		String sql = "INSERT INTO datanode "
				+ "(scenario_id, state_id, dataclass_id, dataobject_id, model_id)"
				+ " VALUES (" + scenarioID + ", " + stateID + ", "
				+ dataClassID + ", " + dataObjectID + ", '" + modelID + "')";
		return performSQLInsertStatementWithAutoId(sql);
	}

	/**
	 * Inserts a dataset into the database.
	 * Only a flag is needed which indicates,
	 * weather the set is an input or an output set.
	 *
	 * @param isInput true if input else false.
	 * @return The id of the newly created dataset entry.
	 */
	public int insertDataSetIntoDatabase(final boolean isInput) {
		int inputAsInt;
		if (isInput) {
			inputAsInt = 1;
		} else {
			inputAsInt = 0;
		}
		String sql = "INSERT INTO dataset (input) VALUES (" + inputAsInt + ")";
		return performSQLInsertStatementWithAutoId(sql);
	}

	/**
	 * Inserts a relation of a datanode to a dataset.
	 * All necessary information are given as a parameter.
	 *
	 * @param dataSetID  the database id of the dataset.
	 * @param dataNodeID the database id of the datanode.
	 */
	public void insertDataSetConsistOfDataNodeIntoDatabase(final int dataSetID,
			final int dataNodeID) {
		String sql = "INSERT INTO datasetconsistsofdatanode "
				+ "(dataset_id, datanode_id) "
				+ "VALUES (" + dataSetID + ", " + dataNodeID + ")";
        this.executeInsertStatement(sql);
	}

	/**
	 * Inserts a new DataFlow into the database.
	 * All necessary information are provided by parameters.
	 *
	 * @param controlNodeID the id of the control node (source or target).
	 * @param dataSetID     the id of the dataSet (source or target).
	 * @param isInput       describes the direction, weather it is an input or output.
	 */
	public void insertDataFlowIntoDatabase(final int controlNodeID, final int dataSetID,
			final boolean isInput) {
		int inputAsInt;
		if (isInput) {
			inputAsInt = 1;
		} else {
			inputAsInt = 0;
		}
		String sql = "INSERT INTO dataflow "
				+ "(controlnode_id, dataset_id, input) "
				+ "SELECT " + controlNodeID + ", "
				+ dataSetID + ", " + inputAsInt + " "
				+ "FROM dual WHERE NOT EXISTS( SELECT 1 "
				+ "FROM dataflow  WHERE controlnode_id = " + controlNodeID
				+ " AND dataset_id = " + dataSetID
				+ " AND input = " + inputAsInt + " )";
        this.executeInsertStatement(sql);
	}

	/**
	 * inserts a new Reference into the Database.
	 * The references describe the equivalence relation
	 * between tasks.
	 *
	 * @param controlNodeID1 the id of the first node.
	 * @param controlNodeID2 the id of the second node.
	 */
	public void insertReferenceIntoDatabase(
			final int controlNodeID1, final int controlNodeID2) {
		insertReferenceOneSideIntoDatabase(controlNodeID1, controlNodeID2);
		insertReferenceOneSideIntoDatabase(controlNodeID2, controlNodeID1);
	}

	/**
	 * Inserts one reference into the database.
	 *
	 * @param sourceNodeId source node id of the reference.
	 * @param targetNodeId target node id of the reference.
	 */
	private void insertReferenceOneSideIntoDatabase(final int sourceNodeId,
			final int targetNodeId) {
		String sql = "INSERT INTO reference (controlnode_id1,"
				+ " controlnode_id2) VALUES ("
				+ sourceNodeId + ", " + targetNodeId + ")";
        this.executeInsertStatement(sql);
	}

	public void insertPathMappingIntoDatabase(int controlNodeId, int dataAttributeId, String jsonPathString) {
		String sql = "INSERT INTO pathmapping (controlnode_id,"
				+ "dataattribute_id, jsonpath) VALUES ("
				+ controlNodeId + ", " + dataAttributeId + ", '" + jsonPathString + "')";
		this.executeInsertStatement(sql);
	}

	public void insertWebServiceTaskIntoDatabase(int controlNodeId, String url, String method, String body) {
		String sql = String.format(
				"INSERT INTO webservicetask (controlnode_id, url, method, body) "
						+ "VALUES (%d, '%s', '%s', '%s')",
				controlNodeId, url, method, body);
		this.executeInsertStatement(sql);
	}

	/**
	 * Get the databaseIDs of all dataClasses that belong to one scenario.
	 *
	 * @param scenarioID databaseID of the scenario
	 * @return List of all databaseIDs that belong to the scenario specified by scenarioID
	 */
	public List<Integer> getDataClassIDs(int scenarioID) {
		DbDataClass dataClass = new DbDataClass();
		String select = "SELECT dataclass_id "
				+ "FROM dataobject "
				+ "WHERE scenario_id = " + scenarioID;
		// temporaryDataClassIDs might contain duplicate entries
		List<Integer> temporaryDataClassIDs = dataClass
				.executeStatementReturnsListInt(select, "dataclass_id");
		List<Integer> resultDataClassIDs = new LinkedList<>();
		for (int entry : temporaryDataClassIDs) {
			if (!resultDataClassIDs.contains(entry)) {
				resultDataClassIDs.add(entry);
			}
		}
		return resultDataClassIDs;
	}

    public void insertStartQueryIntoDatabase(String query, int scenarioId,
                                             int attributeDbId, String jsonPath, String id) {
        String sql = "INSERT INTO startquery (query, scenario_id, dataattribute_id, jsonpath, id) " +
                "VALUES ('%s', %d, %d, '%s', '%s');";
        sql = String.format(sql, query, scenarioId, attributeDbId, jsonPath, id);
        this.executeInsertStatement(sql);
    }
}
