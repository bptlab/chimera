package de.hpi.bpt.chimera.rest.beans.activity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ObjectLifecycleTransitionJaxBean {
	private String dataclass;
	private String objectlifecyclestate;

	public String getDataclass() {
		return dataclass;
	}

	public void setDataclass(String dataclass) {
		this.dataclass = dataclass;
	}

	public String getObjectlifecyclestate() {
		return objectlifecyclestate;
	}

	public void setObjectlifecyclestate(String objectlifecyclestate) {
		this.objectlifecyclestate = objectlifecyclestate;
	}
}
