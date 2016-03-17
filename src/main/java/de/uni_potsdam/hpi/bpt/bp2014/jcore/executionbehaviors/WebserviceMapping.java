package de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors;

import com.jayway.jsonpath.JsonPath;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbWebServiceTask;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.DataAttributeInstance;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class is represents an mapping between the response of an Webservice Task and
 * the corresponding DataAttributeInstances.
 */
public class WebserviceMapping {

    Map<String, String> attributeidToJsonPath;

    public WebserviceMapping(int scenarioId) {
        DbWebServiceTask dbWebServiceTask = new DbWebServiceTask();
        this.attributeidToJsonPath = dbWebServiceTask.retrieveWebserviceMapping(scenarioId);
    }

    public void writeDataObjects(String json, List<DataAttributeInstance> dataAttributeInstances) {
        Map<String, DataAttributeInstance> idToDataAttributeInstance = dataAttributeInstances
                .stream().collect(Collectors.toMap(DataAttributeInstance::getEditorId, x -> x));
        for (Map.Entry<String, String> sdf : attributeidToJsonPath.entrySet()) {
            String dataattributeInstanceId = sdf.getKey();
            DataAttributeInstance instance = idToDataAttributeInstance.get(dataattributeInstanceId);
            String jsonPath = sdf.getValue();
            Object value = JsonPath.read(json, jsonPath);
            instance.setValue(value);
        }
    }

    public void setAttributeidToJsonPath(Map<String, String> attributeidToJsonPath) {
        this.attributeidToJsonPath = attributeidToJsonPath;
    }
}
