package de.hpi.bpt.chimera.execution;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hpi.bpt.chimera.model.condition.ConditionStatable;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.model.fragment.bpmn.DataNode;

public class DataObject implements ConditionStatable {
	private String id;
	private DataStateCondition condition;
	private DataManager dataManager;
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
	public DataObject(DataStateCondition condition, DataManager dataManager) {
		this.id = UUID.randomUUID().toString().replace("-", "");
		this.lock();
		this.setDataManger(dataManager);
		this.condition = new DataStateCondition(condition);
		// getCaseExecutioner().logDataObjectTransition(this, null,
		// dataNode.getObjectLifecycleState());
		instantiateDataAttributes(condition.getDataClass());
	}

	/**
	 * Clear the existing DataAttributeInstances and instantiate all
	 * DataAttributes of the DataClass.
	 * 
	 * @param dataClass
	 */
	private void instantiateDataAttributes(DataClass dataClass) {
		dataAttributeInstances = new HashMap<>();
		for (DataAttribute attribute : dataClass.getDataAttributes()) {
			DataAttributeInstance attributeInstance = new DataAttributeInstance(attribute, this);
			dataAttributeInstances.put(attributeInstance.getId(), attributeInstance);
		}
	}

	/**
	 * Constructor with predefined values for the DataAttributes.
	 * 
	 * @param dataNode
	 * @param attributeValues
	 */
	@Deprecated
	public DataObject(DataNode dataNode, CaseExecutioner caseExecutioner, Map<String, Object> attributeValues) {
		this.id = UUID.randomUUID().toString().replace("-", "");
		this.lock();
		// this.setCaseExecutioner(caseExecutioner);
		// this.dataNode = dataNode;
		caseExecutioner.logDataObjectTransition(this, null, dataNode.getObjectLifecycleState());
		instantiateDataAttributesWithValues(dataNode.getDataClass(), caseExecutioner, attributeValues);
	}

	/**
	 * Instantiate all DataAttribute of DataClass.
	 * 
	 * @param dataClass
	 */
	private void instantiateDataAttributes(DataClass dataClass, CaseExecutioner caseExecutioner) {

	}

	@Deprecated
	private void instantiateDataAttributesWithValues(DataClass dataClass, CaseExecutioner caseExecutioner, Map<String, Object> attributeValues) {
		dataAttributeInstances = new HashMap<>();
		for (DataAttribute attribute : dataClass.getDataAttributes()) {
			if (attributeValues.containsKey(attribute.getId())) {
				DataAttributeInstance attributeInstance = new DataAttributeInstance(attribute, caseExecutioner, attributeValues.get(attribute.getId()));
				dataAttributeInstances.put(attributeInstance.getId(), attributeInstance);
			} else {
				// DataAttributeInstance attributeInstance = new
				// DataAttributeInstance(attribute, caseExecutioner);
				// dataAttributeInstances.put(attributeInstance.getId(),
				// attributeInstance);
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

	public void setDataNode(DataNode dataNode) {
		getCaseExecutioner().logDataObjectTransition(this, dataNode.getObjectLifecycleState());
		// this.dataNode = dataNode;
	}

	@Override
	public DataStateCondition getCondition() {
		return condition;
	}

	public void setCondition(DataStateCondition condition) {
		this.condition = condition;
	}

	public DataClass getDataClass() {
		return condition.getDataClass();
	}

	public ObjectLifecycleState getObjectLifecycleState() {
		return condition.getState();
	}

	public Map<String, DataAttributeInstance> getDataAttributeInstances() {
		return dataAttributeInstances;
	}

	public void setDataAttributeInstances(Map<String, DataAttributeInstance> dataAttributeInstances) {
		this.dataAttributeInstances = dataAttributeInstances;
	}

	public void setDataManger(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	public DataManager getDataManager() {
		return dataManager;
	}

	public CaseExecutioner getCaseExecutioner() {
		return dataManager.getCaseExecutioner();
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
