package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.jhistory.transportationbeans.ActivityLog;
import de.hpi.bpt.chimera.jhistory.transportationbeans.DataAttributeLog;
import de.hpi.bpt.chimera.jhistory.transportationbeans.DataObjectLog;
import de.hpi.bpt.chimera.jhistory.transportationbeans.LogEntry;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractDataControlNode;

public class CaseExecutioner {
	// private static final Logger log = Logger.getLogger(CaseExecutioner.class);

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
		this.dataManager = new DataManager(caseModel.getDataModel(), this);
	}

	/**
	 * Start the Case.
	 */
	public void startCase() {
		for (FragmentInstance fragmentInstance : caze.getFragmentInstances().values()) {
			fragmentInstance.start();
		}
	}

	/**
	 * Get all ActivityInstances in all Fragment Instances that are in a
	 * specific State.
	 * 
	 * @param state
	 * @return Collection of ActivityInstances
	 */
	public Collection<AbstractActivityInstance> getActivitiesWithState(State state) {
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
			AbstractActivityInstance activtyInstance = (AbstractActivityInstance) nodeInstance;
			List<DataObject> lockedDataObjects = dataManager.lockDataObjects(selectedDataObjectInstanceIds);
			activtyInstance.setSelectedDataObjects(lockedDataObjects);
			// TODO: has this to be before lock?
			activtyInstance.getFragmentInstance().skipAlternativeControlNodes(nodeInstance);
			activtyInstance.begin();
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
			AbstractActivityInstance activtyInstance = (AbstractActivityInstance) nodeInstance;
			List<DataObject> toUnlockDataObjects = activtyInstance.getSelectedDataObjectInstances();
			dataManager.unlockDataObjects(toUnlockDataObjects);
			AbstractDataControlNode controlNode = (AbstractDataControlNode) nodeInstance.getControlNode();
			// dataManager.transitionDataObject(controlNode.getOutgoingDataNodes(),
			// dataManagerBean);
			// dataManager.createDataObject(controlNode.getOutgoingDataNodes(),
			// dataManagerBean);
			activtyInstance.terminate();
		}
	}

	/**
	 * Get a specific ActivityInstance by an id.
	 * 
	 * @param activityInstanceId
	 * @return the associated AbstractActivityInstance, or {@code null} if the
	 *         id is not assigned, or {@code null} if the id is associated to a
	 *         ControlNodeInstance which is not an ActivityInstance
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
	 * Get an ControlNodeInstance over all FragmentInstances of the Case by an
	 * id.
	 * 
	 * @param controlNodeId
	 * @return the associated ControlNodeInstance, or {@code null} if the id is
	 *         not assigned
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
	 * Log the transition of the {@link State} of an
	 * {@link AbstractActivityInstance}. Therefore only log certain
	 * {@link State} transitions.
	 * 
	 * @param activityInstance
	 *            - that shall be logged
	 * @param newState
	 *            - of the ActivityInstance
	 */
	public void logActivityTransition(AbstractActivityInstance activityInstance, State newState) {
		List<State> stateable = new ArrayList<>( Arrays.asList(State.INIT, State.RUNNING, State.TERMINATED) );
		if (stateable.contains(newState)) {
			ActivityLog activityLog = new ActivityLog(activityInstance, activityInstance.getState(), newState);
			activityLogs.add(activityLog);
			// TODO: think about necessary
			sortLogs(activityLogs);
		}
	}

	/**
	 * Log the transition of an {@link ObjectLifecycleState} of an
	 * {@link DataObject}.
	 * 
	 * @param dataObjectInstance
	 *            - that shall be logged
	 * @param newObjectLifecycleState
	 *            - of the DataObject
	 */
	public void logDataObjectTransition(DataObject dataObjectInstance, ObjectLifecycleState newObjectLifecycleState) {
		DataObjectLog dataObjectLog = new DataObjectLog(dataObjectInstance, dataObjectInstance.getObjectLifecycleState(), newObjectLifecycleState);
		dataObjectLogs.add(dataObjectLog);
		// TODO: think about necessary
		sortLogs(dataObjectLogs);
	}

	public void logDataObjectTransition(DataObject dataObjectInstance, ObjectLifecycleState oldObjectLifecycleState, ObjectLifecycleState newObjectLifecycleState) {
		DataObjectLog dataObjectLog = new DataObjectLog(dataObjectInstance, oldObjectLifecycleState, newObjectLifecycleState);
		dataObjectLogs.add(dataObjectLog);
		// TODO: think about necessary
		sortLogs(dataObjectLogs);
	}

	/**
	 * Log the transition of the value of an {@link DataAttributeInstance}.
	 * 
	 * @param dataAttributeInstance
	 *            - that shall be logged
	 * @param newValue
	 *            - of the DataAttributeInstance
	 */
	public void logDataAttributeTransition(DataAttributeInstance dataAttributeInstance, Object newValue) {
		DataAttributeLog dataAttributeLog = new DataAttributeLog(dataAttributeInstance, dataAttributeInstance.getValue(), newValue);
		dataAttributeLogs.add(dataAttributeLog);
		// TODO: think about necessary
		sortLogs(dataAttributeLogs);
	}

	/**
	 * Sort the LogEntries descending by time which means the newest LogEntry
	 * comes first.
	 * 
	 * @param logEntries
	 *            - that shall be sorted
	 */
	private void sortLogs(List<? extends LogEntry> logEntries) {
		logEntries.sort((l1, l2) -> l2.getTimeStamp().compareTo(l1.getTimeStamp()));
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
