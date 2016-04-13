package de.uni_potsdam.hpi.bpt.bp2014.database.history;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import java.util.Map;

/**
 *
 */
public class DbHistoryEvent extends DbObject {

    public Map<Integer, Map<String, Object>> getLogEntries(int scenarioInstanceId) {
        String sql = "SELECT * FROM historyeventinstance WHERE scenarioinstance_id = %d;";
        return this.executeStatementReturnsMapWithMapWithKeys(String.format(
                sql, scenarioInstanceId), "id", "eventid", "state", "scenarioinstance_id");
    }
    public int logEventRegistration(int eventInstanceId, int scenarioInstanceid) {
        String insertEvent = "INSERT INTO historyeventinstance " +
                "(`eventid`, `state`, `scenarioinstance_id`) VALUES (%d, 'registered', %d);";
        return this.executeInsertStatement(
                String.format(insertEvent, eventInstanceId, scenarioInstanceid));
    }

    public int logEventReceiving(int eventInstanceId, int scenarioInstanceid) {
        String insertEvent = "INSERT INTO historyeventinstance " +
                "(`eventid`, `state`, `scenarioinstance_id`) VALUES (%d, 'received', %d);";
        return this.executeInsertStatement(
                String.format(insertEvent, eventInstanceId, scenarioInstanceid));
    }
}
