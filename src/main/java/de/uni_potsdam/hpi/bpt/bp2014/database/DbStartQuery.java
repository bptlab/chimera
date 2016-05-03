package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.List;
import java.util.Map;

/**
 * .
 */
public class DbStartQuery extends DbObject {
    public String getStartQuery(int scenarioId) {
        String sql = "SELECT * FROM startquery WHERE scenario_id = %d";
        return executeStatementReturnsString(String.format(sql, scenarioId), "query");
    }

    public Map<Integer, String> getPathMappings(int scenarioId) {
        String sql = "SELECT * FROM startquery WHERE scenario_id = %d";
        return executeStatementReturnsMap(
                String.format(sql, scenarioId), "dataattribute_id", "jsonpath");
    }

}
