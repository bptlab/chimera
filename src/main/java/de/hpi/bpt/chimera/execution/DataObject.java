package de.hpi.bpt.chimera.execution;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hpi.bpt.chimera.execution.exception.IllegalObjectLifecycleStateSuccessorException;
import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

public class DataObject {
	private String id;
	private AtomicDataStateCondition condition;
	private DataManager dataManager;
	private boolean locked;
	/**
	 * Map of id of DataAttributeInstance to DataAttributeInstance.
	 */
	private Map<String, DataAttributeInstance> dataAttributeInstanceIdToInstance;

	/**
	 * 
	 * @param dataNode
	 * @param caseExecutioner
	 */
	public DataObject(AtomicDataStateCondition condition, DataManager dataManager) {
		this.id = UUID.randomUUID().toString().replace("-", "");
		this.setDataManger(dataManager);
		this.condition = new AtomicDataStateCondition(condition);
		getCaseExecutioner().logDataObjectTransition(this, null, condition.getObjectLifecycleState());
		instantiateDataAttributes(condition.getDataClass());
	}

	/**
	 * Clear the existing DataAttributeInstances and instantiate all
	 * DataAttributes of the DataClass.
	 * 
	 * @param dataClass
	 */
	private void instantiateDataAttributes(DataClass dataClass) {
		dataAttributeInstanceIdToInstance = new HashMap<>();
		for (DataAttribute attribute : dataClass.getDataAttributes()) {
			DataAttributeInstance attributeInstance = new DataAttributeInstance(attribute, this);
			dataAttributeInstanceIdToInstance.put(attributeInstance.getId(), attributeInstance);
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
			if (dataAttributeInstanceIdToInstance.containsKey(attributeInstanceId)) {
				DataAttributeInstance dataAttribute = dataAttributeInstanceIdToInstance.get(attributeInstanceId);
				dataAttribute.setValue(attributeValue.getValue());
			}
		}
	}

	/**
	 * Make the transition of the ObjectLifecycleState of the DataStateCondition
	 * in the DataObject. Therefore check if the new State is a successor of the
	 * current State. Otherwise throw an Exception. If everything is valid log
	 * the transition in the CaseExecutioner.
	 * 
	 * @param newObjectLifecycleState
	 */
	public void makeObjectLifecycleTransition(ObjectLifecycleState newObjectLifecycleState) {
		ObjectLifecycleState oldObjectLifecycleState = condition.getObjectLifecycleState();
		if (!newObjectLifecycleState.isSuccessorOf(oldObjectLifecycleState)) {
			throw new IllegalObjectLifecycleStateSuccessorException(condition.getDataClass(), oldObjectLifecycleState, newObjectLifecycleState);
		}
		getCaseExecutioner().logDataObjectTransition(this, newObjectLifecycleState);
		this.condition.setObjectLifecycleState(newObjectLifecycleState);
	}

	// GETTER & SETTER
	public String getId() {
		return id;
	}

	public AtomicDataStateCondition getCondition() {
		return condition;
	}

	public void setCondition(AtomicDataStateCondition condition) {
		getCaseExecutioner().logDataObjectTransition(this, condition.getObjectLifecycleState());
		this.condition = condition;
	}

	public DataClass getDataClass() {
		return condition.getDataClass();
	}

	public ObjectLifecycleState getObjectLifecycleState() {
		return condition.getObjectLifecycleState();
	}

	public Map<String, DataAttributeInstance> getDataAttributeInstances() {
		return dataAttributeInstanceIdToInstance;
	}

	public void setDataAttributeInstances(Map<String, DataAttributeInstance> dataAttributeInstances) {
		this.dataAttributeInstanceIdToInstance = dataAttributeInstances;
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
