package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.exception.IllegalCaseIdException;
import de.hpi.bpt.chimera.execution.exception.IllegalCaseModelIdException;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;
import de.hpi.bpt.chimera.persistencemanager.DomainModelPersistenceManager;

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

	public ExecutionService() {
	}

	/**
	 * 
	 * @param caseId
	 * @return true when the case identified by this caseId exists, false
	 *         otherwise.
	 */
	public static boolean isExistingCase(String caseId) {
		if (cases.containsKey(caseId)) {
			return true;
		}
		Case caze = DomainModelPersistenceManager.loadCase(caseId);
		if (caze != null) {
			addCase(caze.getCaseExecutioner());
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param cmId
	 * @param caseId
	 * @return CaseExecutioner
	 */
	public static CaseExecutioner getCaseExecutioner(String cmId, String caseId) {
		if (!CaseModelManager.isExistingCaseModel(cmId) || !caseExecutions.containsKey(cmId)) {
			IllegalCaseModelIdException e = new IllegalCaseModelIdException(cmId);
			log.error(e.getMessage());
			throw e;
		}
		if (!cases.containsKey(caseId)) {
			Case caze = DomainModelPersistenceManager.loadCase(caseId);
			if (caze == null) {
				IllegalCaseIdException e = new IllegalCaseIdException(cmId);
				log.error(e.getMessage());
				throw e;
			} else {
				addCase(caze.getCaseExecutioner());
			}
		}

		return cases.get(caseId);
	}

	/**
	 * Start a Case of an CaseModel.
	 * 
	 * @param cmId
	 * @param name
	 *            for Case
	 * @return created CaseExecutioner
	 */
	public static CaseExecutioner createCaseExecutioner(String cmId, String name) {
		try {
			CaseModel cm = CaseModelManager.getCaseModel(cmId);

			// If name of Case isn't specified, use name of CaseModel
			String caseName = cm.getName();
			if (name == null || !name.isEmpty())
				caseName = name;

			CaseExecutioner caseExecutioner = new CaseExecutioner(cm, caseName);
			addCase(caseExecutioner);

			log.info(String.format("Successfully created Case with Case-Id: %s", caseExecutioner.getCase().getId()));
			return caseExecutioner;
		} catch (IllegalArgumentException e) {
			throw e;
		}
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
			IllegalCaseModelIdException e = new IllegalCaseModelIdException(cmId);
			log.error(e.getMessage());
			throw e;
		}
		log.info(String.format("Successfully requested all Case-Informations of CaseModel-Id: %s", cmId));
		return caseExecutions.get(cmId);
	}

	/**
	 * Delete a the Case Executions of a certain CaseModel.
	 * 
	 * @param cmId
	 */
	public static void deleteCaseModel(String cmId) {
		if (caseExecutions.containsKey(cmId)) {
			List<CaseExecutioner> executions = caseExecutions.get(cmId);
			for (CaseExecutioner caseExecutioner : executions) {
				// TODO: add deletion of case?
				cases.remove(caseExecutioner.getCase().getId(), caseExecutioner);
			}
			caseExecutions.remove(cmId);
		} else {
			log.info(String.format("CaseModel with id: %s is not assigned or hadn't any cases", cmId));
		}
	}

	public static List<CaseExecutioner> getAllExecutingCaseExecutioner() {
		return new ArrayList(cases.values());
	}
}
