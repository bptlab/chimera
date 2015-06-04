package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.LinkedList;


/**
 * This class is the representation of a dataObject in the database.
 * It provides the functionality to retrieve all dataObjects of a scenario as well as their names and startStates.
 */
public class DbDataObject extends DbObject {
    /**
     * This method returns all database ID's for all dataObjects belonging to a scenario.
     *
     * @param scenario_id This is the database ID of a scenario.
     * @return a list of database ID's of dataObjects belonging to this scenario.
     */
    public LinkedList<Integer> getDataObjectsForScenario(int scenario_id) {
        String sql = "SELECT id FROM dataobject WHERE scenario_id = " + scenario_id;
        return this.executeStatementReturnsListInt(sql, "id");
    }

    /**
     * This method returns the startState of a specific dataObject used for initialization.
     *
     * @param dataObject_id This is the database ID of a dataObject.
     * @return the database ID of the startState of the dataObject.
     */
    public int getStartStateID(int dataObject_id) {
        String sql = "SELECT start_state_id FROM dataobject WHERE id = " + dataObject_id;
        return this.executeStatementReturnsInt(sql, "start_state_id");
    }

    /**
     * This method returns the name of a dataObject corresponding to the database ID.
     *
     * @param dataObject_id This is the database ID of a dataObject.
     * @return the name of a dataObject as a String.
     */
    public String getName(int dataObject_id) {
        String sql = "SELECT name FROM dataobject WHERE id = " + dataObject_id;
        return this.executeStatementReturnsString(sql, "name");
    }

    public LinkedList<Integer> getAllDataAttributesForDataObject(int dataObject_id) {
        String sql = "SELECT dataattribute.id AS id FROM `dataobject`, `dataclass`, `dataattribute` WHERE dataobject.dataclass_id = dataclass.id AND dataclass.id = dataattribute.dataclass_id AND dataobject.id = " + dataObject_id;
        return this.executeStatementReturnsListInt(sql, "id");
    }
}
