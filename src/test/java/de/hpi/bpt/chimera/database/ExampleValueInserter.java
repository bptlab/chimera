package de.hpi.bpt.chimera.database;

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
        String insertDataattribute = "INSERT INTO dataattribute (dataattribute.name, " +
                "dataattribute.type, dataattribute.default, dataattribute.dataclass_id) " +
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

    public int insertDataClass(String name, boolean isEvent) {
        String insertDataclass = "INSERT INTO dataclass (name, is_event) VALUES ('%s', %d);";
        int event = isEvent ? 1 : 0;
        insertDataclass = String.format(insertDataclass, name, event);
        return new DbObject().executeInsertStatement(insertDataclass);
    }
}
