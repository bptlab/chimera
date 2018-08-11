package de.hpi.bpt.chimera.rest.beans.caze;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MultipleCasesJaxBean {
	private List<CaseOverviewJaxBean> cases;

	public MultipleCasesJaxBean(List<CaseOverviewJaxBean> cases) {
		this.cases = cases;
	}
	public List<CaseOverviewJaxBean> getCases() {
		return cases;
	}

	public void setCases(List<CaseOverviewJaxBean> cases) {
		this.cases = cases;
	}


}
