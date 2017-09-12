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
	public static boolean isExistingCase(String cmId, String caseId) {
		return (caseExecutions.containsKey(cmId) && cases.containsKey(caseId));
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

		// If name of Case isn't specified use name of CaseModel
		String caseName = cm.getName();
		if (!name.isEmpty())
			caseName = name;

		CaseExecutioner caseExecutioner = new CaseExecutioner(cm, caseName);
		log.info("CaseExecutioner created");
		caseExecutioner.startCase();
		log.info("Case started");
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
		cases.put(caseId, caseExecutioner);

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
	 * Get specific Case.
	 * 
	 * @param caseId
	 * @return Case
	 */
	public static Case getCase(String caseId) {
		if (cases.containsKey(caseId)) {
			return cases.get(caseId).getCase();
		} else {
			// TODO: throw exception
		}
		return null;
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
		if (cases.containsKey(caseId)) {
			CaseExecutioner caseExecutioner = cases.get(caseId);
			result.put("name", caseExecutioner.getCase().getName());
			// TODO: implement state of termination
			result.put("terminated", false);
		}

		return result;
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
}
