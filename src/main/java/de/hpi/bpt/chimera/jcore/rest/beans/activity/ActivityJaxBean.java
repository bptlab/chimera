package de.hpi.bpt.chimera.jcore.rest.beans.activity;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.execution.activity.AbstractActivityInstance;

/**
 *
 */
@XmlRootElement
public class ActivityJaxBean {
	private String id;
	private String label;
	private String state;

	public ActivityJaxBean(AbstractActivityInstance activityInstance) {
		setId(activityInstance.getId());
		setLabel(activityInstance.getControlNode().getName());
		setState(activityInstance.getState().getText().toUpperCase());
	}

	public String getId() {
		return id;
	}

	public void setId(String activityInstanceId) {
		this.id = activityInstanceId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}