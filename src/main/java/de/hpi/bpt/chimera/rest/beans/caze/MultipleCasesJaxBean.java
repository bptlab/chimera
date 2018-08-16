package de.hpi.bpt.chimera.rest.beans.caze;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.execution.CaseExecutioner;

@XmlRootElement
public class MultipleCasesJaxBean {
	private List<CaseOverviewJaxBean> cases;

	public MultipleCasesJaxBean(List<CaseExecutioner> caseExecutioners) {
		this.cases = caseExecutioners.stream()
						.map(CaseOverviewJaxBean::new)
						.collect(Collectors.toList());
	}
	public List<CaseOverviewJaxBean> getCases() {
		return cases;
	}

	public void setCases(List<CaseOverviewJaxBean> cases) {
		this.cases = cases;
	}


}
