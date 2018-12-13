package de.hpi.bpt.chimera.rest.beans.casemodel;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.rest.beans.caze.CaseOverviewJaxBean;
import de.hpi.bpt.chimera.rest.beans.caze.MultipleCasesJaxBean;

import java.util.List;
import java.util.stream.Collectors;

public class CaseModelDetailsJaxBean extends CaseModelOverviewJaxBean {
	private List<CaseOverviewJaxBean> cases;
	private List<FragmentJaxBean> fragments;

	public CaseModelDetailsJaxBean(CaseModel cm) {
		super(cm);
		List<CaseExecutioner> caseExecutions = ExecutionService.getAllCasesOfCaseModel(cm.getId());
		setCases(new MultipleCasesJaxBean(caseExecutions).getCases());
		setFragments(cm.getFragments().stream()
						.map(FragmentJaxBean::new)
						.collect(Collectors.toList()));
	}

	public List<CaseOverviewJaxBean> getCases() {
		return cases;
	}

	public void setCases(List<CaseOverviewJaxBean> cases) {
		this.cases = cases;
	}

	public List<FragmentJaxBean> getFragments() {
		return fragments;
	}

	public void setFragments(List<FragmentJaxBean> fragments) {
		this.fragments = fragments;
	}
}
