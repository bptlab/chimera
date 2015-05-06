package de.uni_potsdam.hpi.bpt.bp2014.database;

import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * This class creates and executes sql statements to get the terminationCondition for a scenario.
 */
public class DbTerminationCondition extends DbObject {
    static Logger log = Logger.getLogger(DbTerminationCondition.class.getName());

    /**
     * Creates a list of Conditions which shall terminate the scenario if they are fulfilled.
     *
     * @param scenario_id This is the database ID of a scenario.
     * @return a list of conditions which terminate the scenario if fulfilled.
     */
    public LinkedList<Condition> getConditionsForScenario(int scenario_id) {
        String sql = "SELECT conditionset_id, dataobject_id, state_id, scenario_id FROM terminationcondition WHERE scenario_id = " + scenario_id;
        return this.executeStatementReturnsListCondition(sql);
    }

    /**
     * Creates a list of ID's of conditionsSets for a scenario.
     *
     * @param scenario_id This is the database ID of a scenario.
     * @return a list of database ID's of conditionsSets.
     */
    public LinkedList<Integer> getConditionsSetIDsForScenario(int scenario_id) {
        String sql = "SELECT conditionset_id FROM terminationcondition WHERE scenario_id = " + scenario_id + " GROUP BY conditionset_id";
        return this.executeStatementReturnsListInt(sql, "conditionset_id");
    }

    /**
     * Creates a list of conditions from an ID of a scenario and the corresponding conditionSet.
     *
     * @param scenario_id     This is the database ID of a scenario.
     * @param conditionSet_id This is the database ID of a conditionSet which belongs to the scenario.
     * @return a list of conditions for a scenario and the corresponding conditionSet.
     */
    public LinkedList<Condition> getConditionsForConditionSetAndScenario(int scenario_id, int conditionSet_id) {
        String sql = "SELECT conditionset_id, dataobject_id, state_id, scenario_id FROM terminationcondition WHERE scenario_id = " + scenario_id + " AND conditionset_id = " + conditionSet_id;
        return this.executeStatementReturnsListCondition(sql);
    }

    /**
     * Returns a list of Hashmap with all the information
     * of a Termination Condition of one specific Scenario.
     * Tje Scenario is specified by the given id.
     *
     * @param scenarioID the id of the given Scenario
     */
    public Map<Integer, List<Map<String, Object>>> getDetailedConditionsForScenario(int scenarioID) {
        String sql = "SELECT t.conditionset_id AS set_id, s.name AS state, d.name AS data_object " +
                "FROM terminationcondition AS t, dataobject AS d, state AS s " +
                "WHERE t.dataobject_id = d.id AND t.state_id = s.id AND t.scenario_id = " + scenarioID;
        return this.executeStatementReturnsMapOfListOfMapsWithKeys(sql, "state", "data_object", "set_id");
    }

    /**
     * This method gets an sql statement and executes it to get a list of conditions.
     *
     * @param sql This is the sql Statement which shall be executed.
     * @return a list of conditions.
     */
    private LinkedList<Condition> executeStatementReturnsListCondition(String sql) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        LinkedList<Condition> results = new LinkedList<Condition>();
        if (conn == null) {
            return results;
        }

        try {
            //Execute a query
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                results.add(new Condition(rs.getInt("conditionset_id"), rs.getInt("state_id"), rs.getInt("dataobject_id"), rs.getInt("scenario_id")));
            }
            //Clean-up environment
            rs.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            log.error("SQL Error!: ", se);
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                log.error("SQL Error!: ", se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                log.error("SQL Error!: ", se);
            }
        }
        return results;
    }

    public Map<Integer, List<Map<String, Object>>> executeStatementReturnsMapOfListOfMapsWithKeys(String sql, String... keys) {
        java.sql.Connection conn = Connection.getInstance().connect();
        ResultSet results = null;
        Map<Integer, List<Map<String, Object>>> conditionSets = new HashMap<>();
        try {
            results = conn.prepareStatement(sql).executeQuery();
            while (results.next()) {
                Integer conditionSetId = results.getInt("set_id");
                if (!conditionSets.containsKey(conditionSetId)) {
                    conditionSets.put(conditionSetId, new LinkedList<Map<String, Object>>());
                }
                Map<String, Object> condition = new HashMap<>();
                for (String key : keys) {
                    condition.put(key, results.getObject(key));
                }
                conditionSets.get(conditionSetId).add(condition);
            }
        } catch (SQLException e) {
            log.error("SQL Error!: ", e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("SQL Error!: ", e);
            }
            try {
                results.close();
            } catch (SQLException e) {
                log.error("SQL Error!: ", e);
            }
        }
        return conditionSets;
    }
}
