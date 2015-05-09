package de.uni_potsdam.hpi.bpt.bp2014.database;

import de.uni_potsdam.hpi.bpt.bp2014.jhistory.Log;

public class DbDataAttributeInstance extends DbObject {

    /**
     * This method creates and saves a new DataAttributeInstance to the database into the context of a DataObjectInstance and saves a log entry into the database.
     *
     * @param dataAttribute_id      This is the database ID of the DataAttribute of which the new Instance is created, the ID can be found in the database.
     * @param dataObjectInstance_id This is the database ID of the DataObjectInstance the AttributeInstance belongs to which is found in the database.
     * @return an Integer which is -1 if something went wrong else it is the database ID of the newly created DataAttributeInstance.
     */
    public int createNewDataAttributeInstance(int dataAttribute_id, int dataObjectInstance_id) {
        String sql = "INSERT INTO dataattributeinstance (value, dataobjectinstance_id, dataattribute_id) VALUES ((SELECT dataattribute.default FROM dataattribute WHERE id = " + dataAttribute_id + "), " + dataObjectInstance_id + ", " + dataAttribute_id + ")";
        int id = this.executeInsertStatement(sql);
        Log log = new Log();
        log.newDataAttributeInstance(id);
        return id;
    }

    public Boolean existDataAttributeInstance(int dataAttribute_id, int dataObjectInstance_id) {
        String sql = "SELECT id FROM dataattributeinstance WHERE dataobjectinstance_id = " + dataObjectInstance_id + " AND dataattribute_id = " + dataAttribute_id;
        return this.executeExistStatement(sql);
    }

    public int getDataAttributeInstanceID(int dataAttribute_id, int dataObjectInstance_id) {
        String sql = "SELECT id FROM dataattributeinstance WHERE dataobjectinstance_id = " + dataObjectInstance_id + " AND dataattribute_id = " + dataAttribute_id;
        return this.executeStatementReturnsInt(sql, "id");
    }

    public String getType(int dataAttribute_id) {
        String sql = "SELECT type FROM dataattribute WHERE id = " + dataAttribute_id;
        return this.executeStatementReturnsString(sql, "type");
    }

    public Object getValue(int dataAttributeInstance_id) {
        String sql = "SELECT value FROM dataattributeinstance WHERE id = " + dataAttributeInstance_id;
        return this.executeStatementReturnsObject(sql, "value");
    }

    public String getName(int dataAttribute_id) {
        String sql = "SELECT name FROM dataattribute WHERE id = " + dataAttribute_id;
        return this.executeStatementReturnsString(sql, "name");
    }

    /**
     * This method sets the DataAttributeinstance to a desired value and saves a log entry into the database.
     *
     * @param dataAttributeInstance_id This is the database ID of the DataAttributeinstance which is found in the database.
     * @param value                    This is the value the DataAttributeinstance should have after executing setValue.
     */
    public void setValue(int dataAttributeInstance_id, Object value) {
        Log log = new Log();
        log.newDataAttributeInstanceValue(dataAttributeInstance_id, value);
        String sql = "UPDATE dataattributeinstance SET value = '" + value + "' WHERE id = " + dataAttributeInstance_id;
        executeUpdateStatement(sql);
    }
}
