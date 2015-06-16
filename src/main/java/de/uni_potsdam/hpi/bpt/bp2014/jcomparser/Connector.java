package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * The Connector has methods to create entries inside the database.
 * Therefore it uses the database.Connection class.
 */
public class Connector extends DbObject {
    static Logger log = Logger.getLogger(Connector.class.getName());

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
    public int insertScenarioIntoDatabase(final String name,
                                          final long modelID,
                                          final int modelVersion) {
        String sql = "INSERT INTO scenario " +
                "(scenario.name, modelid, modelversion) " +
                "VALUES ('" + name + "', " + modelID +
                ", " + modelVersion + ")";
        return performSQLInsertStatementWithAutoId(sql);
    }

    /**
     * Inserts a Fragment Into the Database.
     * The parameters contain all necessary information.
     *
     * @param fragmentName The name of the fragment.
     * @param scenarioID   The database id of the scenario (foreign key).
     * @param modelID      The ID used for the fragment inside the xml.
     * @param modelVersion The version number of the model.
     * @return returns the database id on success or -1 if insertion failed.
     */
    public int insertFragmentIntoDatabase(final String fragmentName,
                                          final int scenarioID,
                                          final long modelID,
                                          final int modelVersion) {
        String sql = "INSERT INTO fragment " +
                "(fragment.name, scenario_id, modelid, modelversion) " +
                "VALUES ('" + fragmentName + "', " + scenarioID +
                "," + modelID + "," + modelVersion + ")";
        return performSQLInsertStatementWithAutoId(sql);
    }

    /**
     * Inserts a Control Node into the Database.
     * The Parameters contain all information for the insertion
     * are given as a parameter
     *
     * @param label      The label of the node.
     * @param type       The type of the node (StartEvent/EndEvent/Task).
     * @param fragmentID The database ID of the Fragment.
     * @param modelID    The modelID of the controlNode from the XML.
     * @return The newly created database entry.
     */
    public int insertControlNodeIntoDatabase(final String label,
                                             final String type,
                                             final int fragmentID,
                                             final long modelID) {

        String sql = "INSERT INTO controlnode " +
                "(label, controlnode.type, fragment_id, modelid) " +
                "VALUES ('" + label + "', '" +
                type + "', " + fragmentID + ", " + modelID + ")";
        return performSQLInsertStatementWithAutoId(sql);
    }

    /**
     * This method inserts a termination condition into the database.
     * Information necessary for the insertion are given to
     * the database.
     *
     * @param conditionID  The Id of the condition inside the model.
     * @param dataObjectID The Id of the dataObj. as a part of the condition.
     * @param stateID      The ID of the state which is inside the condition.
     * @param scenarioID   The Id of the scenario which has the term. condition.
     */
    public void insertTerminationConditionIntoDatabase(final int conditionID,
                                                       final int dataObjectID,
                                                       final int stateID,
                                                       final int scenarioID) {
        String sql = "INSERT INTO terminationcondition " +
                "(conditionset_id, dataobject_id, state_id, scenario_id)" +
                " VALUES (" + conditionID + ", " + dataObjectID + ", " +
                stateID + ", " + scenarioID + ")";
        performDefaultSQLInsertStatement(sql);
    }

