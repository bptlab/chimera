package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public void createDataObject(AbstractDataControlNode controlNode) {
		for (DataNode dataNode : controlNode.getOutgoingDataNodes()) {
			DataObject dataObjectInstance = new DataObject(dataNode, caseExecutioner);
			dataObjects.put(dataObjectInstance.getId(), dataObjectInstance);
		}
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
	 * @return Map of DataObjectInstances mapped by their ids
	 */
	public Map<String, DataObject> lockDataObjects(List<String> dataObjectInstanceIds) {
		Map<String, DataObject> lockedDataObjectInstances = new HashMap<>();
		for (String id : dataObjectInstanceIds) {
			if (dataObjects.containsKey(id)) {
				DataObject dataObjectInstance = dataObjects.get(id);
				dataObjectInstance.lock();
				lockedDataObjectInstances.put(dataObjectInstance.getId(), dataObjectInstance);
			}
		}
		return lockedDataObjectInstances;
	}

	/**
	 * Unlock the Instances of DataObjects.
	 * 
	 * @param toUnlockDataObjectInstances
	 */
	public void unlockDataObjects(Map<String, DataObject> toUnlockDataObjectInstances) {
		// toUnlockDataObjectInstances.values().forEach(DataObjectInstance::unlock);
		for (Map.Entry<String, DataObject> entry : toUnlockDataObjectInstances.entrySet()) {
			if (dataObjects.containsKey(entry.getKey())) {
				DataObject dataObjectInstance = dataObjects.get(entry.getKey());
				dataObjectInstance.unlock();
			}
		}
	}

	/**
	 * Get DataObjectInstances that are instantiated by the DataNodes.
	 * 
	 * TODO get all DOs that fulfill the DataObjectStateCondition of the DataNodes
	 * @param dataNodesToCheck
	 * @return List of DataObjectInstances
	 */
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

	/**
	 * Check whether the DataNodes that shell be checked are instantiated by the
	 * existing DataObjectInstances.
	 * 
	 * @param dataNodesToCheck
	 * @return boolean
	 */
	public boolean isExistingDataObject(List<DataNode> dataNodesToCheck) {
		List<DataNode> existingDataNodes = new ArrayList<>();
		// make sure that even for DataNodes that occur twice exists two
		// Instances.
		for (DataObject dataObjectInstance : dataObjects.values()) {
			existingDataNodes.add(dataObjectInstance.getDataNode());
		}
		for (DataNode dataNodeToCheck : dataNodesToCheck) {
			if (existingDataNodes.contains(dataNodeToCheck)) {
				existingDataNodes.remove(dataNodeToCheck);
			} else {
				return false;
			}
		}
		return true;
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

	public Map<String, DataObject> getDataObjectInstances() {
		return dataObjects;
	}
}
