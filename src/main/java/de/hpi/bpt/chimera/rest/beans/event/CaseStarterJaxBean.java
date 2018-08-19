package de.hpi.bpt.chimera.rest.beans.event;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.model.condition.CaseStartTrigger;

@XmlRootElement
public class CaseStarterJaxBean {
	private String id;
	private String query;

	public CaseStarterJaxBean(CaseStartTrigger caseStarter) {
		id = caseStarter.getId();
		query = caseStarter.getQueryExecutionPlan();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
}
