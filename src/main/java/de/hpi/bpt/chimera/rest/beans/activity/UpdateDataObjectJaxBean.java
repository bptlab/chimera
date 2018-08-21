package de.hpi.bpt.chimera.rest.beans.activity;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UpdateDataObjectJaxBean {
	private String dataclass;
	private List<UpdateDataAttributeJaxBean> attributeUpdate;

	public String getDataclass() {
		return dataclass;
	}

	public void setDataclass(String dataclass) {
		this.dataclass = dataclass;
	}

	public List<UpdateDataAttributeJaxBean> getAttributeUpdate() {
		return attributeUpdate;
	}

	public void setAttributeUpdate(List<UpdateDataAttributeJaxBean> attributeUpdate) {
		this.attributeUpdate = attributeUpdate;
	}
}
