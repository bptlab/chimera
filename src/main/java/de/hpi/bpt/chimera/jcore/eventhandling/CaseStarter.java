package de.hpi.bpt.chimera.jcore.eventhandling;

import com.jayway.jsonpath.JsonPath;
import de.hpi.bpt.chimera.database.controlnodes.events.DbStartQuery;
import de.hpi.bpt.chimera.database.history.DbLogEntry;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.data.DataAttributeInstance;
import de.hpi.bpt.chimera.jcore.data.DataManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Responsible for
 */
public class CaseStarter {
	static final Logger LOGGER = Logger.getLogger(CaseStarter.class);
	private List<StartQueryPart> startQueryParts;

	public CaseStarter(int scenarioId, String queryId) {
		DbStartQuery dbStartQuery = new DbStartQuery();
		this.startQueryParts = dbStartQuery.loadStartQueryParts(queryId, scenarioId);
	}

	public void startInstance(String json, ScenarioInstance scenarioInstance) {
		initializeDataObjects(scenarioInstance);
		List<DataAttributeInstance> dataAttributes = new ArrayList<>(scenarioInstance.getDataManager().getDataAttributeInstances());
		writeDataAttributes(dataAttributes, json, scenarioInstance.getId());
	}

	/**
	 * This method is responsible for instantiating all data classes, which have attributes
	 * specified in the json path mapping. For each data class there will maximal be
	 * one data object.
	 */
	public void initializeDataObjects(ScenarioInstance scenarioInstance) {
		DataManager dataManager = scenarioInstance.getDataManager();
		for (StartQueryPart part : this.startQueryParts) {
			dataManager.initializeDataObject(part.getDataClassId(), part.getStartStateId());
		}
	}

	public void writeDataAttributes(List<DataAttributeInstance> dataAttributes, String json, int scenarioInstanceId) {

		if (new JSONObject(json).length() == 0 && this.hasMapping()) {
			throw new IllegalStateException("Could not initialize attributes from empty json");
		}

		Map<Integer, DataAttributeInstance> idToDataAttributeInstance = dataAttributes.stream().collect(Collectors.toMap(DataAttributeInstance::getDataAttributeId, x -> x));
		for (StartQueryPart part : this.startQueryParts) {
			initializeFromPart(part, scenarioInstanceId, json, idToDataAttributeInstance);
		}
	}

	private void initializeFromPart(StartQueryPart part, int scenarioInstanceId, String json, Map<Integer, DataAttributeInstance> idToDataAttributeInstance) {
		for (Map.Entry<Integer, String> idToPathEntry : part.getAttributeIdToJsonPath().entrySet()) {
			int dataAttributeId = idToPathEntry.getKey();
			DataAttributeInstance instance = idToDataAttributeInstance.get(dataAttributeId);
			String jsonPath = idToPathEntry.getValue();
			String value = JsonPath.read(json, jsonPath).toString();

			if (instance.isValueAllowed(value)) {
				instance.setValue(value);
				new DbLogEntry().logDataAttributeTransition(instance.getId(), value, scenarioInstanceId);
			} else {
				LOGGER.error("Attribute value could not be set because it has the wrong data type.");
			}
		}
	}


	private boolean hasMapping() {
		return this.startQueryParts.stream().filter(x -> x.getAttributeIdToJsonPath().size() > 0).findAny().isPresent();
	}


}

