package de.hpi.bpt.chimera.database.controlnodes.events;

import de.hpi.bpt.chimera.database.ConnectionWrapper;
import de.hpi.bpt.chimera.database.DbObject;
import de.hpi.bpt.chimera.jcore.eventhandling.CaseStarter;
import de.hpi.bpt.chimera.jcore.eventhandling.StartQueryPart;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Data Access Object for the start queries of scenarios.
 * Used by the {@link CaseStarter} to retrieve start query data for the execution.
 */
public class DbStartQuery extends DbObject {

    /**
     * Retrieves the Esper event queries registered for a given scenario.
     * @param scenarioId Id of the scenario to be checked.
     * @return The actual query strings of all start conditions modeled in the scenario.
     */
    public List<String> getStartQueries(int scenarioId) {
        String sql = "SELECT DISTINCT query FROM startquery WHERE scenario_id = %d";
        return  executeStatementReturnsListString(
                String.format(sql, scenarioId), "query");
    }

    /**
     * Load the query data for a given scenario and event query.
     * This includes data class, initial data object state, and attribute mapping.
     * @param queryId Id of the event query.
     * @param scenarioId Id of the scenario to be checked.
     * @return A List of all details for a given start query in the form of StartQueryParts.
     */
    public List<StartQueryPart> loadStartQueryParts(String queryId, Integer scenarioId) {
        String sql = "SELECT * FROM startquery, startpart WHERE scenario_id = %d AND " +
                "startquery.id = startpart.query_id AND startpart.query_id = '%s'";
        sql = String.format(sql, scenarioId, queryId);
        Map<Integer, StartQueryPart> dataClassIdToStartQuery = new HashMap<>();
        Connection connection = ConnectionWrapper.getInstance().connect();
        try (Statement stat = connection.createStatement();
             ResultSet rs = stat.executeQuery(sql)) {
            while (rs.next()) {
                int dataClassId = rs.getInt("dataclass");
                int stateId = rs.getInt("state");
                if (!dataClassIdToStartQuery.containsKey(dataClassId)) {
                    StartQueryPart startQueryPart = new StartQueryPart(dataClassId, stateId);
                    dataClassIdToStartQuery.put(dataClassId, startQueryPart);
                }
                int dataattributeId = rs.getInt("dataattribute_id");
                String jsonPath = rs.getString("jsonpath");
                StartQueryPart part = dataClassIdToStartQuery.get(dataClassId);
                part.addAttributeMapping(dataattributeId, jsonPath);
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return new ArrayList<>(dataClassIdToStartQuery.values());
    }
}
