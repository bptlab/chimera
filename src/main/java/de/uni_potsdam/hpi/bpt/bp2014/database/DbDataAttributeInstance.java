package de.uni_potsdam.hpi.bpt.bp2014.database;

/**
 * Created by jaspar.mang on 19.03.15.
 */
public class DbDataAttributeInstance extends DbObject{

    public int createNewDataAttributeInstance(int attribute_id, Object value, int dataObjectInstance_id) {
        String sql = "INSERT INTO dataattributeinstance (value, dataobjectinstance_id, dataattribute_id) VALUES ('" + value + "', " + dataObjectInstance_id + ", " + attribute_id + ")";
        int id = this.executeInsertStatement(sql);
        return id;
    }

    public Boolean existDataAttributeInstance(int attribute_id, int dataObjectInstance_id) {
        String sql = "SELECT id FROM dataattributeinstance WHERE dataobjectinstance_id = " + dataObjectInstance_id + " AND dataattribute_id = " + attribute_id;
        return this.executeExistStatement(sql);
    }

    public int getDataAttributeInstanceID(int attribute_id, int dataObjectInstance_id) {
        String sql = "SELECT id FROM dataattributeinstance WHERE dataobjectinstance_id = " + dataObjectInstance_id + " AND dataattribute_id = " + attribute_id;
        return this.executeStatementReturnsInt(sql, "id");
    }

    //TODO: dataAttributes methods to set values
}
