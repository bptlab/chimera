package de.hpi.bpt.chimera.execution.eventhandling;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.DataAttributeInstanceWriter;
import de.hpi.bpt.chimera.execution.DataManager;
import de.hpi.bpt.chimera.execution.DataObject;
import de.hpi.bpt.chimera.model.JsonPath;
import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.CaseStartTrigger;
import de.hpi.bpt.chimera.model.condition.CaseStartTriggerConsequence;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.Map;

/**
 * Responsible for
 */
public class CaseStarter {
	static final Logger LOGGER = Logger.getLogger(CaseStarter.class);
	private CaseStartTrigger caseStartTrigger;

	public CaseStarter(CaseStartTrigger caseStartTrigger) {
		this.caseStartTrigger = caseStartTrigger;
	}

	public void startCase(String json, CaseExecutioner caseExecutioner) {
		if (new JSONObject(json).length() == 0 && this.hasMapping()) {
			throw new IllegalStateException("Could not initialize attributes from empty json");
		}

		initializeDataObjects(caseExecutioner, json);
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
			AtomicDataStateCondition condition = triggerConsequence.getDataObjectState();
			LOGGER.info(String.format("initialize DataObject %s of Case %s in State %s.", condition.getDataClass().getName(), caseExecutioner.getCase().getName(), condition.getObjectLifecycleState().getName()));

			DataObject dataObject = dataManager.createDataObject(condition);
			Map<DataAttribute, JsonPath> dataAttributeToJsonPath = triggerConsequence.getDataAttributeToJsonPath();
			DataAttributeInstanceWriter.writeDataAttributeInstances(dataObject, dataAttributeToJsonPath, json);
		}
	}

	private boolean hasMapping() {
		return this.caseStartTrigger.getTriggerConsequences().stream().filter(x -> !x.getDataAttributeToJsonPath().isEmpty()).findAny().isPresent();
	}
}

