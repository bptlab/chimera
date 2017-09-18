package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.hpi.bpt.chimera.execution.activity.AbstractActivityInstance;
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
	 * @return true if the CaseModel exists
	 */
	private static boolean isExistingCaseModel(String cmId) {
		return CaseModelManager.isExistingCaseModel(cmId);
	}

	/**
	 * Get specific Case.
	 * 
	 * @param cmId
	 * @param caseId
	 * @return Case
	 */
	public static Case getCase(String cmId, String caseId) {
		if (isExistingCase(cmId, caseId)) {
			return cases.get(caseId).getCase();
		} else {
			// TODO: throw exception
		}
		return null;
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
	 * @return caseId
	 */
	public static String startCase(String cmId, String name) {
		if (!isExistingCaseModel(cmId))
			throw new IllegalArgumentException("Id of CaseModel is not assigned.");
		CaseModel cm = CaseModelManager.getCaseModel(cmId);

		// If name of Case isn't specified, use name of CaseModel
		String caseName = cm.getName();
		if (!name.isEmpty())
			caseName = name;

		CaseExecutioner caseExecutioner = new CaseExecutioner(cm, caseName);
		String caseId = caseExecutioner.getCase().getId();
		cases.put(caseId, caseExecutioner);
		// check whether there are running CaseExecutions to the CaseModel
		if(caseExecutions.containsKey(cm.getId())) {
			List<CaseExecutioner> caseExecutioners = caseExecutions.get(cm.getId());
			caseExecutioners.add(caseExecutioner);
		} else {
			List<CaseExecutioner> caseExecutioners = new ArrayList<>();
			caseExecutioners.add(caseExecutioner);
			caseExecutions.put(cmId, caseExecutioners);
		}
		
		caseExecutioner.startCase();


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
	public static List<CaseExecutioner> getAllCasesOfCaseModel(String cmId) {
		if (!isExistingCaseModel(cmId))
			throw new IllegalArgumentException("Id of CaseModel is not assigned.");
		if (!caseExecutions.containsKey(cmId))
			return new ArrayList<>();
		log.info(String.format("Successfully requested all Case-Informations of CaseModel-Id: %s", cmId));
		return caseExecutions.get(cmId);
	}

	/**
	 * Begin a specific ActivityInstance.
	 * 
	 * @param cmId
	 * @param caseId
	 * @param activityInstanceId
	 * @param selectedDataObjectInstanceIds
	 */
	public static void beginActivityInstance(String cmId, String caseId, String activityInstanceId, List<String> selectedDataObjectInstanceIds) {
		if (isExistingCase(cmId, caseId)) {
			CaseExecutioner caseExecutioner = cases.get(caseId);
			caseExecutioner.beginActivityInstance(activityInstanceId, selectedDataObjectInstanceIds);
		} else {
			// throw exception
		}
	}

	/**
	 * Terminate a specific ActivityInstance.
	 * 
	 * @param cmId
	 * @param caseId
	 * @param activityInstanceId
	 * @param dataClassNameToState
	 */
	public static void terminateActivity(String cmId, String caseId, String activityInstanceId, Map<String, String> dataClassNameToState) {
		if (isExistingCase(cmId, caseId)) {
			CaseExecutioner caseExecutioner = cases.get(caseId);
			caseExecutioner.terminateActivityInstance(activityInstanceId, dataClassNameToState);
		} else {
			// throw exception
		}
	}

	/**
	 * Get an specific ActivityInstance.
	 * 
	 * @param cmId
	 * @param caseId
	 * @param activityInstanceId
	 * @return AbstractAcitivtyInstance
	 */
	public static AbstractActivityInstance getActivityInstance(String cmId, String caseId, String activityInstanceId) {
		if (isExistingCase(cmId, caseId)) {
			CaseExecutioner caseExecutioner = cases.get(caseId);
			return caseExecutioner.getActivityInstance(activityInstanceId);
		}
		return null;
	}

	/**
	 * Get all Instances of DataObjects of specific Case.
	 * 
	 * @param cmId
	 * @param caseId
	 * @return List of DataObjectInstance
	 */
	public static List<DataObjectInstance> getDataObjectInstances(String cmId, String caseId) {
		if (isExistingCase(cmId, caseId)) {
			CaseExecutioner caseExecutioner = cases.get(caseId);
			return caseExecutioner.getDataObjectInstances();
		} else {
			// exception
		}
		return new ArrayList<>();
	}

	/**
	 * Get a specific DataObjectInstance.
	 * 
	 * @param cmId
	 * @param caseId
	 * @param instanceId
	 * @return DataObjectInstance
	 */
	public static DataObjectInstance getDataObjectInstance(String cmId, String caseId, String instanceId) {
		if (isExistingCase(cmId, caseId)) {
			CaseExecutioner caseExecutioner = cases.get(caseId);
			return caseExecutioner.getDataObjectInstance(instanceId);
		}
		return null;
	}

	/**
	 * Get the available DataInput for an ActivityInstance.
	 * 
	 * @param cmId
	 * @param caseId
	 * @param activityInstanceId
	 * @return List of DataObjectInstance
	 */
	public static List<DataObjectInstance> getAvailableInputForAcitivityInstance(String cmId, String caseId, String activityInstanceId) {
		if (isExistingCase(cmId, caseId)) {
			CaseExecutioner caseExecutioner = cases.get(caseId);
			return caseExecutioner.getAvailableInputForAcitivityInstance(activityInstanceId);
		} else {
			// exception
		}
		return new ArrayList<>();
	}

	/**
	 * Returns whether a Case fulfills their TerminationCondition.
	 * 
	 * @param cmId
	 * @param caseId
	 * @return boolean
	 */
	public static boolean caseCanTerminate(String cmId, String caseId) {
		if (isExistingCase(cmId, caseId)) {
			CaseExecutioner caseExecutioner = cases.get(caseId);
			return caseExecutioner.canTerminate();
		} 
		return false;
	}
}
