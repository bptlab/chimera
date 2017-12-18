package de.hpi.bpt.chimera.rest.beans.casemodel;

import de.hpi.bpt.chimera.model.CaseModel;

public class CaseModelDetailsJaxBean extends CaseModelOverviewJaxBean {
	private int modelversion;

	public CaseModelDetailsJaxBean(CaseModel cm) {
		super(cm);
		setModelversion(cm.getVersionNumber());
	}

	public int getModelversion() {
		return modelversion;
	}

	public void setModelversion(int modelversion) {
		this.modelversion = modelversion;
	}

}
