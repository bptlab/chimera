package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;

/**
 * A static class that holds every running CaseExecution.
 */
public class ExecutionService {
	private static Logger log = Logger.getLogger(ExecutionService.class);
	/**
	 * Map of CaseModelId to running CaseExecutions
	 */
	private static Map<String, List<CaseExecutioner>> caseExecutions = new HashMap<>();
	/**
	 * Map of CaseId to corresponding CaseExecution
	 */
	private static Map<String, CaseExecutioner> mapIdCase = new HashMap<>();

	private ExecutionService() {
	}

	/**
	 * Start a Case of an CaseModel.
	 * 
	 * @param cmId
	 * @param name
	 *            of Case
	 * @return caseId
	 */
	public static String startCase(String cmId, String name) {
		CaseModel cm = CaseModelManager.getCaseModel(cmId);

		String caseName = cm.getName();
		if (name != "")
			caseName = name;

		CaseExecutioner caseExecutioner = new CaseExecutioner(cm, caseName);

		// check whether there are running CaseExecutions to the CaseModel
		if(caseExecutions.containsKey(cm.getId())) {
			List<CaseExecutioner> caseExecutioners = caseExecutions.get(cm.getId());
			caseExecutioners.add(caseExecutioner);
		} else {
			List<CaseExecutioner> caseExecutioners = new ArrayList<>();
			caseExecutioners.add(caseExecutioner);
			caseExecutions.put(cm.getId(), caseExecutioners);
		}
		
		String caseId = caseExecutioner.getCase().getId();
		mapIdCase.put(caseId, caseExecutioner);

		log.info(String.format("Successfully started Case with Case-Id: %s", caseId));
		return caseId;
	}
	
	/**
	 * Get all Cases of an CaseModel.
	 * 
	 * @param cmId
	 * @param filter
	 *            for Names of Cases
	 * @return Map of all Cases for Json
	 */
	public static Map<String, String> getAllCasesOfCaseModel(String cmId, String filter) {
		String filterString = "";
		if (filter != null)
			filterString = filter;
		Map<String, String> allCases = new HashMap<>();
		if (caseExecutions.containsKey(cmId)) {
			for (CaseExecutioner caseExecutioner : caseExecutions.get(cmId)) {
				Case caze = caseExecutioner.getCase();
				if (caze.getName().contains(filterString)) {
					allCases.put(caze.getId(), caze.getName());
				}
			}
		}

		log.info(String.format("Successfully requested all Case-Informations of CaseModel-Id: %s", cmId));
		return allCases;
	}

	/**
	 * Receive information of a specific Case. The information contains the name
	 * of the Case and the state of termination.
	 * 
	 * @param caseId
	 * @return JSONObject
	 */
	public static JSONObject getCaseInformation(String caseId) {
		JSONObject result = new JSONObject();
		if (mapIdCase.containsKey(caseId)) {
			CaseExecutioner caseExecutioner = mapIdCase.get(caseId);
			result.put("name", caseExecutioner.getCase().getName());
			// TODO: implement state of termination
			result.put("terminated", false);
		}

		return result;
	}

}
