package de.hpi.bpt.chimera.rest.beans.casemodel;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MultipleCaseModelsJaxBean {
	private List<CaseModelOverviewJaxBean> casemodels;

	public MultipleCaseModelsJaxBean(List<CaseModelOverviewJaxBean> casemodels) {
		this.casemodels = casemodels;
	}

	public List<CaseModelOverviewJaxBean> getCasemodels() {
		return casemodels;
	}

	public void setCasemodels(List<CaseModelOverviewJaxBean> casemodels) {
		this.casemodels = casemodels;
	}
}
