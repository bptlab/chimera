package de.hpi.bpt.chimera.rest.beans.casemodel;

import java.util.Date;
import java.util.List;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.rest.beans.caze.CaseOverviewJaxBean;
import de.hpi.bpt.chimera.rest.beans.caze.MultipleCasesJaxBean;

public class CaseModelDetailsJaxBean extends CaseModelOverviewJaxBean {
	private int modelversion;
	private Date deployment;
	private List<CaseOverviewJaxBean> cases;

	public CaseModelDetailsJaxBean(CaseModel cm) {
		super(cm);
		setModelversion(cm.getVersionNumber());
		setDeployment(cm.getDeployment());
		List<CaseExecutioner> caseExecutions = ExecutionService.getAllCasesOfCaseModel(cm.getId());
		caseExecutions.sort((e1, e2) -> e1.getCase().getInstantiation().compareTo(e2.getCase().getInstantiation()));
		setCases(new MultipleCasesJaxBean(caseExecutions).getCases());
	}

	public int getModelversion() {
		return modelversion;
	}

	public void setModelversion(int modelversion) {
		this.modelversion = modelversion;
	}

	public Date getDeployment() {
		return deployment;
	}

	public void setDeployment(Date deployment) {
		this.deployment = deployment;
	}

	public List<CaseOverviewJaxBean> getCases() {
		return cases;
	}

	public void setCases(List<CaseOverviewJaxBean> cases) {
		this.cases = cases;
	}

}
