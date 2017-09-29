package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.jhistory.transportationbeans.ActivityLog;
import de.hpi.bpt.chimera.jhistory.transportationbeans.DataAttributeLog;
import de.hpi.bpt.chimera.jhistory.transportationbeans.DataObjectLog;
import de.hpi.bpt.chimera.jhistory.transportationbeans.LogEntry;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.condition.DataObjectStateCondition;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractDataControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.DataNode;

public class CaseExecutioner {
	private static final Logger log = Logger.getLogger(CaseExecutioner.class);

	private Case caze;
	private CaseModel caseModel;
	private DataManager dataManager;
	private boolean terminated;
	private List<ActivityLog> activityLogs;
	private List<DataObjectLog> dataObjectLogs;
	private List<DataAttributeLog> dataAttributeLogs;

	public CaseExecutioner(CaseModel caseModel, String caseName) {
		this.activityLogs = new ArrayList<>();
		this.dataObjectLogs = new ArrayList<>();
		this.dataAttributeLogs = new ArrayList<>();
		this.terminated = false;
		this.caseModel = caseModel;
		this.caze = new Case(caseName, caseModel, this);
		this.dataManager = new DataManager(caze, caseModel.getDataModel());
	}

	/**
	 * Start the Case.
	 */
	public void startCase() {
		for (FragmentInstance fragmentInstance : caze.getFragmentInstances().values()) {
			fragmentInstance.start();
		}
	}
	// TODO: think about whether this should be put up here or in
	// FragmentInstance
	public void createDataObjectInstances(AbstractControlNode node) {
		if (node instanceof AbstractDataControlNode) {
			dataManager.createDataObjectInstances((AbstractDataControlNode) node);
		}
	}

	/**
	 * Get all ActivityInstances that are in a specific State.
	 * 
	 * @param state
	 * @return Collection of ActivityInstances
	 */
	public Collection<AbstractActivityInstance> getAllActivitiesWithState(State state) {
		Collection<AbstractActivityInstance> activityInstances = new ArrayList<>();
		for (FragmentInstance fragmentInstance : caze.getFragmentInstances().values()) {
			for (ControlNodeInstance nodeInstance : fragmentInstance.getControlNodeInstances().values()) {
				if (nodeInstance instanceof AbstractActivityInstance && nodeInstance.getState() == state) {
					activityInstances.add((AbstractActivityInstance) nodeInstance);
				}
			}
		}
		return activityInstances;
	}

	/**
	 * Begin an ActivityInstance. Therefore lock all DataObjectInstances that
	 * are used by the ActivityInstance and set them in the ActivityInstance.
	 * 
	 * @param activityInstanceId
	 * @param selectedDataObjectInstanceIds
	 */
	public void beginActivityInstance(String activityInstanceId, List<String> selectedDataObjectInstanceIds) {
		ControlNodeInstance nodeInstance = getControlNodeInstance(activityInstanceId);
		if (nodeInstance == null)
			return;

		if (nodeInstance instanceof AbstractActivityInstance && nodeInstance.getState() == State.READY) {
			Map<String, DataObjectInstance> lockedDataObjectInstances = dataManager.lockDataObjectInstances(selectedDataObjectInstanceIds);
			((AbstractActivityInstance) nodeInstance).setSelectedDataObjectInstances(lockedDataObjectInstances);
			nodeInstance.begin();
			return;
		}
	}

	/**
	 * 
	 * @param activityInstanceId
	 * @param dataObjectTransitions
	 */
	public void terminateActivityInstance(String activityInstanceId, DataManagerBean dataManagerBean) {
		ControlNodeInstance nodeInstance = getControlNodeInstance(activityInstanceId);
		if (nodeInstance == null)
			return;

		// TODO: think about instance has to be running
		if (nodeInstance instanceof AbstractActivityInstance && nodeInstance.getState() == State.RUNNING) {
			Map<String, DataObjectInstance> toUnlockDataObjectInstances = ((AbstractActivityInstance) nodeInstance).getSelectedDataObjectInstances();
			dataManager.unlockDataObjectInstances(toUnlockDataObjectInstances);
			AbstractDataControlNode controlNode = (AbstractDataControlNode) nodeInstance.getControlNode();
			dataManager.transitionDataObjectInstances(controlNode.getOutgoingDataNodes(), dataManagerBean);
			dataManager.createDataObjectInstances(controlNode.getOutgoingDataNodes(), dataManagerBean);
			nodeInstance.terminate();
		}
	}

	/**
	 * Get a specific ActivityInstance.
	 * 
	 * @param activityInstanceId
	 */
	public AbstractActivityInstance getActivityInstance(String activityInstanceId) {
		ControlNodeInstance nodeInstance = getControlNodeInstance(activityInstanceId);
		if (nodeInstance == null)
			return null;
		if (!(nodeInstance instanceof AbstractActivityInstance))
			return null;
		return (AbstractActivityInstance) nodeInstance;
	}

	// Do not implement this at the moment
	public void startAutomaticTasks() {
		// TODO Auto-generated method stub

	}

	/**
	 * Get an ControlNodeInstance over all FragmentInstances of the Case.
	 * 
	 * @param activityInstanceId
	 * @return
	 */
	private ControlNodeInstance getControlNodeInstance(String controlNodeId) {
		for (FragmentInstance fragmentInstance : caze.getFragmentInstances().values()) {
			if (fragmentInstance.getControlNodeInstances().containsKey(controlNodeId)) {
				return fragmentInstance.getControlNodeInstances().get(controlNodeId);
			}
		}
		return null;
	}

	/**
	 * Get all Instances of DataObjects.
	 * 
	 * @return List of DataObjectInstance
	 */
	public List<DataObjectInstance> getDataObjectInstances() {
		Collection<DataObjectInstance> instances = dataManager.getDataObjectInstances().values();
		return new ArrayList<>(instances);
	}

