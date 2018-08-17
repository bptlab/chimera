package de.hpi.bpt.chimera.rest.beans.event;

import java.util.List;
import java.util.stream.Collectors;

import de.hpi.bpt.chimera.model.condition.CaseStartTrigger;

public class MultipleCaseStartersJaxBean {
	private List<CaseStarterJaxBean> casestarters;


	public MultipleCaseStartersJaxBean(List<CaseStartTrigger> casestarters) {
		this.casestarters = casestarters.stream()
								.map(CaseStarterJaxBean::new)
								.collect(Collectors.toList());
	}

	public List<CaseStarterJaxBean> getCasestarters() {
		return casestarters;
	}

	public void setCasestarters(List<CaseStarterJaxBean> casestarters) {
		this.casestarters = casestarters;
	}
}
