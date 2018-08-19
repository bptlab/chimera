package de.hpi.bpt.chimera.rest.beans.activity;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TerminateActivityJaxBean {
	private List<ObjectLifecycleTransitionJaxBean> transitions;
	private List<UpdateDataObjectJaxBean> attributeUpdates;

	public List<ObjectLifecycleTransitionJaxBean> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<ObjectLifecycleTransitionJaxBean> transitions) {
		this.transitions = transitions;
	}

	public List<UpdateDataObjectJaxBean> getAttributeUpdates() {
		return attributeUpdates;
	}

	public void setAttributeUpdates(List<UpdateDataObjectJaxBean> attributeUpdates) {
		this.attributeUpdates = attributeUpdates;
	}
}
