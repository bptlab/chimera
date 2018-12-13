package de.hpi.bpt.chimera.rest.beans.casemodel;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.model.CaseModel;

import java.util.Date;

@XmlRootElement
public class CaseModelOverviewJaxBean {
	private String id;
	private String name;
	private String description;
	private int modelversion;
	private Date deployment;

	public CaseModelOverviewJaxBean(CaseModel cm) {
		setId(cm.getId());
		setName(cm.getName());
		setModelversion(cm.getVersionNumber());
		setDeployment(cm.getDeployment());
		setDescription(cm.getDescription());
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
}
