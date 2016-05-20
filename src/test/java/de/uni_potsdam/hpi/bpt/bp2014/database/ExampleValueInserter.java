package de.uni_potsdam.hpi.bpt.bp2014.database;

/**
 *
 */
public class ExampleValueInserter {
    public void insertDataObject(int scenarioId, int scenarioInstanceId, int stateId,
                                 int dataclass_id, boolean locked) {
        String insertDataobject = "INSERT INTO dataobject (scenario_id, scenarioinstance_id, state_id," +
                " dataclass_id, locked) VALUES (%d, %d, %d, %d, %b);";
        insertDataobject = String.format(insertDataobject, scenarioId, scenarioInstanceId,
                stateId, dataclass_id, locked);
        DbObject dbObject = new DbObject();
        dbObject.executeInsertStatement(insertDataobject);
    }

}
