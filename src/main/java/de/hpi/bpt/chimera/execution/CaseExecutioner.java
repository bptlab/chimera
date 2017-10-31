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
	 * Begin an {@link AbstractActivityInstance}. Therefore lock all
	 * {@link DataObject}s that are used by the AbstractActivityInstance and set
	 * them in the AbstractActivityInstance. If {@code activityInstanceId} is
	 * not assigned to an AbstractActivityInstance or if one of the selected
	 * DataObjects is already locked the AbstractActivityInstance cannot be
	 * begun.
	 * 
	 * @param activityInstanceId
	 *            - {@code id} of AbstractActivityInstance that shall be begun
	 * @param selectedDataObjectIds
	 *            - list of {@code ids } of DataObjects that are used by the
	 *            AbstractActivityInstance
	 */
	public void beginActivityInstance(String activityInstanceId, List<String> selectedDataObjectIds) {
		try {
			AbstractActivityInstance activityInstance = getActivityInstanceWithStateAndExceptions(activityInstanceId, State.READY);
			List<DataObject> lockedDataObjects = dataManager.lockDataObjects(selectedDataObjectIds);
			activityInstance.setSelectedDataObjects(lockedDataObjects);
			// TODO: has this to be before lock?
			activityInstance.getFragmentInstance().skipAlternativeControlNodes(activityInstance);
			activityInstance.begin();
		} catch (IllegalArgumentException | SecurityException e) {
			throw e;
		}
	}

	/**
	 * 
	 * @param activityInstanceId
	 * @param dataObjectTransitions
	 */
	public void terminateActivityInstance(String activityInstanceId, DataManagerBean dataManagerBean) {
		try {
			AbstractActivityInstance activityInstance = this.getActivityInstanceWithStateAndExceptions(activityInstanceId, State.RUNNING);
			List<DataObject> dataObjectsToUnlock = activityInstance.getSelectedDataObjectInstances();
			dataManager.unlockDataObjects(dataObjectsToUnlock);
			// dataManager.transitionDataObject(controlNode.getOutgoingDataNodes(),
			// dataManagerBean);
			// dataManager.createDataObject(controlNode.getOutgoingDataNodes(),
			// dataManagerBean);
			activityInstance.terminate();
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	// TODO: think about whether this should be refactored to a method with
	// exceptions
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

	/**
	 * Get a specific ActivityInstance by an id.
	 * 
	 * @param activityInstanceId
	 * @return the associated AbstractActivityInstance, or {@code null} if the
	 *         id is not assigned, or {@code null} if the id is associated to a
	 *         ControlNodeInstance which is not an ActivityInstance
	 */
	public AbstractActivityInstance getActivityInstanceWithExceptions(String activityInstanceId) {
		ControlNodeInstance nodeInstance = getControlNodeInstance(activityInstanceId);
		if (nodeInstance == null) {
			String message = String.format("ActivityInstance-id: %s is not assigned", activityInstanceId);
			log.error(message);
			throw new IllegalArgumentException(message);
		}
		if (!(nodeInstance instanceof AbstractActivityInstance)) {
			String message = String.format("ControlNode assigned by id: %s, is not of an ActivityInstance", activityInstanceId);
			log.error(message);
			throw new IllegalArgumentException(message);
		}

		return (AbstractActivityInstance) nodeInstance;
	}

	/**
	 * Get a specific ActivityInstance by an id.
	 * 
	 * @param activityInstanceId
	 * @return the associated AbstractActivityInstance, or {@code null} if the
	 *         id is not assigned, or {@code null} if the id is associated to a
	 *         ControlNodeInstance which is not an ActivityInstance
	 */
	public AbstractActivityInstance getActivityInstanceWithStateAndExceptions(String activityInstanceId, State state) {
		try {
			AbstractActivityInstance activitiyInstance = this.getActivityInstanceWithExceptions(activityInstanceId);
			if (activitiyInstance.getState() != state) {
				String message = String.format("ActivityInstance assigned by id: %s, is not in State: %s", activityInstanceId, state.toString());
				log.error(message);
				throw new IllegalArgumentException(message);
			}
			return activitiyInstance;
		} catch (IllegalArgumentException e) {
			throw e;
		}
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

	/**
	 * 
	 * @return all AbstractActivityInstances in all FragmentInstances.
	 */
	public Collection<AbstractActivityInstance> getActivityInstances() {
		Collection<AbstractActivityInstance> activityInstances = new ArrayList<>();
		for (FragmentInstance fragmentInstance : caze.getFragmentInstances().values()) {
			for (ControlNodeInstance nodeInstance : fragmentInstance.getControlNodeInstances().values()) {
				if (nodeInstance instanceof AbstractActivityInstance) {
					activityInstances.add((AbstractActivityInstance) nodeInstance);
				}
			}
		}
		return activityInstances;
	}
}
