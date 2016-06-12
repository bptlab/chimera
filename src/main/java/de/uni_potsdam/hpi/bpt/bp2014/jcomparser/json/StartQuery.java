package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;
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
        JSONArray pathMappings = startQueryJson.getJSONArray("mappings");
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
        connector.insertStartQueryIntoDatabase(this.query, scenarioId, id);
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


    private class StartPart {

        final String dataClass;
        final String state;
        final String queryId;
        Map<String, String> attributeNameToJsonPath = new HashMap<>();

        public StartPart(JSONObject jsonObject, String queryId) {
            this.queryId = queryId;
            dataClass = jsonObject.getString("class");
            state = jsonObject.getString("state");
            JSONArray attributeMappings = jsonObject.getJSONArray("attributes");
            for (int i = 0; i < attributeMappings.length(); i++) {
                JSONObject attrMapping = attributeMappings.getJSONObject(i);
                String attrName = attrMapping.getString("name");
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
                    int attributeDbId = dataAttribute.get().getAttributeDatabaseId();
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
