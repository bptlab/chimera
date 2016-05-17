package de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.events;

import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * .
 */
public class DbStartQuery extends DbObject {
    public List<String> getStartQueries(int scenarioId) {
        String sql = "SELECT * FROM startquery WHERE scenario_id = %d";
        List<String> queries = executeStatementReturnsListString(
                String.format(sql, scenarioId), "query");
        Set<String> uniqueQueries = new HashSet<>(queries);
        return new ArrayList<>(uniqueQueries);
    }

    public Map<String, Map<Integer, String>> getPathMappings(int scenarioId) {
        String sql = "SELECT * FROM startquery WHERE scenario_id = %d";
        sql = String.format(sql, scenarioId);
        java.sql.Connection connection = Connection.getInstance().connect();
        Map<String, Map<Integer, String>> queryIdToJsonMapping = new HashMap<>();
        try (Statement stat = connection.createStatement();
             ResultSet rs = stat.executeQuery(sql)) {
            while (rs.next()) {
                String queryId = rs.getString("id");
                if (!queryIdToJsonMapping.containsKey(queryId )) {
                    queryIdToJsonMapping.put(queryId, new HashMap<>());
                }
                int dataattributeId = rs.getInt("dataattribute_id");
                String jsonPath = rs.getString("jsonpath");
                queryIdToJsonMapping.get(queryId).put(dataattributeId, jsonPath);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queryIdToJsonMapping;
    }
}
