package de.hpi.bpt.chimera.persistencemanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.condition.DataObjectStateCondition;
import de.hpi.bpt.chimera.model.condition.TerminationCondition;
import de.hpi.bpt.chimera.model.condition.TerminationConditionComponent;
import de.hpi.bpt.chimera.model.fragment.Fragment;
import de.hpi.bpt.chimera.parser.CaseModelParser;
import de.hpi.bpt.chimera.parser.IllegalCaseModelException;

public class CaseModelManager {
	private static Logger log = Logger.getLogger(CaseModelManager.class);

	private static boolean isInstantiated = false;
	private static Map<String, CaseModel> caseModelMap = new HashMap<>();

	private CaseModelManager() {
	}

	private static void mayInstantiate() {
		if (!isInstantiated) {
			for (CaseModel cm : DomainModelPersistenceManager.loadAllCaseModels()) {
				caseModelMap.put(cm.getId(), cm);
			}
			log.info("updated CaseModels");
			isInstantiated = true;
		}
	}

	public static CaseModel parseCaseModel(String jsonString) {
		mayInstantiate();
		try {
			CaseModel cm = CaseModelParser.parseCaseModel(jsonString);
			caseModelMap.put(cm.getId(), cm);
			DomainModelPersistenceManager.saveCaseModel(cm);
			log.info(String.format("new CaseModel: %s deployed", cm.getName()));
			return cm;
		} catch (IllegalArgumentException | JSONException | IllegalCaseModelException e) {
			throw e;
		}
	}

	/*
	 * public static void addCaseModel(CaseModel model) { try {
	 * DomainModelPersistenceManager.saveCaseModel(model);
	 * caseModelMap.put(model.getId(), model); } catch (Exception e) { throw e;
	 * } }
	 */
	public static CaseModel getCaseModel(String id) {
		mayInstantiate();
		CaseModel cm;
		if (caseModelMap.containsKey(id)) {
			cm = caseModelMap.get(id);
		} else {
			cm = DomainModelPersistenceManager.loadCaseModel(id);
		}
		return cm;
	}

	public static Map<String, String> getAllCaseModelNameDetails(String filter) {
		mayInstantiate();

		Map<String, String> allCaseModelDetails = new HashMap<>();

		for (Map.Entry<String, CaseModel> entry : caseModelMap.entrySet()) {
			if (entry.getValue().getName().contains(filter)) {
				allCaseModelDetails.put(entry.getValue().getId(), entry.getValue().getName());
			}
		}
		log.info("get all CaseModelNameDetails");
		return allCaseModelDetails;
	}

	public static Map<String, Object> getCaseModelDetails(String id) {
		mayInstantiate();
		Map<String, Object> caseModelDetails = new HashMap<>();

		if (caseModelMap.containsKey(id)) {
			CaseModel cm = caseModelMap.get(id);
			caseModelDetails.put("id", cm.getId());
			caseModelDetails.put("name", cm.getName());
			caseModelDetails.put("modelversion", cm.getVersionNumber());
		}

		return caseModelDetails;

	}

	public static void deleteCaseModel(String id) {
		mayInstantiate();
		if (caseModelMap.containsKey(id)) {
			DomainModelPersistenceManager.deleteCaseModel(caseModelMap.get(id));
			caseModelMap.remove(id);
		} else {
			throw new IllegalArgumentException(String.format("CaseModel %s id does not exist", id));
		}
	}

	/**
	 * Receive information about the TerminationCondition of a specific
	 * CaseModel. Information contains name of affected DataObject and name of
	 * State.
	 * 
	 * @param cmId
	 * @return JSONObject with information
	 */
	public static JSONObject getTerminationConditionOfCaseModel(String cmId) {
		mayInstantiate();

		JSONObject result = new JSONObject();
		JSONObject conditions = new JSONObject();
		if (caseModelMap.containsKey(cmId)) {
			CaseModel cm = caseModelMap.get(cmId);
			TerminationCondition terminationCondition = cm.getTerminationCondition();
			int id = 1;
			for (TerminationConditionComponent component : terminationCondition.getConditions()) {
				JSONArray dataObjectStateConditions = new JSONArray();
				for (DataObjectStateCondition dosc : component.getConditions()) {
					JSONObject dataObjectStateCondition = new JSONObject();
					dataObjectStateCondition.put("data_object", dosc.getDataClass().getName());
					dataObjectStateCondition.put("state", dosc.getState().getName());
					dataObjectStateConditions.put(dataObjectStateCondition);
				}
				conditions.put(String.format("%d", id++), dataObjectStateConditions);
			}
		}
		result.put("conditions", conditions);
		return result;
	}

	/**
	 * Receive all Xml-Strings of all Fragments of a specific CaseModel.
	 * 
	 * @param cmId
	 * @return JSONObject with information
	 */
	public static JSONObject getFragmentXmlOfCaseModel(String cmId) {
		JSONObject result = new JSONObject();
		if (caseModelMap.containsKey(cmId)) {
			CaseModel cm = caseModelMap.get(cmId);
			JSONArray fragmentXmls = new JSONArray();
			for (Fragment fragment : cm.getFragments()) {
				fragmentXmls.put(fragment.getContentXML());
			}
			result.put("xml", fragmentXmls);
		}
		return result;
	}
}
