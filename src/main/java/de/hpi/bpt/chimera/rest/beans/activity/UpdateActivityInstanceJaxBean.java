package de.hpi.bpt.chimera.rest.beans.activity;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UpdateActivityInstanceJaxBean {
	private List<UpdateDataObjectJaxBean> update;

	public List<UpdateDataObjectJaxBean> getUpdate() {
		return update;
	}

	public void setUpdate(List<UpdateDataObjectJaxBean> update) {
		this.update = update;
	}
}
