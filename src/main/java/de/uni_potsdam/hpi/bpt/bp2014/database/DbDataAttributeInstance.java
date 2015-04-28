package de.uni_potsdam.hpi.bpt.bp2014.database;

public class DbDataAttributeInstance extends DbObject{

    public int createNewDataAttributeInstance(int dataAttribute_id, int dataObjectInstance_id) {
        String sql = "INSERT INTO dataattributeinstance (value, dataobjectinstance_id, dataattribute_id) VALUES ((SELECT dataattribute.default FROM dataattribute WHERE id = " + dataAttribute_id + "), " + dataObjectInstance_id + ", " + dataAttribute_id + ")";
        int id = this.executeInsertStatement(sql);
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

    public void setValue(int dataAttributeInstance_id, Object value){
        String sql = "UPDATE dataattributeinstance SET value = '" + value + "' WHERE id = " + dataAttributeInstance_id;
        executeUpdateStatement(sql);
    }
}
