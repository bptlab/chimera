package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.List;

import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.datamodel.DataClass;

public class DataObjectInstance {
	private DataClass dataClass;
	private List<DataAttributeInstance> dataAttributeInstances;

	public DataObjectInstance(DataClass dataClass) {
		this.setDataClass(dataClass);
		instantiate(dataClass);
	}

	private void instantiate(DataClass dataClass) {
		dataAttributeInstances = new ArrayList<>();
		for (DataAttribute attribute : dataClass.getDataAttributes()) {
			DataAttributeInstance attributeInstance = new DataAttributeInstance(attribute);
			dataAttributeInstances.add(attributeInstance);
		}
	}

	public DataClass getDataClass() {
		return dataClass;
	}

	public void setDataClass(DataClass dataClass) {
		this.dataClass = dataClass;
	}

	public List<DataAttributeInstance> getDataAttributeInstances() {
		return dataAttributeInstances;
	}

	public void setDataAttributeInstances(List<DataAttributeInstance> dataAttributeInstances) {
		this.dataAttributeInstances = dataAttributeInstances;
	}

}
