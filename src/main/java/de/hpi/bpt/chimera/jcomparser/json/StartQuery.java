package de.hpi.bpt.chimera.jcomparser.json;

import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class StartQuery {
    private String query;

    private String id;

    List<StartPart> queryParts = new ArrayList<>();

    public StartQuery(JSONObject startQueryJson) {
        this.id = UUID.randomUUID().toString().replaceAll("\\-", "");
        this.query = startQueryJson.getString("query");
        JSONArray pathMappings = startQueryJson.getJSONArray("dataclasses");
        for (int i = 0; i < pathMappings.length(); i++) {
            JSONObject singleMapping = pathMappings.getJSONObject(i);
            queryParts.add(new StartPart(singleMapping, id));
        }
    }

    static List<StartQuery> parseStartQueries(JSONArray startQueryArray) {
        List<StartQuery> startQueries = new ArrayList<>();
        for (int i = 0; i < startQueryArray.length(); i++) {
            JSONObject jsonObject = startQueryArray.getJSONObject(i);
            startQueries.add(new StartQuery(jsonObject));
        }
        return startQueries;
    }

    public void save(int scenarioId, List<DataClass> dataClasses) {
        Connector connector = new Connector();
        connector.insertStartQuery(this.query, scenarioId, id);
        for (StartPart startPart : this.queryParts) {
            startPart.save(dataClasses);
        }
    }

    public void register(int scenarioId) {
        EventDispatcher.registerCaseStartEvent(this.query, scenarioId, id);
    }

    public String getQuery() {
        return query;
    }


    public String getId() {
        return id;
    }


    /**
     * Represents a single data object that get's created when the start query triggers.
     * Consists of the data class of the data object, the state it should be created in
     * and for each attribute an optional json path, how to initialise the data attribute
     * from the json of the start query event.
     */
    private class StartPart {
        final String dataClass;
        final String state;
        final String queryId;
        Map<String, String> attributeNameToJsonPath = new HashMap<>();

        public StartPart(JSONObject jsonObject, String queryId) {
            this.queryId = queryId;
            dataClass = jsonObject.getString("classname");
            state = jsonObject.getString("state");
            JSONArray attributeMappings = jsonObject.getJSONArray("mapping");
            for (int i = 0; i < attributeMappings.length(); i++) {
                JSONObject attrMapping = attributeMappings.getJSONObject(i);
                String attrName = attrMapping.getString("attr");
                String jsonPath = attrMapping.getString("path");
                attributeNameToJsonPath.put(attrName, jsonPath);
            }
        }

        /**
         * Saves the start part into the database.
         * @param dataClasses List of dataclasses which are already saved into database.
         */
        public void save(List<DataClass> dataClasses) {
            Map<String, DataClass> nameToDataClass = dataClasses.stream().collect(
                    Collectors.toMap(x -> x.getName(), x -> x));
            DataClass belongingDataClass = nameToDataClass.get(dataClass);
            int dataClassDbId = belongingDataClass.getDatabaseId();
            int stateDbId = belongingDataClass.getStateToDatabaseId().get(state);

            Connector connector = new Connector();
            if (this.attributeNameToJsonPath.size() == 0) {
                // TODO do not insert -1 and null but find a better way for start queries without attributes
                connector.insertStartPart(queryId, dataClassDbId, stateDbId, -1, "null");
            } else {
                for (Map.Entry<String, String> entry : this.attributeNameToJsonPath.entrySet()) {
                    Optional<DataAttribute> dataAttribute = belongingDataClass
                            .getDataAttributeByName(entry.getKey());
                    assert dataAttribute.isPresent() : "Referenced invalid data attribute";
                    int attributeDbId = dataAttribute.get().getId();
                    String jsonPath = entry.getValue();
                    connector.insertStartPart(
                            queryId, dataClassDbId, stateDbId, attributeDbId, jsonPath);
                }
            }
        }
    }

    public List<StartPart> getQueryParts() {
        return queryParts;
    }
}
