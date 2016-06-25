package de.uni_potsdam.hpi.bpt.bp2014.database;

import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.events.DbEventMapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 */
public class DbCaseStart extends DbEventMapping {
    public void insertCaseStartMapping(
            String requestId, int scenarioId, String notificationRuleId, String queryId) {
        String sql = "INSERT INTO casestart (eventkey, notificationrule_id, scenario_id, query_id)" +
                " VALUES ('%s', '%s', %d, '%s')";
        sql = String.format(sql, requestId, notificationRuleId, scenarioId, queryId);
        this.executeInsertStatement(sql);
    }

    public void deleteCaseMapping(String requestKey) {
        String sql = "SELECT * FROM casestart WHERE eventkey = '%s'";
        sql = String.format(sql, requestKey);
        this.executeUpdateStatement(sql);
    }

    public int getScenarioId(String requestKey) {
        String sql = "SELECT * FROM casestart WHERE eventkey = '%s'";
        sql = String.format(sql, requestKey);
        return this.executeStatementReturnsInt(sql, "scenario_id");
    }

    public String getQueryId(String requestKey) {
        String sql = "SELECT * FROM casestart WHERE eventkey = '%s'";
        sql = String.format(sql, requestKey);
        return this.executeStatementReturnsString(sql, "query_id");
    }

    public String getEventKey(String queryId) {
        String sql = "SELECT * FROM casestart WHERE query_id = '%s'";
        sql = String.format(sql, queryId);
        return executeStatementReturnsString(sql, "eventkey");
    }

    public String getNotificationRuleId(int scenarioId, String requestKey) {
        String sql = "SELECT * FROM casestart WHERE eventkey = '%s' AND scenario_id = %d";
        sql = String.format(sql, requestKey, scenarioId);
        return this.executeStatementReturnsString(sql, "notificationrule_id");
    }

    public List<String> getRequestKeys(int scenarioId) {
        String sql = "SELECT * FROM casestart WHERE scenario_id = %d";
        sql = String.format(sql, scenarioId);
        List<String> requestKeys = executeStatementReturnsListString(sql, "eventkey");
        return new ArrayList<>(new HashSet<>(requestKeys));
    }
}
