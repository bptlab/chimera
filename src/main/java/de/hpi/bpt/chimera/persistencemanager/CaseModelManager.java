package de.hpi.bpt.chimera.persistencemanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.execution.exception.IllegalCaseModelIdException;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.condition.CaseStartTrigger;
import de.hpi.bpt.chimera.parser.CaseModelParser;
import de.hpi.bpt.chimera.parser.IllegalCaseModelException;

public class CaseModelManager {
	private static Logger log = Logger.getLogger(CaseModelManager.class);

	private static boolean isInstantiated = false;
	private static Map<String, CaseModel> caseModels = new HashMap<>();

	private CaseModelManager() {
	}

	private static void mayInstantiate() {
		if (!isInstantiated) {
			for (CaseModel cm : DomainModelPersistenceManager.loadAllCaseModels()) {
				caseModels.put(cm.getId(), cm);
			}
			log.info("updated CaseModels");

			isInstantiated = true;
		}
	}

	/**
	 * 
	 * @param cmId
	 * @return true if the CaseModel exists
	 */
	public static boolean isExistingCaseModel(String cmId) {
		mayInstantiate();
		return caseModels.containsKey(cmId);
	}

	/**
	 * Parse a CaseModel with a given String. Save the CaseModel and the updated
	 * EventMapper to the DataBase.
	 * 
	 * @param jsonString
	 * @return
	 */
	public static CaseModel parseCaseModel(String jsonString) {
		mayInstantiate();
		try {
			CaseModel cm = CaseModelParser.parseCaseModel(jsonString);
			caseModels.put(cm.getId(), DomainModelPersistenceManager.saveCaseModel(cm));
			log.info(String.format("new CaseModel: %s deployed", cm.getName()));
			return cm;
		} catch (IllegalArgumentException | JSONException | IllegalCaseModelException e) {
			throw e;
		}
	}

	/**
	 * Add a CaseModel and save it to the database.
	 * 
	 * @param cm
	 */
	public static void addCaseModel(CaseModel cm) {
		try {
			caseModels.put(cm.getId(), DomainModelPersistenceManager.saveCaseModel(cm));
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Receive the CaseModel for a specific id.
	 * 
	 * @param id
	 * @return CaseModel
	 * @throws IllegalCaseModelIdException
	 *             if the id is not assigned
	 */
	public static CaseModel getCaseModel(String id) {
		mayInstantiate();
		if (caseModels.containsKey(id)) {
			return caseModels.get(id);
		} else {
			// cm = DomainModelPersistenceManager.loadCaseModel(id);
			IllegalCaseModelIdException e = new IllegalCaseModelIdException(id);
			log.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Get all CaseModels of ExecutionService.
	 * 
	 * @return List of CaseModels
	 */
	public static List<CaseModel> getCaseModels() {
		mayInstantiate();
		Collection<CaseModel> caseModelList = caseModels.values();
		return new ArrayList<>(caseModelList);
	}

	/**
	 * Delete a certain CaseModel.
	 * 
	 * @param cmId
	 */
	public static void deleteCaseModel(String cmId) {
		mayInstantiate();
		if (caseModels.containsKey(cmId)) {
			try {
				EventDispatcher.deregisterEvents(caseModels.get(cmId));
				ExecutionService.deleteCaseModel(cmId);
				DomainModelPersistenceManager.deleteCaseModel(cmId);
				caseModels.remove(cmId);
			} catch (Exception e) {
				throw e;
			}
		} else {
			throw new IllegalArgumentException(String.format("CaseModel id: %s is not assigned", cmId));
		}
	}

}
