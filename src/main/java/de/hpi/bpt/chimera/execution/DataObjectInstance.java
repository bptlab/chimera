package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.fragment.bpmn.DataNode;

public class DataObjectInstance {
	private String id;
	private DataNode dataNode;
	private List<DataAttributeInstance> dataAttributeInstances;
	private boolean locked;

	public DataObjectInstance(DataNode dataNode) {
		this.id = UUID.randomUUID().toString();
		this.dataNode = dataNode;
		this.locked = false;
		instantiateDataAttributes(dataNode.getDataObjectState().getDataClass());
	}

	/**
	 * Instantiate all DataAttribute of DataClass.
	 * 
	 * @param dataClass
	 */
	private void instantiateDataAttributes(DataClass dataClass) {
		dataAttributeInstances = new ArrayList<>();
		for (DataAttribute attribute : dataClass.getDataAttributes()) {
			DataAttributeInstance attributeInstance = new DataAttributeInstance(attribute);
			dataAttributeInstances.add(attributeInstance);
		}
	}

	// GETTER & SETTER
	public String getId() {
		return id;
	}

	public DataNode getDataNode() {
		return dataNode;
	}

	public void setDataNode(DataNode dataNode) {
		this.dataNode = dataNode;
	}

	public DataClass getDataClass() {
		return dataNode.getDataObjectState().getDataClass();
	}

	public List<DataAttributeInstance> getDataAttributeInstances() {
		return dataAttributeInstances;
	}

	public void setDataAttributeInstances(List<DataAttributeInstance> dataAttributeInstances) {
		this.dataAttributeInstances = dataAttributeInstances;
	}

	public boolean isLocked() {
		return locked;
	}

	public void lock() {
		this.locked = true;
	}

	public void unlock() {
		this.locked = false;
	}
}
