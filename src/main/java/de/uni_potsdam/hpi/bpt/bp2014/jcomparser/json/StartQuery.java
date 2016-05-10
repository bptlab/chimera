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

    /**
     * Map from editor Id of data attribute to path mapping
     */
    private Map<String, String> attributeToJsonPath = new HashMap<>();


    private String id;
    private List<DataAttribute> dataAttributes;

    public StartQuery(JSONObject startQueryJson, List<DataClass> dataClasses) {
        this.query = startQueryJson.getString("query");
        JSONArray pathMappings = startQueryJson.getJSONArray("mapping");
        for (int i = 0; i < pathMappings.length(); i++) {
            JSONObject singleMapping = pathMappings.getJSONObject(i);
            String className = singleMapping.getString("classname");
            String attributeName = singleMapping.getString("attr");
            String dataattributeId = findDataattributeId(dataClasses, className, attributeName);
            attributeToJsonPath.put(dataattributeId, singleMapping.getString("path"));
        }
        dataAttributes = dataClasses.stream().map(DataClass::getAttributes)
                .flatMap(Collection::stream).collect(Collectors.toList());
        id = UUID.randomUUID().toString().replaceAll("\\-", "");
    }

    private String findDataattributeId(
            List<DataClass> dataClasses, String className, String attributeName) {
        Map<String, DataClass> classNameToDataclass = new HashMap<>();
        for (DataClass dataClass : dataClasses) {
            classNameToDataclass.put(dataClass.getName(), dataClass);
        }
        DataClass dataClass = classNameToDataclass.get(className);
        Optional<DataAttribute> dataAttribute = dataClass.getDataAttributeByName(
                attributeName);
        if (!dataAttribute.isPresent()) {
            throw new IllegalArgumentException(
                    "Invalid data attribute specified: " + attributeName);
        }
        return dataAttribute.get().getEditorId();
    }

    static List<StartQuery> parseStartQueries(
            JSONArray startQueryArray, List<DataClass> dataClasses) {
        List<StartQuery> startQueries = new ArrayList<>();
        for (int i = 0; i < startQueryArray.length(); i++) {
            JSONObject jsonObject = startQueryArray.getJSONObject(i);
            startQueries.add(new StartQuery(jsonObject, dataClasses));
        }
        return startQueries;
    }

    public void save(int scenarioId) {
        Map<String, Integer> editorToDbId = new HashMap<>();
        for (DataAttribute dataAttribute : dataAttributes) {
            editorToDbId.put(dataAttribute.getEditorId(), dataAttribute.getDataAttributeID());
        }

        Connector connector = new Connector();

        for (Map.Entry<String, String> entry : this.attributeToJsonPath.entrySet()) {
            int attributeDbId = editorToDbId.get(entry.getKey());
            connector.insertStartQueryIntoDatabase(
                    this.query, scenarioId, attributeDbId, entry.getValue(), id);
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

    public Map<String, String> getAttributeToJsonPath() {
        return attributeToJsonPath;
    }
}