	/**
	 * Get a specific Instance of an DataObject.
	 * 
	 * @param instanceId
	 * @return DataObjectInstance
	 */
	public DataObjectInstance getDataObjectInstance(String instanceId) {
		if (dataManager.getDataObjectInstances().containsKey(instanceId)) {
			return dataManager.getDataObjectInstances().get(instanceId);
		}
		return null;
	}

	/**
	 * Get the available DataInput for an ActivityInstance.
	 * 
	 * @param activityInstanceId
	 * @return List of DataObjectInstance
	 */
	public List<DataObjectInstance> getAvailableInputForAcitivityInstance(String activityInstanceId) {
		AbstractActivityInstance activityInstance = getActivityInstance(activityInstanceId);
		List<DataNode> dataNodesToCheck = activityInstance.getControlNode().getIncomingDataNodes();
		return dataManager.getExistingDataObjectInstances(dataNodesToCheck);
	}

	/**
	 * Check whether a ControlNode is DataFlow enabled.
	 * 
	 * @param controlNode
	 * @return boolean
	 */
	public boolean isDataFlowEnabled(AbstractDataControlNode controlNode) {
		List<DataNode> incomingDataNodes = controlNode.getIncomingDataNodes();
		if (incomingDataNodes.isEmpty())
			return true;
		return dataManager.isExistingDataObject(incomingDataNodes);
	}

	/**
	 * Returns whether the Case fulfills the TerminationCondition of the
	 * CaseModel.
	 * 
	 * @return boolean
	 */
	public boolean canTerminate() {
		List<DataObjectInstance> dataObjectInstances = this.getDataObjectInstances();
		List<DataObjectStateCondition> existingConditions = new ArrayList<>();
		for (DataObjectInstance dataObjectInstance : dataObjectInstances)
			existingConditions.add(dataObjectInstance.getDataNode().getDataObjectState());
		return caseModel.getTerminationCondition().isFulfilled(existingConditions);
	}

	/**
	 * Log the transition of the State of an ActivityInstance.
	 * 
	 * @param activityInstance
	 * @param newState
	 */
	public void logActivityTransition(AbstractActivityInstance instance, State newState) {
		List<State> stateable = new ArrayList<>( Arrays.asList(State.INIT, State.RUNNING, State.TERMINATED) );
		if (stateable.contains(newState)) {
			ActivityLog activityLog = new ActivityLog(instance, instance.getState(), newState);
			activityLogs.add(activityLog);
			// TODO: think about necessary
			sortLogs(activityLogs);
		}
	}

	/**
	 * Log the transition of an ObjectLifecycleState of an DataObject.
	 * 
	 * @param dataObjectInstance
	 * @param newState
	 */
	public void logDataObjectTransition(DataObjectInstance instance, ObjectLifecycleState newState) {
		DataObjectLog dataObjectLog = new DataObjectLog(instance, instance.getObjectLifecycleState(), newState);
		dataObjectLogs.add(dataObjectLog);
		// TODO: think about necessary
		sortLogs(dataObjectLogs);
	}

	public void logDataObjectTransition(DataObjectInstance dataObjectInstance, ObjectLifecycleState oldState, ObjectLifecycleState newState) {
		DataObjectLog dataObjectLog = new DataObjectLog(dataObjectInstance, oldState, newState);
		dataObjectLogs.add(dataObjectLog);
		// TODO: think about necessary
		sortLogs(dataObjectLogs);
	}

	/**
	 * Log the transition of the value of an DataAttributeInstance.
	 * 
	 * @param instance
	 * @param newValue
	 */
	public void logDataAttributeTransition(DataAttributeInstance instance, Object newValue) {
		DataAttributeLog dataAttributeLog = new DataAttributeLog(instance, instance.getValue(), newValue);
		dataAttributeLogs.add(dataAttributeLog);
		// TODO: think about necessary
		sortLogs(dataAttributeLogs);
	}

	/**
	 * Sort the LogEntries descending by time which means the newest LogEntry
	 * comes first.
	 * 
	 * @param logEntries
	 */
	private void sortLogs(List<? extends LogEntry> logEntries) {
		logEntries.sort((l1, l2) -> l2.getTimeStamp().compareTo(l1.getTimeStamp()));
	}


	public void createDataObjectInstance(String activityInstanceId, String dataNodeId, Map<String, Object> attributeValues) {
		AbstractActivityInstance instance = this.getActivityInstance(activityInstanceId);
		for (DataNode dataNode : instance.getControlNode().getOutgoingDataNodes()) {
			if (dataNode.getId().equals(dataNodeId)) {
				dataManager.createDataObjectInstance(dataNode, attributeValues);
			}
		}
	}

	public void setDataAttributeValues(Map<String, Map<String, Object>> dataAttributeValues) {
		dataManager.setDataAttributeValues(dataAttributeValues);
	}

	// GETTER & SETTER
	public Case getCase() {
		return caze;
	}

	public void setCase(Case caze) {
		this.caze = caze;
	}

	public CaseModel getCaseModel() {
		return caseModel;
	}

	public void setCaseModel(CaseModel caseModel) {
		this.caseModel = caseModel;
	}

	public DataManager getDataManager() {
		return dataManager;
	}

	public void setDataManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	public boolean isTerminated() {
		return terminated;
	}

	public void setTerminated(boolean terminated) {
		this.terminated = terminated;
	}

	public List<ActivityLog> getActivityLogs() {
		return activityLogs;
	}

	public List<DataObjectLog> getDataObjectLogs() {
		return dataObjectLogs;
	}

	public List<DataAttributeLog> getDataAttributeLogs() {
		return dataAttributeLogs;
	}
}
