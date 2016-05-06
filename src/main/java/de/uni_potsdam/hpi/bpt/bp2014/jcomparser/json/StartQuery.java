package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
public class StartQuery {

    String query;
    Map<String, String> attributeToJsonPath = new HashMap<>();

    public StartQuery(JSONObject startQueryJson) {
        this.query = startQueryJson.getString("query");
        JSONObject pathMappings = startQueryJson.getJSONObject("mappings");
        for (Object key : pathMappings.keySet()) {
            attributeToJsonPath.put((String) key, pathMappings.getString((String) key));
        }
    }


    public void save(List<DataAttribute> dataAttributes, int scenarioId) {
        Map<String, Integer> editorToDbId = dataAttributes.stream()
                .collect(Collectors.toMap(
                        DataAttribute::getEditorId, DataAttribute::getDataAttributeID));

        Connector connector = new Connector();
        for (Map.Entry<String, String> entry : this.attributeToJsonPath.entrySet()) {
            int attributeDbId = editorToDbId.get(entry.getKey());
            connector.insertStartQueryIntoDatabase(
                    this.query, scenarioId, attributeDbId, entry.getValue());
        }
    }

    public Map<String, String> getAttributeToJsonPath() {
        return attributeToJsonPath;
    }

    public String getQuery() {
        return query;
    }

    public void register(int scenarioId) {
        EventDispatcher.registerCaseStartEvent(this.query, scenarioId);
    }
}