    /**
     * Inserts a control flow into the database.
     * The parameters contain all necessary information.
     *
     * @param sourceID  the database id of the sourceNode.
     * @param targetID  the database id of the target node
     * @param condition the condition which is label of the flow.
     */
    public void insertControlFlowIntoDatabase(final int sourceID,
                                              final int targetID,
                                              final String condition) {

        String sql = "INSERT INTO controlflow " +
                "VALUES (" + sourceID + ", " +
                targetID + ", '" + condition + "')";
        performDefaultSQLInsertStatement(sql);
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
    public int insertDataObjectIntoDatabase(final String name,
                                            final int dataClassID,
                                            final int scenarioID,
                                            final int startStateID) {
        String sql = "INSERT INTO dataobject" +
                "(dataobject.name, dataclass_id, scenario_id, start_state_id) " +
                "VALUES ('" + name + "', " + dataClassID + ", " + scenarioID +
                ", " + startStateID + ")";
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
    public int insertStateIntoDatabase(final String name,
                                       final int dataClassId) {
        String sql = "INSERT INTO state (state.name, olc_id) " +
                "VALUES ('" + name + "', " + dataClassId + ")";
        return performSQLInsertStatementWithAutoId(sql);
    }

    /**
     * Inserts a new Data Class into the database.
     *
     * @param name the name of the Class.
     * @return the id of the newly created entry.
     */
    public int insertDataClassIntoDatabase(final String name, final int root) {
        String sql = "INSERT INTO dataclass (dataclass.name, dataclass.rootnode) " +
                "VALUES ('" + name + "', " + root + ")";
        return performSQLInsertStatementWithAutoId(sql);
    }

    /**
     * Inserts a new DataAttribute into the database.
     *
     * @param name        is the name of the dataAttribute as a String.
     * @param dataClassID is the databaseID of the corresponding dataClass.
     * @param type        is the type of the dataAttribute.
     * @return the auto-incremented databaseID of the newly added dataAttribute.
     */
    public int insertDataAttributeIntoDatabase(final String name, final int dataClassID, final String type) {
        String sql = "INSERT INTO dataattribute (dataattribute.name, " +
                "dataclass_id, dataattribute.type, dataattribute.default) " +
                "VALUES ('" + name + "', " + dataClassID + ", '" + type + "', '')";
        return performSQLInsertStatementWithAutoId(sql);
    }

    /**
     * Updates the scenario entry with the corresponding domainModel.
     *
     * @param modelID       is the modelID of the domainModel which should be saved as a Long.
     * @param versionNumber is the versionNumber of the domainModel as an Integer.
     * @param scenarioID    is the databaseID of the corresponding scenario.
     */
    public void insertDomainModelIntoDatabase(final long modelID, final int versionNumber, final int scenarioID) {
        DbObject dbObject = new DbObject();
        String sql = "UPDATE scenario " +
                "SET scenario.datamodelid = " + modelID + ", scenario.datamodelversion = " + versionNumber + " WHERE id = " + scenarioID + "";
        dbObject.executeUpdateStatement(sql);
    }

    /**
     * This method inserts an aggregation into the database.
     *
     * @param sourceID     is the databaseID of a dataClass which is the source of the aggregation.
     * @param targetID     is the databaseID of a dataClass which is the target of the aggregation.
     * @param multiplicity is the multiplicity of the aggregation as an Integer.
     */
    public void insertAggregationIntoDatabase(final int sourceID, final int targetID, final int multiplicity) {
        String sql = "INSERT INTO aggregation (dataclass_id1, " +
                "dataclass_id2, aggregation.multiplicity) " +
                "VALUES (" + sourceID + ", " + targetID + ", " + multiplicity + ")";
        performDefaultSQLInsertStatement(sql);
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
    public int insertDataNodeIntoDatabase(final int scenarioID,
                                          final int stateID,
                                          final int dataClassID,
                                          final int dataObjectID,
                                          final long modelID) {
        String sql = "INSERT INTO datanode " +
                "(scenario_id, state_id, dataclass_id, dataobject_id, modelid)" +
                " VALUES (" + scenarioID + ", " + stateID + ", " +
                dataClassID + ", " + dataObjectID + ", " + modelID + ")";
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
        int inputAsInt = isInput ? 1 : 0;
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
        String sql = "INSERT INTO datasetconsistsofdatanode " +
                "(dataset_id, datanode_id) " +
                "VALUES (" + dataSetID + ", " + dataNodeID + ")";
        performDefaultSQLInsertStatement(sql);
    }

    /**
     * Inserts a new DataFlow into the database.
     * All necessary information are provided by parameters.
     *
     * @param controlNodeID the id of the control node (source or target).
     * @param dataSetID     the id of the dataSet (source or target).
     * @param isInput       describes the direction, weather it is an input or output.
     */
    public void insertDataFlowIntoDatabase(final int controlNodeID,
                                           final int dataSetID,
                                           final boolean isInput) {
        int inputAsInt = isInput ? 1 : 0;
        String sql = "INSERT INTO dataflow " +
                "(controlnode_id, dataset_id, input) " +
                "SELECT " + controlNodeID + ", " +
                dataSetID + ", " + inputAsInt + " FROM dual WHERE NOT EXISTS( SELECT 1 " +
                "FROM dataflow " +
                "WHERE controlnode_id = " + controlNodeID +
                " AND dataset_id = " + dataSetID +
                " AND input = " + inputAsInt + " )";
        performDefaultSQLInsertStatement(sql);
    }

    /**
     * inserts a new Reference into the Database.
     * The references describe the equivalence relation
     * between tasks.
     *
     * @param controlNodeID1 the id of the first node.
     * @param controlNodeID2 the id of the second node.
     */
    public void insertReferenceIntoDatabase(final int controlNodeID1,
                                            final int controlNodeID2) {
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
        String sql = "INSERT INTO reference (controlnode_id1," +
                " controlnode_id2) VALUES (" + sourceNodeId +
                ", " + targetNodeId + ")";
        performDefaultSQLInsertStatement(sql);
    }

    /**
     * @param controlNodeId
     * @return
     */
    public int createEMailTemplate(int controlNodeId) {
        String sql = "INSERT INTO emailconfiguration " +
                "(receivermailaddress, sendmailaddress, subject," +
                " message, controlnode_id) SELECT " +
                "receivermailaddress, sendmailaddress, subject, message, " + controlNodeId + " FROM " +
                "emailconfiguration WHERE id = -1";
        return performSQLInsertStatementWithAutoId(sql);
    }

    /**
     * Perform a insert statement for the database.
     * Nothing will be returned. Exceptions will be catched and handled.
     *
     * @param statement the statement to be executed.
     */
    private void performDefaultSQLInsertStatement(final String statement) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        if (conn == null) {
            System.err.println("Could not create a Connection instance.");
            return;
        }
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(statement);
        } catch (SQLException se) {
            System.err.println("Error occured executing the statement:");
            System.err.println(statement);
            log.error("Error:", se);
        } finally {
            // Close used resources (Statement/Connection)
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se2) {
                System.err.println("An error occurred closing a resource");
            }
        }
    }

