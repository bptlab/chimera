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
	private Map<String, DataObject> dataObjectIdToDataObject;

	public DataManager(DataModel dataModel, CaseExecutioner caseExecutioner) {
		this.setDataModel(dataModel);
		this.setCaseExecutioner(caseExecutioner);
		this.dataObjectIdToDataObject = new HashMap<>();
	}

	/**
	 * Create new DataObjects.
	 * 
	 * @param outgoingDataNodes
	 * @param dataManagerBean
	 */
	@Deprecated
	public void createDataObject(List<DataNode> outgoingDataNodes, DataManagerBean dataManagerBean) {
		for (String newCreationId : dataManagerBean.getNewCreations()) {
			for (DataNode dataNode : outgoingDataNodes) {
				if (dataNode.getId().equals(newCreationId)) {
					Map<String, Object> attributeValues = dataManagerBean.getDataNodeAttributeValuesById(newCreationId);
					DataObject dataObjectInstance = new DataObject(dataNode, caseExecutioner, attributeValues);
					this.dataObjectIdToDataObject.put(dataObjectInstance.getId(), dataObjectInstance);
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
			if (!dataObjectIdToDataObject.containsKey(dataObjectId))
				continue;
			DataObject objectInstance = dataObjectIdToDataObject.get(dataObjectId);
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
	 * Lock those {@link DataObject}{@code s} that are referred by the
	 * dataObjectIds and returns these Instances.
	 * 
	 * @param dataObjectIds
	 *            - list of {@code ids} which referring DataObjects shall be
	 *            locked
	 * @return List of locked DataObjects
	 */
	public List<DataObject> lockDataObjects(List<String> dataObjectIds) {
		List<DataObject> lockedDataObjects = new ArrayList<>();
		for (String id : dataObjectIds) {
			if (dataObjectIdToDataObject.containsKey(id)) {
				DataObject dataObject = dataObjectIdToDataObject.get(id);
				dataObject.lock();
				lockedDataObjects.add(dataObject);
			}
		}
		return lockedDataObjects;
	}

	/**
	 * Unlock certain {@link DataObject}{@code s}.
	 * 
	 * @param dataObjects
	 *            - that shall be unlocked
	 */
	public void unlockDataObjects(List<DataObject> dataObjects) {
		// toUnlockDataObjects.values().forEach(DataObjectInstance::unlock);
		for (DataObject dataObjectToUnlock : dataObjects) {
			if (dataObjectIdToDataObject.containsKey(dataObjectToUnlock.getId())) {
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
	 * Get the {@link DataStateCondition}s of all unlocked {@link DataObject}s.
	 * 
	 * @return List of DataStateConditions which referring DataObject is
	 *         unlocked
	 */
	public List<DataStateCondition> getDataStateConditions() {
		List<DataStateCondition> existingConditions = new ArrayList<>();

		for (DataObject dataObject : dataObjectIdToDataObject.values()) {
			if (!dataObject.isLocked()) {
				existingConditions.add(dataObject.getCondition());
			}
		}
		return existingConditions;
	}

	/**
	 * Get all DataObjects that have the same DataStateCondition (equal
	 * DataClass and ObjectLifecycleState) as the conditions in the
	 * conditionSets.
	 * 
	 * @param conditionSets
	 *            - list of ConditionSets which DataStateCondition shall be
	 *            checked
	 * @return Set of DataObjects that have the same DataStateCondition as one
	 *         of DataStateConditions in the conditionSets
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

		for (DataObject dataObject : dataObjectIdToDataObject.values()) {
			if (condition.equals(dataObject.getCondition())) {
				availableDataObjects.add(dataObject);
			}
		}

		return availableDataObjects;
	}

	/**
	 * Set value of DataObjects.
	 * 
	 * @param dataAttributeValues
	 */
	public void setDataAttributeValues(Map<String, Map<String, Object>> dataAttributeValues) {
		for (Map.Entry<String, Map<String, Object>> transition : dataAttributeValues.entrySet()) {
			String dataObjectId = transition.getKey();
			if (!dataObjectIdToDataObject.containsKey(dataObjectId))
				continue;
			DataObject dataObject = dataObjectIdToDataObject.get(dataObjectId);
			Map<String, Object> attributeValues = transition.getValue();
			dataObject.setDataAttributeValues(attributeValues);
		}
	}

	/**
	 * Get a specific DataObject by id.
	 * 
	 * @param dataObjectId
	 * @return DataObject
	 */
	public DataObject getDataObjectById(String dataObjectId) {
		if (dataObjectIdToDataObject.containsKey(dataObjectId)) {
			return dataObjectIdToDataObject.get(dataObjectId);
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

	public Map<String, DataObject> getDataObjectMap() {
		return dataObjectIdToDataObject;
	}
}
