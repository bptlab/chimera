package de.hpi.bpt.chimera.rest.beans.history;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CompleteLogJaxBean {
	private List<ActivityLog> activities;
	private List<DataObjectLog> dataobjects;
	private List<DataAttributeLog> attributes;

	public CompleteLogJaxBean(List<ActivityLog> activities, List<DataObjectLog> dataobjects, List<DataAttributeLog> attributes) {
		this.activities = activities;
		this.dataobjects = dataobjects;
		this.attributes = attributes;
	}

	public List<ActivityLog> getActivities() {
		return activities;
	}

	public void setActivities(List<ActivityLog> activities) {
		this.activities = activities;
	}

	public List<DataObjectLog> getDataobjects() {
		return dataobjects;
	}

	public void setDataobjects(List<DataObjectLog> dataobjects) {
		this.dataobjects = dataobjects;
	}

	public List<DataAttributeLog> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<DataAttributeLog> attributes) {
		this.attributes = attributes;
	}
}
