package de.hpi.bpt.chimera.persistencemanager;

import java.util.ArrayList;
import java.util.Collection;
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

	public static CaseModel parseCaseModel(String jsonString) {
		mayInstantiate();
		try {
			CaseModel cm = CaseModelParser.parseCaseModel(jsonString);
			caseModels.put(cm.getId(), cm);
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
	 * caseModels.put(model.getId(), model); } catch (Exception e) { throw e; }
	 * }
	 */
	public static CaseModel getCaseModel(String id) {
		mayInstantiate();
		CaseModel cm;
		if (caseModels.containsKey(id)) {
			cm = caseModels.get(id);
		} else {
			cm = DomainModelPersistenceManager.loadCaseModel(id);
		}
		return cm;
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

	// TODO:
	public static void deleteCaseModel(String id) {
		mayInstantiate();
		if (caseModels.containsKey(id)) {
			DomainModelPersistenceManager.deleteCaseModel(caseModels.get(id));
			caseModels.remove(id);
		} else {
			throw new IllegalArgumentException(String.format("CaseModel %s id does not exist", id));
		}
	}

	/**
	 * Receive the TerminationCondition of a specific CaseModel.
	 * 
	 * @param cmId
	 * @return TerminationCondition
	 */
	public static TerminationCondition getTerminationConditionOfCaseModel(String cmId) {
		mayInstantiate();

		if (caseModels.containsKey(cmId)) {
			CaseModel cm = caseModels.get(cmId);
			return cm.getTerminationCondition();
		} else {
			// exception
		}
		return null;
	}

	/**
	 * Receive the Xml-Strings of all Fragments of a specific CaseModel.
	 * 
	 * @param cmId
	 * @return List of Xml-Strings of FragmentContent
	 */
	public static List<String> getFragmentXmlOfCaseModel(String cmId) {
		mayInstantiate();
		List<String> xmlStrings = new ArrayList<>();
		if (caseModels.containsKey(cmId)) {
			CaseModel cm = caseModels.get(cmId);
			for (Fragment fragment : cm.getFragments()) {
				xmlStrings.add(fragment.getContentXML());
			}
		} else {
			// exception
		}
		return xmlStrings;
	}
}
