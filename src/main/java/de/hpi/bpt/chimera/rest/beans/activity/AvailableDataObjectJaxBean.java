package de.hpi.bpt.chimera.rest.beans.activity;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.rest.beans.datamodel.DataAttributeInstanceJaxBean;

@XmlRootElement
public class AvailableDataObjectJaxBean {
	private String dataclassName;
	private String stateName;
	private List<String> availableStates;
	private List<DataAttributeInstanceJaxBean> attributeConfiguration;

	public AvailableDataObjectJaxBean(DataObject dataObject, List<ObjectLifecycleState> availableStateTransitions) {
		dataclassName = dataObject.getDataClass().getName();
		stateName = dataObject.getObjectLifecycleState().getName();
		availableStates = availableStateTransitions.stream()
							.map(ObjectLifecycleState::getName)
							.collect(Collectors.toList());
		attributeConfiguration = dataObject.getDataAttributeInstances().stream()
									.map(DataAttributeInstanceJaxBean::new)
									.collect(Collectors.toList());
	}
	
	public String getDataclassName() {
		return dataclassName;
	}

	public void setDataclassName(String dataclassName) {
		this.dataclassName = dataclassName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public List<String> getAvailableStates() {
		return availableStates;
	}

	public void setAvailableStates(List<String> availableStates) {
		this.availableStates = availableStates;
	}

	public List<DataAttributeInstanceJaxBean> getAttributeConfiguration() {
		return attributeConfiguration;
	}

	public void setAttributeConfiguration(List<DataAttributeInstanceJaxBean> attributeConfiguration) {
		this.attributeConfiguration = attributeConfiguration;
	}
}
