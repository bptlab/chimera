package de.uni_potsdam.hpi.bpt.bp2014.database.history;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import java.util.Map;

/**
 *
 */
public class DbHistoryEvent extends DbObject {

    public Map<Integer, Map<String, Object>> getLogEntries(int scenarioInstanceId) {
        String sql =
                "SELECT cn.label as eventname, hi.id as id, hi.state as state,"
                + " hi.scenarioinstance_id as scenarioinstance_id, hi.eventid as eventid"
                + " FROM historyeventinstance as hi, controlnodeinstance as cni,"
                + " controlnode as cn WHERE scenarioinstance_id = %d AND hi.eventid = cni.id"
                + " AND cn.id = cni.controlnode_id;";
        return this.executeStatementReturnsMapWithMapWithKeys(String.format(sql, scenarioInstanceId)
                , "id", "state", "eventid", "scenarioinstance_id","eventname");
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
