package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.List;

/**
 * This class is used to save and retrieve the data objects selected by the user
 * to work on in an activity.
 */
public class DbSelectedDataObjects extends DbObject {
    public void saveDataObjectSelection(
            int scenarioInstanceId, int activityInstanceId, List<Integer> dataObjectIds) {
        String insertTemplate = "INSERT INTO dataobjectselection " +
                "(scenarioinstance_id, activityinstance_id, dataobject_id) VALUES " +
                "(%d, %d, %d);";
        for (Integer dataObjectId : dataObjectIds) {
            String sql = String.format(
                    insertTemplate, scenarioInstanceId, activityInstanceId, dataObjectId);
            this.executeInsertStatement(sql);
        }
    }

    public List<Integer> getDataObjectSelection(int scenarioInstanceId, int activityInstanceId) {
        String getWorkingItems = "Select * FROM dataobjectselection WHERE scenarioinstance_id = %d AND " +
                "activityinstance_id = %d;";
        getWorkingItems = String.format(getWorkingItems, scenarioInstanceId, activityInstanceId);
        return this.executeStatementReturnsListInt(getWorkingItems, "dataobject_id");
    }
}
