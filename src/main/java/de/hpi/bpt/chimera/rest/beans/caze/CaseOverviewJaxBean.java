package de.hpi.bpt.chimera.rest.beans.caze;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.execution.CaseExecutioner;

@XmlRootElement
public class CaseOverviewJaxBean {
	private String id;
	private String name;
	private boolean terminated;
	private Date instantiation;
	private boolean canTerminate;

	public CaseOverviewJaxBean(CaseExecutioner caseExecutioner) {
		setId(caseExecutioner.getCase().getId());
		setName(caseExecutioner.getCase().getName());
		setInstantiation(caseExecutioner.getCase().getInstantiation());
		setTerminated(caseExecutioner.isTerminated());
		setCanTerminate(caseExecutioner.canTerminate());
	}

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

	public boolean isTerminated() {
		return terminated;
	}

	public void setTerminated(boolean terminated) {
		this.terminated = terminated;
	}

	public Date getInstantiation() {
		return instantiation;
	}

	public void setInstantiation(Date instantiation) {
		this.instantiation = instantiation;
	}

	public boolean isCanTerminate() {
		return canTerminate;
	}

	public void setCanTerminate(boolean canTerminate) {
		this.canTerminate = canTerminate;
	}
}
