package de.hpi.bpt.chimera.rest.beans.casemodel;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;

@XmlRootElement
public class DataObjectStateConditionJaxBean {
	private String dataclass;
	private String state;

	public DataObjectStateConditionJaxBean(AtomicDataStateCondition condition) {
		setDataclass(condition.getDataClass().getName());
		setState(condition.getObjectLifecycleState().getName());
	}

	public String getDataclass() {
		return dataclass;
	}

	public void setDataclass(String dataclass) {
		this.dataclass = dataclass;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
