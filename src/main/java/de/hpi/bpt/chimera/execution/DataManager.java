package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hpi.bpt.chimera.model.datamodel.DataModel;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractDataControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.DataNode;

public class DataManager {
	private Case caze;
	// TerminationCondition t = caze.getCaseModel().getTerminationConditions();
	private DataModel dataModel;
	/**
	 * Map of Id of DataObjectInstance to DataObjectInstance
	 */
	private Map<String, DataObjectInstance> dataObjectInstances;

	public DataManager(Case caze, DataModel dataModel) {
		this.caze = caze;
		this.dataModel = dataModel;
		this.dataObjectInstances = new HashMap<>();
	}

	/**
	 * Create new DataObjectInstances with the outgoing DataNodes of an specific
	 * ControlNode.
	 * 
	 * @param controlNode
	 */
	public void createDataObjectInstances(AbstractDataControlNode controlNode) {
		for (DataNode dataNode : controlNode.getOutgoingDataNodes()) {
			// TODO: think about whether implement this thing that DataClasses
			// that are incomingDataNodes shell not be created as Instances or
			// get rid of this while parsing
			DataObjectInstance dataObjectInstance = new DataObjectInstance(dataNode);
			dataObjectInstances.put(dataObjectInstance.getId(), dataObjectInstance);
		}
	}

	/**
	 * Lock the Instances of DataObjects. And returns these Instances.
	 * 
	 * @param List
	 *            of dataObjectInstanceIds
	 * @return Map of DataObjectInstances mapped by their ids
	 */
	public Map<String, DataObjectInstance> lockDataObjectInstances(List<String> dataObjectInstanceIds) {
		Map<String, DataObjectInstance> lockedDataObjectInstances = new HashMap<>();
		for (String id : dataObjectInstanceIds) {
			if (dataObjectInstances.containsKey(id)) {
				DataObjectInstance dataObjectInstance = dataObjectInstances.get(id);
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
	public void unlockDataObjectInstances(Map<String, DataObjectInstance> toUnlockDataObjectInstances) {
		// toUnlockDataObjectInstances.values().forEach(DataObjectInstance::unlock);
		for (Map.Entry<String, DataObjectInstance> entry : toUnlockDataObjectInstances.entrySet()) {
			if (dataObjectInstances.containsKey(entry.getKey())) {
				DataObjectInstance dataObjectInstance = dataObjectInstances.get(entry.getKey());
				dataObjectInstance.unlock();
			}
		}
	}

	/**
	 * Handle the transition of the Object-LifecycleStates of the Instances of
	 * DataObject.
	 * 
	 * @param dataObjectTransitions
	 */
	public void transitionDataObjectInstances(Map<String, String> dataObjectTransitions) {
		if (dataObjectTransitions.isEmpty()) {
			return;
		}
		// TODO: do the transition
	}

	/**
	 * Get DataObjectInstances that are instantiated by the DataNodes.
	 * 
	 * @param dataNodesToCheck
	 * @return List of DataObjectInstances
	 */
	public List<DataObjectInstance> getExistingDataObjectInstances(List<DataNode> dataNodesToCheck) {
		// create a Map of DataNode to DataObjectInstance
		Map<DataNode, DataObjectInstance> dataNodeToInstanceAssociation = new HashMap<>();
		for (DataObjectInstance instance : dataObjectInstances.values())
			dataNodeToInstanceAssociation.put(instance.getDataNode(), instance);
		// resolve Map
		List<DataObjectInstance> existingInstances = new ArrayList<>();
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
		for (DataObjectInstance dataObjectInstance : dataObjectInstances.values()) {
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

	// GETTER & SETTER
	public Case getCaze() {
		return caze;
	}

	public void setCaze(Case caze) {
		this.caze = caze;
	}

	public DataModel getDataModel() {
		return dataModel;
	}

	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}

	public Map<String, DataObjectInstance> getDataObjectInstances() {
		return dataObjectInstances;
	}
}