    /**
     * Performs a sql insert statement.
     * This method contains an basic error handling. Resources will be closed.
     *
     * @param statement the statement to be executed.
     * @return the auto increment id of the newly created entry.
     */
    private int performSQLInsertStatementWithAutoId(final String statement) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        int result = -1;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(statement, Statement.RETURN_GENERATED_KEYS);
            rs = stmt.getGeneratedKeys();
            rs.next();
            result = rs.getInt(1);
        } catch (SQLException se) {
            System.err.println("Error occured executing the statement:");
            System.err.println(statement);
            log.error("Error:", se);
        } finally {
            // Close used resources (Statement/Connection)
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se2) {
                System.err.println("An error occurred closing a resource");
            }
        }
        return result;
    }

    /**
     * Get the version of a fragment which is in the database.
     *
     * @param scenarioID databaseID of the scenario the fragment belongs to
     * @param modelID    modelID of the fragment
     * @return version of the specified fragment (return -1 if there is no fragment of this id)
     */
    public int getFragmentVersion(int scenarioID, long modelID) {

        DbDataObject dbDataObject = new DbDataObject();
        String select = "SELECT modelversion FROM fragment " +
                "WHERE scenario_id = " + scenarioID +
                " AND modelid = " + modelID;
        LinkedList<Integer> versions = dbDataObject.executeStatementReturnsListInt(select, "modelversion");
        if (versions.size() == 0) {
            return -1;
        }
        return versions.get(0);
    }

    /**
     * Get the version of a scenario which is in the database.
     *
     * @param scenarioID databaseID of the scenario
     * @return version of the scenario with the scenarioID (return -1 if there is no scenario of this id)
     */
    public int getScenarioVersion(int scenarioID) {

        DbDataObject dbDataObject = new DbDataObject();
        String select = "SELECT modelversion FROM scenario " +
                "WHERE id = " + scenarioID;
        LinkedList<Integer> versions = dbDataObject.executeStatementReturnsListInt(select, "modelversion");
        if (versions.size() == 0) {
            return -1;
        }
        return versions.get(0);
    }

    /**
     * Get the newest scenarioID for its modelId.
     *
     * @param scenarioID modelID of the scenario
     * @return the databaseID of the scenario for the LATEST version
     * (we assume that the scenario with the largest id is the one of the newest version)
     */
    public int getScenarioID(long scenarioID) {
        DbDataObject dbDataObject = new DbDataObject();
        String select = "SELECT id FROM scenario " +
                "WHERE modelid = " + scenarioID;
        LinkedList<Integer> ids = dbDataObject.executeStatementReturnsListInt(select, "id");
        if (ids.size() > 0) {
            return Collections.max(ids);
        } else {
            return -1;
        }
    }

    /**
     * Change all oldScenarioIDs to newScenarioIDs of all running instances
     * with oldScenarioID in table "scenarioinstance".
     *
     * @param oldScenarioID scenarioID that gets updated by newScenarioId
     * @param newScenarioID new scenarioID that overwrites oldScenarioId
     * @return List of all IDs that running scenarioinstances of oldScenarioID hold
     */
    public List<Integer> migrateScenarioInstance(int oldScenarioID, int newScenarioID) {
        DbDataObject dbDataObject = new DbDataObject();
        String select = "SELECT id " +
                "FROM scenarioinstance " +
                "WHERE scenarioinstance.terminated = 0 " +
                "AND scenario_id = " + oldScenarioID;
        List<Integer> runningInstances = dbDataObject.executeStatementReturnsListInt(select, "id");
        String update = "UPDATE scenarioinstance " +
                "SET scenario_id = " + newScenarioID +
                " WHERE scenarioinstance.terminated = 0 " +
                "AND scenario_id = " + oldScenarioID;
        dbDataObject.executeUpdateStatement(update);
        return runningInstances;
    }

    /**
     * Get the databaseId for the specified fragment in table "fragment".
     *
     * @param scenarioID the scenarioDatabaseID for which the fragment is selected
     * @param modelID    the modelId of the fragment
     * @return the databaseID of the selected fragment
     */
    public int getFragmentID(int scenarioID, long modelID) {
        DbDataObject dbDataObject = new DbDataObject();
        String select = "SELECT id " +
                "FROM fragment " +
                "WHERE scenario_id = " + scenarioID +
                " AND modelid = " + modelID;
        return dbDataObject.executeStatementReturnsInt(select, "id");
    }

    /**
     * Change all oldFragmentIDs to newFragmentIDs of all running instances
     * with oldFragmentID in table "fragmentinstance".
     *
     * @param oldFragmentID fragmentID that gets updated by newFragmentId
     * @param newFragmentID new fragmentID that overwrites oldFragmentId
     */
    public void migrateFragmentInstance(int oldFragmentID, int newFragmentID) {
        DbDataObject dbDataObject = new DbDataObject();
        String update = "UPDATE fragmentinstance " +
                "SET fragment_id = " + newFragmentID +
                " WHERE fragment_id = " + oldFragmentID;
        dbDataObject.executeUpdateStatement(update);
    }

    /**
     * Insert a running instance of a fragment in table "fragmentinstance".
     *
     * @param fragmentID specifies a fragment the instance is running on
     * @param instanceID specifies a scenarioinstance the fragmentinstance belongs to
     */
    public void insertFragmentInstance(int fragmentID, int instanceID) {
        DbDataObject dbDataObject = new DbDataObject();
        String insert = "INSERT INTO fragmentinstance " +
                "(fragmentinstance.terminated, fragment_id, scenarioinstance_id) " +
                "VALUES (" + 0 + ", " + fragmentID + ", " + instanceID + ")";
        dbDataObject.executeUpdateStatement(insert);


    }

    /**
     * Get the databaseId for specified controlnode in table "controlnode".
     *
     * @param fragmentID the databaseID of the fragment the controlNode belongs to
     * @param modelID    the modelId of the controlnode
     * @return the databaseID of the selected controlnode
     */
    public int getControlNodeID(int fragmentID, long modelID) {
        DbDataObject dbDataObject = new DbDataObject();
        String select = "SELECT id " +
                "FROM controlnode " +
                "WHERE fragment_id = " + fragmentID +
                " AND modelid = " + modelID;
        return dbDataObject.executeStatementReturnsInt(select, "id");

    }

    /**
     * Change all oldControlNodeIDs to newControlNodeIDs in table "controlnodeinstance".
     *
     * @param oldControlNodeID controlNodeID that gets updated by newControlNodeID
     * @param newControlNodeID new controlNodeID that overwrites oldControlNodeID
     */
    public void migrateControlNodeInstance(int oldControlNodeID, int newControlNodeID) {
        DbDataObject dbDataObject = new DbDataObject();
        String update = "UPDATE controlnodeinstance " +
                "SET controlnode_id = " + newControlNodeID +
                " WHERE controlnode_id = " + oldControlNodeID;
        dbDataObject.executeUpdateStatement(update);
    }

    /**
     * Get the dataobjectID of the specified dataObject in table "dataobject".
     *
     * @param scenarioID     the scenarioDatabaseID for which the dataobjectID is selected
     * @param dataObjectName the name of the dataObject
     *                       (we assume that a scenario can't hold dataObjects with the same name)
     * @return the dataobject_id of the selected dataObject
     */
    public int getDataObjectID(int scenarioID, String dataObjectName) {
        DbDataObject dbDataObject = new DbDataObject();
        String select = "SELECT id " +
                "FROM dataobject " +
                "WHERE scenario_id = " + scenarioID +
                " AND name = '" + dataObjectName + "'";
        return dbDataObject.executeStatementReturnsInt(select, "id");
    }

    /**
     * Change all oldDataObjectIDs to newDataObjectIDs in table "dataobjectinstance".
     *
     * @param oldDataObjectID dataobject_id that gets updated by newDataObjectID
     * @param newDataObjectID new dataobject_id that overwrites oldDataObjectID
     */
    public void migrateDataObjectInstance(int oldDataObjectID, int newDataObjectID) {
        DbDataObject dbDataObject = new DbDataObject();
        String update = "UPDATE dataobjectinstance " +
                "SET dataobject_id = " + newDataObjectID +
                " WHERE dataobject_id = " + oldDataObjectID;
        dbDataObject.executeUpdateStatement(update);
        String sql = "SELECT state_id FROM dataobjectinstance WHERE dataobject_id = " + newDataObjectID;
        List<Integer> state_ids = dbDataObject.executeStatementReturnsListInt(sql, "state_id");
        for (int state_id : state_ids) {
            update = "UPDATE dataobjectinstance SET state_id = " +
                    "(SELECT state.id FROM state WHERE olc_id = (SELECT `dataclass_id` FROM `dataobject` WHERE `id` = " + newDataObjectID + ") " +
                    "AND name = (SELECT state.name FROM state WHERE state.id = " + state_id + ")) " +
                    "WHERE dataobject_id = " + newDataObjectID +
                    " AND state_id = " + state_id;
            dbDataObject.executeUpdateStatement(update);
        }
    }

    /**
     * Get all modelIDs of the fragments for one scenario.
     *
     * @param scenarioID databaseID of the scenario
     * @return List of all modelIDs of all fragments that belong to the scenario
     */
    public List<Long> getFragmentModelIDs(int scenarioID) {
        DbDataObject dbDataObject = new DbDataObject();
        String select = "SELECT modelid " +
                "FROM fragment " +
                "WHERE scenario_id = " + scenarioID;
        return dbDataObject.executeStatementReturnsListLong(select, "modelid");
    }

    /**
     * Get the databaseIDs of all dataClasses that belong to one scenario.
     *
     * @param scenarioID databaseID of the scenario
     * @return List of all databaseIDs that belong to the scenario specified by scenarioID
     */
    public List<Integer> getDataClassIDs(int scenarioID) {
        DbDataObject dbDataObject = new DbDataObject();
        String select = "SELECT dataclass_id " +
                "FROM dataobject " +
                "WHERE scenario_id = " + scenarioID;
        // temporaryDataClassIDs might contain duplicate entries
        List<Integer> temporaryDataClassIDs = dbDataObject.executeStatementReturnsListInt(select, "dataclass_id");
        List<Integer> resultDataClassIDs = new LinkedList<>();
        for (int entry : temporaryDataClassIDs) {
            if (!resultDataClassIDs.contains(entry)) {
                resultDataClassIDs.add(entry);
            }
        }
        return resultDataClassIDs;
    }

    /**
     * Get the name of the dataClass specified by its ID.
     *
     * @param dataClassID DatabaseID of the dataClass
     * @return Name of the dataClass as String
     */
    public String getDataClassName(int dataClassID) {
        DbDataObject dbDataObject = new DbDataObject();
        String select = "SELECT name " +
                "FROM dataclass " +
                "WHERE id = " + dataClassID;
        return dbDataObject.executeStatementReturnsString(select, "name");
    }

    /**
     * Get a map of all dataAttribute-database-IDs to their name.
     *
     * @param dataClassID DatabaseID of the dataClass to which the dataAttributes belong
     * @return A map of all dataAttribute-database-IDs to their name
     */
    public Map<Integer, String> getDataAttributes(int dataClassID) {
        DbDataObject dbDataObject = new DbDataObject();
        String select = "SELECT id, name " +
                "FROM dataattribute " +
                "WHERE dataclass_id = " + dataClassID;
        return dbDataObject.executeStatementReturnsMap(select, "id", "name");

    }

    /**
     * Change all oldDataAttributeIDs to newDataAttributeID of all specified dataattributeinstances.
     *
     * @param oldDataAttributeID DataAttributeID that gets updated by newDataAttributeID
     * @param newDataAttributeID new dataAttributeID that overwrites oldDataAttributeID
     */
    public void migrateDataAttributeInstance(Integer oldDataAttributeID, Integer newDataAttributeID) {
        DbDataObject dbDataObject = new DbDataObject();
        String update = "UPDATE dataattributeinstance " +
                "SET dataattribute_id = " + newDataAttributeID +
                " WHERE dataattribute_id = " + oldDataAttributeID;
        dbDataObject.executeUpdateStatement(update);
    }

    /**
     * Get the version of the domainModel that belongs to the specified scenario.
     *
     * @param scenarioID DatabaseID of the scenario
     * @return The version of the domainModel
     */
    public int getDataModelVersion(int scenarioID) {
        DbDataObject dbDataObject = new DbDataObject();
        String select = "SELECT datamodelversion " +
                "FROM scenario " +
                "WHERE id = " + scenarioID;
        return dbDataObject.executeStatementReturnsInt(select, "datamodelversion");
    }

    /**
     * Get the modelID of the domainModel (from the XML) that belongs to the specified scenario.
     *
     * @param scenarioID DatabaseID of the scenario
     * @return The modelID of the domainModel
     */
    public long getDataModelID(int scenarioID) {
        DbDataObject dbDataObject = new DbDataObject();
        String select = "SELECT datamodelid " +
                "FROM scenario " +
                "WHERE id = " + scenarioID;
        return dbDataObject.executeStatementReturnsListLong(select, "datamodelid").get(0);
    }

    /**
     * Find out if the scenario in the database is marked a s deleted.
     *
     * @param scenarioID DatabaseID of the scenario
     * @return The value of the deleted flag (1 if scenario is deleted; 0 otherwise)
     */
    public int isScenarioDeleted(int scenarioID) {
        DbDataObject dbDataObject = new DbDataObject();
        String select = "SELECT deleted " +
                "FROM scenario " +
                "WHERE id = " + scenarioID;
        return dbDataObject.executeStatementReturnsInt(select, "deleted");
    }
}
