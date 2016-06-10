package de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.events;

import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.StartQueryPart;

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

    public List<StartQueryPart> loadStartQueryParts(String queryId, Integer scenarioId) {
        String sql = "SELECT * FROM startquery, startpart WHERE scenario_id = %d AND " +
                "startquery.id = startpart.query_id AND startpart.query_id = '%s'";
        sql = String.format(sql, scenarioId, queryId);
        Map<Integer, StartQueryPart> dataclassIdToStartQuery = new HashMap<>();
        java.sql.Connection connection = Connection.getInstance().connect();
        try (Statement stat = connection.createStatement();
             ResultSet rs = stat.executeQuery(sql)) {
            while (rs.next()) {
                int dataclassId = rs.getInt("dataclass");
                int stateId = rs.getInt("state");
                if (!dataclassIdToStartQuery.containsKey(dataclassId)) {
                    StartQueryPart startQueryPart = new StartQueryPart(dataclassId, stateId);
                    dataclassIdToStartQuery.put(dataclassId, startQueryPart);
                }
                int dataattributeId = rs.getInt("dataattribute_id");
                String jsonPath = rs.getString("jsonpath");
                StartQueryPart part = dataclassIdToStartQuery.get(dataclassId);
                part.addAttributeMapping(dataattributeId, jsonPath);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(dataclassIdToStartQuery.values());
    }

    public List<String> getQueryIds(int scenarioId) {
        String getQueryIds = "SELECT * FROM startquery, startpart WHERE " +
                " scenario_id = %d;";
        getQueryIds = String.format(getQueryIds, scenarioId);
        return this.executeStatementReturnsListString(getQueryIds, "id");
    }
}
