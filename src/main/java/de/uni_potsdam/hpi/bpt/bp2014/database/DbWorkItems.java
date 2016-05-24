package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.List;

/**
 * This class is used to save and retrieve which dataobjects an activity works with.
 */
public class DbWorkItems extends DbObject {
    public void saveWorkingItems(
            int scenarioInstanceId,int activityInstanceId, List<Integer> dataobjectIds) {
        String insertTemplate = "INSERT INTO workitem " +
                "(scenarioinstance_id, activityinstance_id, dataobject_id) VALUES " +
                "(%d, %d, %d);";
        for (Integer dataobjectId : dataobjectIds) {
            String sql = String.format(
                    insertTemplate, scenarioInstanceId, activityInstanceId, dataobjectId);
            this.executeInsertStatement(sql);
        }
    }

    public List<Integer> getWorkingItems(int scenarioInstanceId, int activityInstanceId) {
        String getWorkingItems = "Select * FROM workitem WHERE scenarioinstance_id = %d AND " +
                "activityinstance_id = %d;";
        getWorkingItems = String.format(getWorkingItems, scenarioInstanceId, activityInstanceId);
        return this.executeStatementReturnsListInt(getWorkingItems, "dataobject_id");
    }
}
