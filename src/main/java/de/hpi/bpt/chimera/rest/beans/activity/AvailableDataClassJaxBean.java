package de.hpi.bpt.chimera.rest.beans.activity;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.rest.beans.datamodel.DataAttributeJaxBean;

@XmlRootElement
public class AvailableDataClassJaxBean {
	private String dataclassName;
	private List<String> availableStates;
	private List<DataAttributeJaxBean> attributeConfiguration;

	public AvailableDataClassJaxBean(DataClass dataclass, List<ObjectLifecycleState> availableStateTransitions) {
		dataclassName = dataclass.getName();
		availableStates = availableStateTransitions.stream()
							.map(ObjectLifecycleState::getName)
							.collect(Collectors.toList());
		attributeConfiguration = dataclass.getDataAttributes().stream()
									.map(DataAttributeJaxBean::new)
									.collect(Collectors.toList());
	}

	public String getDataclassName() {
		return dataclassName;
	}

	public void setDataclassName(String dataclassName) {
		this.dataclassName = dataclassName;
	}

	public List<String> getAvailableStates() {
		return availableStates;
	}

	public void setAvailableStates(List<String> availableStates) {
		this.availableStates = availableStates;
	}

	public List<DataAttributeJaxBean> getAttributeConfiguration() {
		return attributeConfiguration;
	}

	public void setAttributeConfiguration(List<DataAttributeJaxBean> attributeConfiguration) {
		this.attributeConfiguration = attributeConfiguration;
	}
}
