package de.hpi.bpt.chimera.execution;

import de.hpi.bpt.chimera.model.CaseModel;

public class CaseExecutioner {

	private Case caze;
	private CaseModel caseModel;
	private DataManager dataManager;

	public CaseExecutioner(CaseModel caseModel, String caseName) {
		this.setCaseModel(caseModel);
		this.caze = new Case(caseName, caseModel);
		this.dataManager = new DataManager(caze, caseModel.getDataModel());
	}

	public Case getCase() {
		return caze;
	}

	public void setCase(Case caze) {
		this.caze = caze;
	}

	public CaseModel getCaseModel() {
		return caseModel;
	}

	public void setCaseModel(CaseModel caseModel) {
		this.caseModel = caseModel;
	}

	public DataManager getDataManager() {
		return dataManager;
	}

	public void setDataManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}
}
