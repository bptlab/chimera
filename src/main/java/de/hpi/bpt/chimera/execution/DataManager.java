package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.DataModel;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
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
	public void createDataObjectInstances(AbstractControlNode controlNode) {
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
}
