package de.hpi.bpt.chimera.database;

import de.hpi.bpt.chimera.database.controlnodes.events.DbEventMapping;
import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Database access object used by the {@link EventDispatcher}
 * for retrieving case start condition data.
 */
public class DbCaseStart extends DbEventMapping {
    private static final String SELECT_BY_EVENT_KEY = "SELECT * FROM casestart WHERE eventkey = '%s'";

    /**
     * Inserts a new case start query into the database.
     *
     * @param requestId if to find the id for the event.
     * @param scenarioId the id of the scenario, which the case start refers to.
     * @param notificationRuleId the id of the callback registered in Unicorn.
     * @param queryId the id of the case start query.
     */
    public void insertCaseStartMapping(
            String requestId, int scenarioId, String notificationRuleId, String queryId) {
        String sql = "INSERT INTO casestart (eventkey, notificationrule_id, scenario_id, query_id)" +
                " VALUES ('%s', '%s', %d, '%s')";
        sql = String.format(sql, requestId, notificationRuleId, scenarioId, queryId);
        this.executeInsertStatement(sql);
    }

    /**
     * Deletes a case mapping for a request key.
     * @param requestKey the key under which the case mapping is saved.
     */
    public void deleteCaseMapping(String requestKey) {
        String sql = String.format(SELECT_BY_EVENT_KEY, requestKey);
        this.executeUpdateStatement(sql);
    }

    /**
     * Retrieves the scenario Id for a request key.
     * @param requestKey the key under which the case mapping is saved.
     */
    public int getScenarioId(String requestKey) {
        String sql = String.format(SELECT_BY_EVENT_KEY, requestKey);
        return this.executeStatementReturnsInt(sql, "scenario_id");
    }

    /**
     * Retrieves the query Id for a request key.
     * @param requestKey the key under which the case mapping is saved.
     */
    public String getQueryId(String requestKey) {
        String sql = String.format(SELECT_BY_EVENT_KEY, requestKey);
        return this.executeStatementReturnsString(sql, "query_id");
    }

    /**
     * Retrieves the event key for a query Id.
     * @param queryId the query id saved in the mapping.
     */
    public String getEventKey(String queryId) {
        String sql = "SELECT * FROM casestart WHERE query_id = '%s'";
        sql = String.format(sql, queryId);
        return executeStatementReturnsString(sql, "eventkey");
    }

    /**
     * Retrieves the notification rule id for a request key.
     * A scenario needs to be specified since the same notification rule
     * can be referenced in different scenarios.
     */
    public String getNotificationRuleId(int scenarioId, String requestKey) {
        String sql = "SELECT * FROM casestart WHERE eventkey = '%s' AND scenario_id = %d";
        sql = String.format(sql, requestKey, scenarioId);
        return this.executeStatementReturnsString(sql, "notificationrule_id");
    }

    /**
     * Collect all event keys saved for a specific scenario.
     */
    public List<String> getRequestKeys(int scenarioId) {
        String sql = "SELECT * FROM casestart WHERE scenario_id = %d";
        sql = String.format(sql, scenarioId);
        List<String> requestKeys = executeStatementReturnsListString(sql, "eventkey");
        return new ArrayList<>(new HashSet<>(requestKeys));
    }
}
