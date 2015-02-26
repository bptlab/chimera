package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * ********************************************************************************
 *
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 *
 * ******************************************************************
 *
 * Copyright © All Rights Reserved 2014 - 2015
 *
 * Please be aware of the License. You may found it in the root directory.
 *
 * **********************************************************************************
 */


/**
 * The Connector has methods to create entries inside the database.
 * Therefore it uses the database.Connection class.
 */
public class Connector extends DbDataObject{

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
     * @return The newly created database entry.
     */
    public int insertControlNodeIntoDatabase(final String label,
                                             final String type,
                                             final int fragmentID) {

        String sql = "INSERT INTO controlnode " +
                "(label, controlnode.type, fragment_id) " +
                "VALUES ('" + label + "', '" +
                type + "', " + fragmentID + ")";
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
    public int insertDataClassIntoDatabase(final String name) {
        String sql = "INSERT INTO dataclass (dataclass.name) " +
                "VALUES ('" + name + "')";
        return performSQLInsertStatementWithAutoId(sql);
    }

    /**
     * This Methods inserts a new DataNode into the Database.
     * All necessary information are given as a parameter.
     *
     * @param scenarioID   The database Id of the corresponding scenario.
     * @param stateID      The database Id of the state of the Node.
     * @param dataClassID  The data class which describes the data object.
     * @param dataObjectID The data Object which is represented by the node.
     * @return the autoincrement id of the newly created entry.
     */
    public int insertDataNodeIntoDatabase(final int scenarioID,
                                          final int stateID,
                                          final int dataClassID,
                                          final int dataObjectID) {
        String sql = "INSERT INTO datanode " +
                "(scenario_id, state_id, dataclass_id, dataobject_id)" +
                " VALUES (" + scenarioID + ", " + stateID + ", " +
                dataClassID + ", " + dataObjectID + ")";
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
                " AND input = " + inputAsInt +" )";
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
     * Inserts a EmailTemplate into the database
     * currently not used.
     *
     * @param controlNodeId the database id of the node which is the mail task.
     * @return returns the newly created Ids.
     */
    public int insertStandardEmailTemplateIntoDatabase(final int controlNodeId) {
        String sql = "INSERT INTO emailconfiguration " +
                "(receivermailaddress, sendmailaddress, subject," +
                " message, controlnode_id) VALUES ('test@test.com'," +
                " 'test@test.com', 'test', 'test', " +
                controlNodeId + ")";
        return performSQLInsertStatementWithAutoId(sql);
    }

    /**
     *
     *
     * @param controlNodeId
     * @return
     */
    public int createEMailTemplate(int controlNodeId){
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
            se.printStackTrace();
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
            se.printStackTrace();
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
     * Get the latest version of a fragment which is in the database.
     *
     * @param fragmentModelID modelID of the fragment
     * @param scenarioID modelID of the scenario the fragment belongs to
     * @return latest version of the fragment with the fragmentModelID (return -1 if there is no fragment of this id)
     */
    public int getFragmentVersion(long fragmentModelID, long scenarioID) {

        DbDataObject dbDataObject = new DbDataObject();
        String select = "SELECT fragment.modelversion FROM scenario, fragment " +
                "WHERE scenario.id = fragment.scenario_ID " +
                "AND scenario.modelid = " + scenarioID +
                " AND fragment.modelid = " + fragmentModelID;
        LinkedList<Integer> versions= dbDataObject.executeStatementReturnsListInt(select, "modelversion");
        int maxVersion = -1;
        for (int entry : versions) {
            if (entry > maxVersion) {
                maxVersion = entry;
            }
        }
        return maxVersion;
    }
    /**
     * Get the latest version of a scenario which is in the database.
     *
     * @param scenarioID modelID of the scenario
     * @return latest version of the scenario with the scenarioID (return -1 if there is no scenario of this id)
     */
    public int getScenarioVersion(long scenarioID) {

        DbDataObject dbDataObject = new DbDataObject();
        String select = "SELECT modelversion FROM scenario " +
                "WHERE modelid = " + scenarioID;
        LinkedList<Integer> versions= dbDataObject.executeStatementReturnsListInt(select, "modelversion");
        int maxVersion = -1;
        for (int entry : versions) {
            if (entry > maxVersion) {
                maxVersion = entry;
            }
        }
        return maxVersion;
    }


}
