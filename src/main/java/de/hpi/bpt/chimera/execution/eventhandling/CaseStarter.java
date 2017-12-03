package de.hpi.bpt.chimera.execution.eventhandling;

import com.jayway.jsonpath.JsonPath;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.DataAttributeInstance;
import de.hpi.bpt.chimera.execution.DataManager;
import de.hpi.bpt.chimera.execution.DataObject;
import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
//import de.hpi.bpt.chimera.database.controlnodes.events.DbStartQuery;
//import de.hpi.bpt.chimera.database.history.DbLogEntry;
//import de.hpi.bpt.chimera.jcore.ScenarioInstance;
//import de.hpi.bpt.chimera.jcore.data.DataAttributeInstance;
//import de.hpi.bpt.chimera.jcore.data.DataManager;
import de.hpi.bpt.chimera.model.condition.CaseStartTrigger;
import de.hpi.bpt.chimera.model.condition.CaseStartTriggerConsequence;
import de.hpi.bpt.chimera.model.condition.DataAttributeJsonPath;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Responsible for
 */
public class CaseStarter {
	static final Logger LOGGER = Logger.getLogger(CaseStarter.class);
	private List<StartQueryPart> startQueryParts;
	private CaseStartTrigger caseStartTrigger;

	public CaseStarter(CaseStartTrigger caseStartTrigger) {
		this.caseStartTrigger = caseStartTrigger;
		// this.startQueryParts = dbStartQuery.loadStartQueryParts(queryId,
		// scenarioId);
	}

	public void startCase(String json, CaseExecutioner caseExecutioner) {
		initializeDataObjects(caseExecutioner, json);

		// TODO implement the access to the dataAttributeInstances
		// List<DataAttributeInstance> dataAttributes = new
		// ArrayList<>(caseExecutioner.getDataManager().getDataAttributeInstances);
		// writeDataAttributes(new ArrayList<DataAttributeInstance>(), json, caseExecutioner);
		caseExecutioner.startCase();
	}

	/**
	 * This method is responsible for instantiating all data classes, which have attributes
	 * specified in the json path mapping. For each data class there will maximal be
	 * one data object.
	 */
	public void initializeDataObjects(CaseExecutioner caseExecutioner, String json) {
		DataManager dataManager = caseExecutioner.getDataManager();
		for (CaseStartTriggerConsequence triggerConsequence : caseStartTrigger.getTriggerConsequences()) {
			LOGGER.info(String.format("initialize DataObject %s of Case %s in State %s.", triggerConsequence.getDataObjectState().getDataClass().getName(), caseExecutioner.getCase().getName(), triggerConsequence.getDataObjectState().getObjectLifecycleState().getName()));
			AtomicDataStateCondition condition = triggerConsequence.getDataObjectState();
			DataObject dataObject = dataManager.createDataObject(condition);
			for (DataAttributeJsonPath dataAttributeJsonPath : triggerConsequence.getMapping()) {
				writeDataAttributeInstances(dataObject, dataAttributeJsonPath, json);
			}
		}
	}

	private void writeDataAttributeInstances(DataObject dataObject, DataAttributeJsonPath dataAttributeJsonPath, String json) {
		for (DataAttributeInstance attributeInstance : dataObject.getDataAttributeInstanceIdToInstance().values()) {
			if (attributeInstance.getDataAttribute().equals(dataAttributeJsonPath.getDataAttribute())) {
				try {
					Object value = JsonPath.read(json, dataAttributeJsonPath.getJsonPath());
					attributeInstance.setValue(value);
				} catch (Exception e) {
					LOGGER.error("An Exception occured while parsing the given JSON according to the given JSON-Path. Maybe there is a mistake in the JSON-Path. " + e.getMessage());
					attributeInstance.setValue("ERROR");
					throw e;
				}
			}
		}
	}

	public void writeDataAttributes(List<DataAttributeInstance> dataAttributes, String json, CaseExecutioner caseExecutioner) {

		if (new JSONObject(json).length() == 0 && this.hasMapping()) {
			throw new IllegalStateException("Could not initialize attributes from empty json");
		}


		// Map<Integer, DataAttributeInstance> idToDataAttributeInstance =
		// dataAttributes.stream().collect(Collectors.toMap(DataAttributeInstance::getDataAttributeId,
		// x -> x));
		// TODO implement same as above with new DataModel, for now mocking
		// this:
		Map<Integer, DataAttributeInstance> idToDataAttributeInstance = new HashMap<Integer, DataAttributeInstance>();
		for (CaseStartTriggerConsequence triggerConsequence : caseStartTrigger.getTriggerConsequences()) {
			initializeFromPart(triggerConsequence, caseExecutioner, json, idToDataAttributeInstance);
		}
	}

	// TODO rename this method.
	private void initializeFromPart(CaseStartTriggerConsequence triggerConsequence, CaseExecutioner caseExecutioner, String json, Map<Integer, DataAttributeInstance> idToDataAttributeInstance) {
		// for (Map.Entry<Integer, String> idToPathEntry :
		// part.getAttributeIdToJsonPath().entrySet()) {
		for (DataAttributeJsonPath dataAttrJson : triggerConsequence.getMapping()) {
			// int dataAttributeId = idToPathEntry.getKey();
			String id = dataAttrJson.getDataAttribute().getId();
			// TODO: implement the following line using the new DataModel
			// DataAttributeInstance instance =
			// idToDataAttributeInstance.get(dataAttributeId);
			String jsonPath = dataAttrJson.getJsonPath();

			String value = "";
			try {
				value = JsonPath.read(json, jsonPath).toString();
			} catch (Exception e) {
				LOGGER.error("An Exception occured while parsing the given JSON according to the given JSON-Path. Maybe there is a mistake in the JSON-Path.");
				value = "ERROR";
				continue;
			}

			LOGGER.info(String.format("Here the DataAttribute %s of Dataclass %s should been set to %s.", dataAttrJson.getDataAttribute().getName(), triggerConsequence.getDataObjectState().getDataClass().getName(), value));
			// TODO: implement the following using the new DataModel
			/*
			 * if (instance.isValueAllowed(value)) { instance.setValue(value);
			 * new DbLogEntry().logDataAttributeTransition(instance.getId(),
			 * value, scenarioInstanceId); } else { LOGGER.
			 * error("Attribute value could not be set because it has the wrong data type."
			 * ); }
			 */
		}
	}


	private boolean hasMapping() {
		return this.caseStartTrigger.getTriggerConsequences().stream().filter(x -> x.getMapping().size() > 0).findAny().isPresent();
	}


}

