package de.hpi.bpt.chimera.rest.beans.casemodel;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FragmentRepresentationJaxBean {
	private List<String> bpmn;

	public FragmentRepresentationJaxBean(List<String> bpmn) {
		this.bpmn = bpmn;
	}

	public List<String> getBpmn() {
		return bpmn;
	}

	public void setBpmn(List<String> bpmn) {
		this.bpmn = bpmn;
	}
}
