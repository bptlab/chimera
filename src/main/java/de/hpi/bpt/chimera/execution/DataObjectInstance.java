package de.hpi.bpt.chimera.execution;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.model.fragment.bpmn.DataNode;

public class DataObjectInstance {
	private String id;
	private DataNode dataNode;
	private CaseExecutioner caseExecutioner;
	private boolean locked;
	/**
	 * Map of id of DataAttributeInstance to DataAttributeInstance.
	 */
	private Map<String, DataAttributeInstance> dataAttributeInstances;

	/**
	 * 
	 * @param dataNode
	 * @param caseExecutioner
	 */
	public DataObjectInstance(DataNode dataNode, CaseExecutioner caseExecutioner) {
		this.id = UUID.randomUUID().toString().replace("-", "");
		this.locked = false;
		this.caseExecutioner = caseExecutioner;
		this.dataNode = dataNode;
		caseExecutioner.logDataObjectTransition(this, null, dataNode.getObjectLifecycleState());
		instantiateDataAttributes(dataNode.getDataClass(), caseExecutioner);
	}

	/**
	 * Constructor with predefined values for the DataAttributes.
	 * 
	 * @param dataNode
	 * @param attributeValues
	 */
	public DataObjectInstance(DataNode dataNode, CaseExecutioner caseExecutioner, Map<String, Object> attributeValues) {
		this.id = UUID.randomUUID().toString().replace("-", "");
		this.locked = false;
		this.caseExecutioner = caseExecutioner;
		this.dataNode = dataNode;
		caseExecutioner.logDataObjectTransition(this, null, dataNode.getObjectLifecycleState());
		instantiateDataAttributesWithValues(dataNode.getDataClass(), caseExecutioner, attributeValues);
	}

	/**
	 * Instantiate all DataAttribute of DataClass.
	 * 
	 * @param dataClass
	 */
	private void instantiateDataAttributes(DataClass dataClass, CaseExecutioner caseExecutioner) {
		instantiateDataAttributesWithValues(dataClass, caseExecutioner, new HashMap<>());
	}

	private void instantiateDataAttributesWithValues(DataClass dataClass, CaseExecutioner caseExecutioner, Map<String, Object> attributeValues) {
		dataAttributeInstances = new HashMap<>();
		for (DataAttribute attribute : dataClass.getDataAttributes()) {
			if (attributeValues.containsKey(attribute.getId())) {
				DataAttributeInstance attributeInstance = new DataAttributeInstance(attribute, caseExecutioner, attributeValues.get(attribute.getId()));
				dataAttributeInstances.put(attributeInstance.getId(), attributeInstance);
			} else {
				DataAttributeInstance attributeInstance = new DataAttributeInstance(attribute, caseExecutioner);
				dataAttributeInstances.put(attributeInstance.getId(), attributeInstance);
			}
		}
	}

	/**
	 * Edit value of DataAttribute Instances.
	 * 
	 * @param attributeValues
	 */
	public void setDataAttributeValues(Map<String, Object> attributeValues) {
		for (Map.Entry<String, Object> attributeValue : attributeValues.entrySet()) {
			String attributeInstanceId = attributeValue.getKey();
			if (dataAttributeInstances.containsKey(attributeInstanceId)) {
				DataAttributeInstance dataAttribute = dataAttributeInstances.get(attributeInstanceId);
				dataAttribute.setValue(attributeValue.getValue());
			}
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
		caseExecutioner.logDataObjectTransition(this, dataNode.getObjectLifecycleState());
		this.dataNode = dataNode;
	}

	public DataClass getDataClass() {
		return dataNode.getDataClass();
	}

	public ObjectLifecycleState getObjectLifecycleState() {
		return dataNode.getDataObjectState().getState();
	}

	public Map<String, DataAttributeInstance> getDataAttributeInstances() {
		return dataAttributeInstances;
	}

	public void setDataAttributeInstances(Map<String, DataAttributeInstance> dataAttributeInstances) {
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
