package de.hpi.bpt.chimera.rest.beans.datamodel;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.execution.data.DataObject;

@XmlRootElement
public class MultipleDataObjectsJaxBean {
	private List<DataObjectJaxBean> dataobjects;

	public MultipleDataObjectsJaxBean(List<DataObject> rawDataObjects) {
		dataobjects = rawDataObjects.stream()
						.map(DataObjectJaxBean::new)
						.collect(Collectors.toList());
	}
	public List<DataObjectJaxBean> getDataobjects() {
		return dataobjects;
	}

	public void setDataobjects(List<DataObjectJaxBean> dataobjects) {
		this.dataobjects = dataobjects;
	}
}
