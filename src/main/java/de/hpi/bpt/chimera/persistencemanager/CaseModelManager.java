package de.hpi.bpt.chimera.persistencemanager;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.model.CaseModel;

public class CaseModelManager {

	private static Logger log = Logger.getLogger(CaseModelManager.class);
	private static boolean upToDate = false;

	private static Map<String, CaseModel> caseModelMap = new HashMap<String, CaseModel>();

	public static void makeOutdated() {
		upToDate = false;
	}

	public static void addCaseModel(CaseModel model) {
		boolean tmp = upToDate;
		try {
			DomainModelPersistenceManager.saveCaseModel(model);
			caseModelMap.put(model.getId(), model);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		upToDate = tmp;
	}

	public static CaseModel getCaseModel(String id) {
		CaseModel cm;
		try {
			if (caseModelMap.containsKey(id)) {
				cm = caseModelMap.get(id);
			} else {
				cm = DomainModelPersistenceManager.loadCaseModel(id);
			}
		} catch (Exception e) {
			log.error("Either CaseModel doesn't exist, or persistence error.");
			return null;
		}
		return cm;
	}

	public static void update() {
		for (CaseModel cm : DomainModelPersistenceManager.loadAllCaseModels()) {
			caseModelMap.put(cm.getId(), cm);
		}
		upToDate = true;
	}

	public static Map<String, Object> getScenarioDetails(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		CaseModel cm = getCaseModel(id);
		map.put("id", cm.getId());
		map.put("name", cm.getName());
		map.put("modelverion", cm.getVersionNumber());

		return map;
	}

	public static Map<String, String> getAllCaseModelInformation(String filter) {
		Map<String, String> CaseModelInformation = new HashMap<String, String>();

		if (!upToDate) {
			update();
		}
		for (Map.Entry<String, CaseModel> entry : caseModelMap.entrySet()) {
			if (entry.getValue().getName().contains(filter))
				CaseModelInformation.put(entry.getValue().getId(), entry.getValue().getName());
		}
		return CaseModelInformation;
	}


}
