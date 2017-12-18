package de.hpi.bpt.chimera.rest.beans.casemodel;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.model.CaseModel;

@XmlRootElement
public class CaseModelOverviewJaxBean {
	private String id;
	private String name;

	public CaseModelOverviewJaxBean(CaseModel cm) {
		setId(cm.getId());
		setName(cm.getName());
	}

	// GETTER & SETTER
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
