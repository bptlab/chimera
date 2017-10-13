package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataModel;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractDataControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.DataNode;

/**
 * I manage the data objects of a case.
 */
public class DataManager {

	private CaseExecutioner caseExecutioner;
	private DataModel dataModel;
	/**
	 * Map of Id of DataObjectInstance to DataObjectInstance
	 */
	private Map<String, DataObject> dataObjects;

	public DataManager(DataModel dataModel, CaseExecutioner caseExecutioner) {
		this.setDataModel(dataModel);
		this.setCaseExecutioner(caseExecutioner);
		this.dataObjects = new HashMap<>();
	}

	/**
	 * Create new DataObjects.
	 * 
	 * @param outgoingDataNodes
	 * @param dataManagerBean
	 */
	public void createDataObject(List<DataNode> outgoingDataNodes, DataManagerBean dataManagerBean) {
		for (String newCreationId : dataManagerBean.getNewCreations()) {
			for (DataNode dataNode : outgoingDataNodes) {
				if (dataNode.getId().equals(newCreationId)) {
					Map<String, Object> attributeValues = dataManagerBean.getDataNodeAttributeValuesById(newCreationId);
					DataObject dataObjectInstance = new DataObject(dataNode, caseExecutioner, attributeValues);
					this.dataObjects.put(dataObjectInstance.getId(), dataObjectInstance);
				}
			}
		}
	}

	/**
	 * Create new DataObjectInstances with the outgoing DataNodes of an specific
	 * ControlNode.
	 * TODO: what about input DOs?
	 * @param controlNode
	 */
	@Deprecated
	public void createDataObject(AbstractDataControlNode controlNode) {
		/*
		for (DataNode dataNode : controlNode.getOutgoingDataNodes()) {
			DataObject dataObjectInstance = new DataObject(dataNode, caseExecutioner);
			dataObjects.put(dataObjectInstance.getId(), dataObjectInstance);
		}
		*/
	}

	/**
	 * Handle the transition of DataNodes.
	 * 
	 * @param dataManagerBean
	 */
	public void transitionDataObject(List<DataNode> outgoingDataNodes, DataManagerBean dataManagerBean) {
		for (Map.Entry<String, String> transition : dataManagerBean.getTransitions().entrySet()) {
			String dataObjectId = transition.getKey();
			String dataNodeId = transition.getValue();
			if (!dataObjects.containsKey(dataObjectId))
				continue;
			DataObject objectInstance = dataObjects.get(dataObjectId);
			for (DataNode dataNode : outgoingDataNodes) {
				if (dataNode.getId().equals(dataNodeId)) {
					objectInstance.setDataNode(dataNode);

					Map<String, Object> attributeValues = dataManagerBean.getDataObjectAttributeValuesById(dataObjectId);
					objectInstance.setDataAttributeValues(attributeValues);
				}
			}
		}
	}

	/**
	 * Lock the Instances of DataObjects. And returns these Instances.
	 * 
	 * @param List
	 *            of dataObjectInstanceIds
	 * @return List of locked DataObjects
	 */
	public List<DataObject> lockDataObjects(List<String> dataObjectInstanceIds) {
		List<DataObject> lockedDataObjects = new ArrayList<>();
		for (String id : dataObjectInstanceIds) {
			if (dataObjects.containsKey(id)) {
				DataObject dataObject = dataObjects.get(id);
				dataObject.lock();
				lockedDataObjects.add(dataObject);
			}
		}
		return lockedDataObjects;
	}

	/**
	 * Unlock the Instances of DataObjects.
	 * 
	 * @param toUnlockDataObjectInstances
	 */
	public void unlockDataObjects(List<DataObject> toUnlockDataObjectInstances) {
		// toUnlockDataObjectInstances.values().forEach(DataObjectInstance::unlock);
		for (DataObject dataObjectToUnlock : toUnlockDataObjectInstances) {
			if (dataObjects.containsKey(dataObjectToUnlock.getId())) {
				dataObjectToUnlock.unlock();
			}
		}
	}

	
	/**
	 * Get DataObjectInstances that are instantiated by the DataNodes.
	 * 
	 * TODO get all DOs that fulfill the DataStateCondition of the DataNodes
	 * 
	 * @param dataNodesToCheck
	 * @return List of DataObjectInstances
	 */
	/*
	@Deprecated
	public List<DataObject> getExistingDataObjects(List<DataNode> dataNodesToCheck) {
		// create a Map of DataNode to DataObjectInstance
		Map<DataNode, DataObject> dataNodeToInstanceAssociation = new HashMap<>();
		for (DataObject instance : dataObjects.values())
			dataNodeToInstanceAssociation.put(instance.getDataNode(), instance);
		// resolve Map
		List<DataObject> existingInstances = new ArrayList<>();
		for (DataNode dataNode : dataNodesToCheck) {
			if (dataNodeToInstanceAssociation.containsKey(dataNode))
				existingInstances.add(dataNodeToInstanceAssociation.get(dataNode));
		}
		return existingInstances;
	}
	*/
	/**
	 * Get all ConditionSets of the PreCondition of an AbstractDataControlNode
	 * that are fulfill the existing Conditions.
	 * 
	 * @param controlNode
	 * @return
	 */
	public List<ConditionSet> getAvailableConditions(AbstractDataControlNode controlNode) {
		List<ConditionSet> availableCondititionSets = new ArrayList<>();
		for (ConditionSet conditionSet : controlNode.getPreCondition()) {
			if (conditionSet.isFulfilled(getDataObjects())) {
				availableCondititionSets.add(conditionSet);
			}
		}
		return availableCondititionSets;
	}

