package de.hpi.bpt.chimera.rest.beans.caze;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.execution.CaseExecutioner;

@XmlRootElement
public class CanTerminateJaxBean {
	private boolean canTerminate;

	public CanTerminateJaxBean(CaseExecutioner caseExecutioner) {
		canTerminate = caseExecutioner.canTerminate();
	}

	public boolean isCanTerminate() {
		return canTerminate;
	}

	public void setCanTerminate(boolean canTerminate) {
		this.canTerminate = canTerminate;
	}
}
