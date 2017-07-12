package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;

public class ExecutionService {
	private ExecutionService() {
	}

	// TODO: find out what throws convention error
	private static Map<String, List<CaseExecutioner>> caseExecutions = new HashMap<>();
	private static Map<String, CaseExecutioner> mapIdCase = new HashMap<>();

	public static String startCase(String cmId, String name) {
		CaseModel cm = CaseModelManager.getCaseModel(cmId);

		String caseName = cm.getName();
		if (name != "")
			caseName = name;

		CaseExecutioner caseExecutioner = new CaseExecutioner(cm, caseName);
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
		return caseId;
	}
	
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
		return allCases;
	}
}
