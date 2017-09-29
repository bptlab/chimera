package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import de.hpi.bpt.chimera.execution.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;

/**
 * A static class that holds every running CaseExecution.
 */
public class ExecutionService {
	private static final Logger log = Logger.getLogger(ExecutionService.class);
	/**
	 * Map of CaseModelId to running CaseExecutions
	 */
	private static Map<String, List<CaseExecutioner>> caseExecutions = new HashMap<>();
	/**
	 * Map of CaseId to corresponding CaseExecution (Case)
	 */
	private static Map<String, CaseExecutioner> cases = new HashMap<>();

	private ExecutionService() {
	}

	/**
	 * 
	 * @param cmId
	 * @param caseId
	 * @return true if the Case exists
	 */
	private static boolean isExistingCase(String cmId, String caseId) {
		return CaseModelManager.isExistingCaseModel(cmId) && caseExecutions.containsKey(cmId) && cases.containsKey(caseId);
	}

	/**
	 * 
	 * @param cmId
	 * @param caseId
	 * @return CaseExecutioner
	 */
	public static CaseExecutioner getCaseExecutioner(String cmId, String caseId) {
		if (isExistingCase(cmId, caseId)) {
			return cases.get(caseId);
		}
		return null;
	}

	/**
	 * Start a Case of an CaseModel.
	 * 
	 * @param cmId
	 * @param name
	 *            for Case
	 * @return created CaseExecutioner
	 */
	public static CaseExecutioner startCase(String cmId, String name) {
		if (!CaseModelManager.isExistingCaseModel(cmId)) {
			return null;
		}
		CaseModel cm = CaseModelManager.getCaseModel(cmId);

		// If name of Case isn't specified, use name of CaseModel
		String caseName = cm.getName();
		if (name == null || !name.isEmpty())
			caseName = name;

		CaseExecutioner caseExecutioner = new CaseExecutioner(cm, caseName);
		addCase(caseExecutioner);

		caseExecutioner.startCase();

		log.info(String.format("Successfully started Case with Case-Id: %s", caseExecutioner.getCase().getId()));
		return caseExecutioner;
	}

	private static void addCase(CaseExecutioner caseExecutioner) {
		String caseId = caseExecutioner.getCase().getId();
		String cmId = caseExecutioner.getCaseModel().getId();
		cases.put(caseId, caseExecutioner);
		// check whether there are running CaseExecutions to the CaseModel
		if (caseExecutions.containsKey(cmId)) {
			List<CaseExecutioner> caseExecutioners = caseExecutions.get(cmId);
			caseExecutioners.add(caseExecutioner);
		} else {
			List<CaseExecutioner> caseExecutioners = new ArrayList<>();
			caseExecutioners.add(caseExecutioner);
			caseExecutions.put(cmId, caseExecutioners);
		}
	}

	/**
	 * Get all Cases of an CaseModel.
	 * 
	 * @param cmId
	 * @return List of all Cases
	 */
	public static List<CaseExecutioner> getAllCasesOfCaseModel(String cmId) {
		if (!CaseModelManager.isExistingCaseModel(cmId) || !caseExecutions.containsKey(cmId)) {
			return new ArrayList<>();
		}
		log.info(String.format("Successfully requested all Case-Informations of CaseModel-Id: %s", cmId));
		return caseExecutions.get(cmId);
	}
}
