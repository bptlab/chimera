package de.uni_potsdam.hpi.bpt.bp2014.database;

/**
 *
 */
public class DbCaseStart extends DbEventMapping {
    public void insertCaseStartMapping(
            String requestId, int scenarioId, String notificationRuleId) {
        String sql = "INSERT INTO casestart (eventkey, notificationrule_id, scenario_id)" +
                " VALUES ('%s', '%s', %d)";
        sql = String.format(sql, requestId, notificationRuleId, scenarioId);
        this.executeInsertStatement(sql);
    }

    public void deleteCaseMapping(String requestKey) {
        String sql = "DELETE FROM casestart WHERE eventkey = %d";
        sql = String.format(sql, requestKey);
        this.executeUpdateStatement(sql);
    }

    public int getScenarioId(String requestKey) {
        String sql = "SELECT * FROM casestart WHERE eventkey = %d";
        sql = String.format(sql, requestKey);
        return this.executeStatementReturnsInt(sql, "scenario_id");
    }

    public String getNotificationPath(int scenarioId, String requestKey) {
        String sql = "SELECT * FROM casestart WHERE eventkey = '%s' AND scenario_id = %d";
        sql = String.format(sql, requestKey, scenarioId);
        return this.executeStatementReturnsString(sql, "notificationrule_id");
    }
}
