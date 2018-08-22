package de.hpi.bpt.chimera.rest.beans.casemodel;

import java.util.Date;

import de.hpi.bpt.chimera.model.CaseModel;

public class CaseModelDetailsJaxBean extends CaseModelOverviewJaxBean {
	private int modelversion;
	private Date deployment;

	public CaseModelDetailsJaxBean(CaseModel cm) {
		super(cm);
		setModelversion(cm.getVersionNumber());
		setDeployment(cm.getDeployment());
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
