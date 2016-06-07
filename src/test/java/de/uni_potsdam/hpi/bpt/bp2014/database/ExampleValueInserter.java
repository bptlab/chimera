package de.uni_potsdam.hpi.bpt.bp2014.database;

/**
 *
 */
public class ExampleValueInserter {
    public int insertDataObject(int scenarioId, int scenarioInstanceId, int stateId,
                                 int dataclass_id, boolean locked) {
        String insertDataobject = "INSERT INTO dataobject (scenario_id, scenarioinstance_id, state_id," +
                " dataclass_id, locked) VALUES (%d, %d, %d, %d, %b);";
        insertDataobject = String.format(insertDataobject, scenarioId, scenarioInstanceId,
                stateId, dataclass_id, locked);
        DbObject dbObject = new DbObject();
        return dbObject.executeInsertStatement(insertDataobject);
    }

    public int insertDataAttribute(String name, String type, String defaultVal, int dataclassId) {
        String insertDataattribute = "INSERT INTO dataattribute (name, type, default, dataclass_id) " +
                "VALUES ('%s', '%s', '%s', %d);";
        insertDataattribute = String.format(
                insertDataattribute, name, type, defaultVal, dataclassId);
        return new DbObject().executeInsertStatement(insertDataattribute);
    }

    public int insertDataAttributeInstance(String value, int attributeId, int dataobjectId) {
        String insertDataattributeInstance = "INSERT INTO dataattributeinstance (" +
                "value, dataattribute_id, dataobject_id) VALUES ('%s', %d, %d);";
        insertDataattributeInstance = String.format(
                insertDataattributeInstance, value, attributeId, dataobjectId);
        return new DbObject().executeInsertStatement(insertDataattributeInstance);
    }

}