	/**
	 * Get all DataObjects that have the same DataStateCondition (equal
	 * DataClass and ObjectLifecycleState) as the available conditions of the
	 * preCondition with no duplicates.
	 * 
	 * @param controlNode
	 * @return
	 */
	public Set<DataObject> getAvailableDataObjects(AbstractDataControlNode controlNode) {
		List<ConditionSet> availableCondititionSets = getAvailableConditions(controlNode);
		return getAvailableDataObjects(availableCondititionSets);
	}

	/**
	 * Get all DataObjects that have the same DataStateCondition (equal
	 * DataClass and ObjectLifecycleState) as the conditions in the
	 * conditionSets with no duplicates.
	 * 
	 * @param conditionSets
	 * @return
	 */
	public Set<DataObject> getAvailableDataObjects(List<ConditionSet> conditionSets) {
		Set<DataObject> availableDataObjects = new HashSet<>();
		for (ConditionSet conditionSet : conditionSets) {
			for (DataStateCondition condition : conditionSet.getConditions()) {
				Set<DataObject> availableDataObjectsForCondition = getAvailableDataObjects(condition);
				availableDataObjects.addAll(availableDataObjectsForCondition);
			}
		}

		return availableDataObjects;
	}

	/**
	 * Get all DataObjects that have the same DataStateCondition (equal
	 * DataClass and ObjectLifecycleState) as condition.
	 * 
	 * @param condition
	 * @return
	 */
	public Set<DataObject> getAvailableDataObjects(DataStateCondition condition) {
		Set<DataObject> availableDataObjects = new HashSet<>();

		for (DataObject dataObject : dataObjects.values()) {
			if (condition.equals(dataObject.getCondition())) {
				availableDataObjects.add(dataObject);
			}
		}

		return availableDataObjects;
	}
	/**
	 * Check whether a ControlNode is DataFlow enabled.
	 * 
	 * @param controlNode
	 * @return boolean
	 */
	public boolean isDataFlowEnabled(AbstractDataControlNode controlNode) {
		List<ConditionSet> preCondition = controlNode.getPreCondition();
		if (preCondition.isEmpty()) {
			return true;
		}

		return isExistingDataObject(preCondition);
	}

	/**
	 * Check whether the DataNodes that shell be checked are instantiated by the
	 * existing DataObjectInstances.
	 * 
	 * @param condition
	 * @return boolean
	 */
	private boolean isExistingDataObject(List<ConditionSet> conditions) {
		// TODO: make sure that even for DataNodes that occur twice exists two
		// Instances.
		if (conditions.isEmpty()) {
			return true;
		}

		List<DataObject> existingConditions = getDataObjects();
		for (ConditionSet condition : conditions) {
			if (condition.isFulfilled(existingConditions)) {
				return true;
			}
		}

		return false;

		/*
		 * List<DataNode> existingDataNodes = new ArrayList<>();
		 * 
		 * for (DataObject dataObjectInstance : dataObjects.values()) {
		 * existingDataNodes.add(dataObjectInstance.getDataNode()); } for
		 * (DataNode dataNodeToCheck : condition) { if
		 * (existingDataNodes.contains(dataNodeToCheck)) {
		 * existingDataNodes.remove(dataNodeToCheck); } else { return false; } }
		 * return true;
		 */
	}

	/**
	 * Set value of DataObjects.
	 * 
	 * @param dataAttributeValues
	 */
	public void setDataAttributeValues(Map<String, Map<String, Object>> dataAttributeValues) {
		for (Map.Entry<String, Map<String, Object>> transition : dataAttributeValues.entrySet()) {
			String dataObjectId = transition.getKey();
			if (!dataObjects.containsKey(dataObjectId))
				continue;
			DataObject dataObject = dataObjects.get(dataObjectId);
			Map<String, Object> attributeValues = transition.getValue();
			dataObject.setDataAttributeValues(attributeValues);
		}
	}

	/**
	 * Returns whether the Case fulfills the TerminationCondition of the
	 * CaseModel.
	 * 
	 * @return boolean
	 */
	public boolean canTerminate() {
		/*
		List<DataStateCondition> existingConditions = new ArrayList<>();
		for (DataObject dataObjectInstance : dataObjectInstances)
			existingConditions.add(dataObjectInstance.getDataNode().getDataObjectState());
		return caseModel.getTerminationCondition().isFulfilled(existingConditions);
		*/
		return false;
	}

	/**
	 * Get a specific Instance of an DataObject.
	 * 
	 * @param instanceId
	 * @return DataObjectInstance
	 */
	public DataObject getDataObject(String dataObjectId) {
		if (dataObjects.containsKey(dataObjectId)) {
			return dataObjects.get(dataObjectId);
		}
		return null;
	}

	// GETTER & SETTER
	public DataModel getDataModel() {
		return dataModel;
	}

	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}

	public CaseExecutioner getCaseExecutioner() {
		return caseExecutioner;
	}

	public void setCaseExecutioner(CaseExecutioner caseExecutioner) {
		this.caseExecutioner = caseExecutioner;
	}

	public Map<String, DataObject> getDataObjectsMap() {
		return dataObjects;
	}

	/**
	 * Get all DataObjects.
	 * 
	 * @return List of DataObjectInstance
	 */
	public List<DataObject> getDataObjects() {
		Collection<DataObject> coll = dataObjects.values();
		return new ArrayList<>(coll);
	}
}
