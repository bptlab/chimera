package de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors;

import com.jayway.jsonpath.JsonPath;
import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.events.DbStartQuery;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataClass;
import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbLogEntry;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.DataAttribute;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataManager;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataObject;
import de.uni_potsdam.hpi.bpt.bp2014.util.CollectionUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public class CaseStarter {

    static final Logger LOGGER = Logger.getLogger(CaseStarter.class);
    Map<Integer, String> attributeIdToJsonPath;

    public CaseStarter(int scenarioId, String queryId) {
        this.attributeIdToJsonPath = new DbStartQuery().getPathMappings(scenarioId).get(queryId);
    }

    public void startInstance(String json, ScenarioInstance scenarioInstance) {
        initializeDataObjects(scenarioInstance);
        List<DataAttributeInstance> dataAttributes =  new ArrayList<>(
                scenarioInstance.getDataManager().getAllDataAttributeInstances());
        writeDataAttributes(dataAttributes, json, scenarioInstance.getScenarioInstanceId());
    }

    /**
     * This method is responsible for instantiating all data classes, which have attributes
     * specified in the json path mapping. For each data class there will maximal be
     * one data object.
     */
    public void initializeDataObjects(ScenarioInstance scenarioInstance) {
        Map<Integer, List<Integer>> classToAttributes =
                new DbDataClass().getDataAttributesPerClass();
        Map<Integer, Integer> attributeToclass = CollectionUtil.invertMapping(classToAttributes);
        DataManager dataManager = scenarioInstance.getDataManager();
        for (int dataAttributeId : attributeIdToJsonPath.keySet()) {
            int dataclassId = attributeToclass.get(dataAttributeId);
            dataManager.initializeDataObject(dataclassId);
        }
    }

    public void writeDataAttributes(List<DataAttributeInstance> dataAttributes,
                                    String json, int scenarioInstanceId) {

        if ("{}".equals(json) && this.hasMapping()) {
            throw new IllegalStateException("Could not initialize attributes from empty json");
        }

        Map<Integer, DataAttributeInstance> idToDataAttributeInstance = dataAttributes
                .stream().collect(Collectors.toMap(
                        DataAttributeInstance::getDataAttributeInstanceId, x -> x));
        for (Map.Entry<Integer, String> idToPathEntry : attributeIdToJsonPath.entrySet()) {
            int dataAttributeInstanceId = idToPathEntry.getKey();
            DataAttributeInstance instance = idToDataAttributeInstance.get(dataAttributeInstanceId);
            String jsonPath = idToPathEntry.getValue();
            Object value = JsonPath.read(json, jsonPath);

            if (instance.isValueAllowed(value)) {
                instance.setValue(value);
                new DbLogEntry().logDataattributeCreation(
                        instance.getDataAttributeInstanceId(), value, scenarioInstanceId);
            } else {
                LOGGER.error("Attribute value could not be set because it has the wrong data type.");
            }
        }
    }



    public boolean hasMapping() {
        return this.attributeIdToJsonPath.size() > 0;
    }
}

