package de.uni_potsdam.hpi.bpt.bp2014.database;


import de.uni_potsdam.hpi.bpt.bp2014.jhistory.Log;

/**
 * This class is the representation of a dataObject instance in the database.
 * It provides the functionality to check for existing instances or create new ones.
 * Moreover you can get the state of the dataObject instance as well as prevent concurrent modifying of the same instance.
 */
public class DbDataObjectInstance extends DbObject {
    /**
     * This method checks if the instance of a dataObject is existing.
     *
     * @param scenarioInstance_id This is the database ID of a scenario instance.
     * @param dataObject_id       This is the database ID of a dataObject.
     * @return true if DataObject is existing else false.
     */
    public Boolean existDataObjectInstance(int scenarioInstance_id, int dataObject_id) {
        String sql = "SELECT id FROM dataobjectinstance WHERE scenarioinstance_id = " + scenarioInstance_id + " AND dataobject_id = " + dataObject_id;
        return this.executeExistStatement(sql);
    }

    /**
     * This method sets the state of a dataObject instance to a desirable one and saves a log entry into the database.
     *
     * @param id    This is the database ID of a dataObject instance.
     * @param state This is the desirable state of a dataObject instance.
     */
    public void setState(int id, int state) {
        Log log = new Log();
        log.newDataObjectInstanceState(id, state);
        String sql = "UPDATE dataobjectinstance SET state_id = " + state + " WHERE id = " + id;
        this.executeUpdateStatement(sql);
    }

    /**
     * This method creates and saves a dataObject instance to the database and saves a log entry into the database.
     *
     * @param scenarioInstance_id This is the database ID of a scenario instance.
     * @param state_id            This is the initial state of a dataObject instance.
     * @param dataObject_id       This is the database ID of a dataObject.
     * @return -1 if something went wrong else return the database ID of the newly created dataObject instance.
     */
    public int createNewDataObjectInstance(int scenarioInstance_id, int state_id, int dataObject_id) {
        String sql = "INSERT INTO dataobjectinstance (scenarioinstance_id, state_id, dataobject_id) VALUES (" + scenarioInstance_id + ", " + state_id + ", " + dataObject_id + ")";
        int id = this.executeInsertStatement(sql);
        Log log = new Log();
        log.newDataObjectInstance(id);
        return id;
    }

    /**
     * This method returns the dataObject instance ID of a corresponding dataObject.
     *
     * @param scenarioInstance_id This is the database ID of a scenario instance.
     * @param dataObject_id       This is the database ID of a dataObject.
     * @return -1 if something went wrong else the database ID of a dataObject instance.
     */
    public int getDataObjectInstanceID(int scenarioInstance_id, int dataObject_id) {
        String sql = "SELECT id FROM dataobjectinstance WHERE dataobject_id = " + dataObject_id + " AND scenarioinstance_id = " + scenarioInstance_id;
        return this.executeStatementReturnsInt(sql, "id");
    }

    /**
     * This method returns the state ID of a given dataObject instance.
     *
     * @param dataObjectInstance_id This is the database ID of a dataObject instance.
     * @return -1 if something went wrong else the database ID of the state of the dataObject instance.
     */
    public int getStateID(int dataObjectInstance_id) {
        String sql = "SELECT state_id FROM dataobjectinstance WHERE id = " + dataObjectInstance_id;
        return this.executeStatementReturnsInt(sql, "state_id");
    }

    /**
     * This method checks if the dataObject instance is in a modified state to prevent concurrency.
     *
     * @param dataObjectInstance_id This is the database ID of a dataObject instance.
     * @return true if another activity is using this dataObject instance else false.
     */
    public boolean getOnChange(int dataObjectInstance_id) {
        String sql = "SELECT onchange FROM dataobjectinstance WHERE id = " + dataObjectInstance_id;
        return this.executeStatementReturnsBoolean(sql, "onchange");
    }

    /**
     * This method sets the dataObject instance to a modified state so that no other activity can work with it.
     *
     * @param id       This is the database ID of a dataObject instance.
     * @param onChange This is the flag set to indicate if the dataObject instance is being modified or not.
     */
    public void setOnChange(int id, Boolean onChange) {
        int onChangeAsInt;
        if (onChange) {
            onChangeAsInt = 1;
        } else {
            onChangeAsInt = 0;
        }
        String sql = "UPDATE dataobjectinstance SET onchange = " + onChangeAsInt + " WHERE id = " + id;
        this.executeUpdateStatement(sql);
    }

    /**
     * This method returns the dataobject_id of a dataObjectInstance.
     *
     * @param dataObjectInstanceID Id of the dataIbjectInstance.
     * @return Dataobject_id.
     */
    public int getDataObjectID(int dataObjectInstanceID) {
        String sql = "SELECT dataobject_id FROM dataobjectinstance WHERE id = " + dataObjectInstanceID;
        return this.executeStatementReturnsInt(sql, "dataobject_id");
    }
}
