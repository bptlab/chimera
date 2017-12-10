package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.exception.IllegalActivityInstanceStateException;
import de.hpi.bpt.chimera.execution.exception.IllegalControlNodeInstanceIdException;
import de.hpi.bpt.chimera.execution.exception.IllegalControlNodeInstanceTypeException;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.jhistory.transportationbeans.ActivityLog;
import de.hpi.bpt.chimera.jhistory.transportationbeans.DataAttributeLog;
import de.hpi.bpt.chimera.jhistory.transportationbeans.DataObjectLog;
import de.hpi.bpt.chimera.jhistory.transportationbeans.LogEntry;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

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
	 * Start the Case by starting all {@link FragmentInstance}s.
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
	public List<AbstractActivityInstance> getActivitiesWithState(State state) {
		List<AbstractActivityInstance> activityInstances = new ArrayList<>();
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
	 * @param activityInstance
	 *            AbstractActivityInstance that shall be begun
	 * @param selectedDataObjectIds
	 *            - list of {@code ids } of DataObjects that are used by the
	 *            AbstractActivityInstance
	 */
	public void beginActivityInstance(AbstractActivityInstance activityInstance, List<DataObject> selectedDataObjects) {
		try {
			if (!activityInstance.getState().equals(State.READY)) {
				IllegalActivityInstanceStateException e = new IllegalActivityInstanceStateException(activityInstance, State.READY);
				log.error(e.getMessage());
				throw e;
			}

			List<DataObject> lockedDataObjects = dataManager.lockDataObjects(selectedDataObjects);
			activityInstance.setSelectedDataObjects(lockedDataObjects);
			// TODO: has this to be before lock?
			activityInstance.getFragmentInstance().skipAlternativeControlNodes(activityInstance);
			activityInstance.begin();
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	/**
	 * Does everything for terminating an activity excepted from the actual
	 * termination. This will be done after all DataObjects/DataAttributes are
	 * set via an Rest-Method which will be called by the Front-End.
	 * 
	 * @param activityInstanceId
	 * @param dataClassToStateTransition
	 * @return List of DataObjects that made a transition and those that are
	 *         newly created
	 */
	public List<DataObject> prepareForActivityInstanceTermination(AbstractActivityInstance activityInstance, Map<DataClass, ObjectLifecycleState> dataClassToStateTransitions) {
		try {
			if (!activityInstance.getState().equals(State.RUNNING)) {
				IllegalActivityInstanceStateException e = new IllegalActivityInstanceStateException(activityInstance, State.RUNNING);
				log.error(e.getMessage());
				throw e;
			}

			List<DataObject> workingItems = activityInstance.getSelectedDataObjects();
			dataManager.unlockDataObjects(workingItems);
			
			List<DataObject> usedDataObject = new ArrayList<>();
			if (!activityInstance.getControlNode().getPostCondition().getAtomicDataStateConditions().isEmpty()) {
				usedDataObject = dataManager.handleDataObjectTransitions(workingItems, dataClassToStateTransitions);
			}

			activityInstance.setOutputDataObjects(usedDataObject);
			return usedDataObject;
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	/**
	 * Finally terminates the activity. Therefore this Method is called after
	 * termination was prepared (prepareForActivityInstanceTermination() ) and
	 * all DataAttributes were set.
	 * 
	 * @param activityInstanceId
	 */
	public void terminateActivityInstance(AbstractActivityInstance activityInstance) {
		try {
			activityInstance.terminate();
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	// Do not implement this at the moment
	public void startAutomaticTasks() {

	}

	/**
<<<<<<< HEAD
	 * Get an ControlNodeInstance over all FragmentInstances of the Case by an
	 * id.
=======
	 * Get a ControlNodeInstance over all FragmentInstances of the Case.
>>>>>>> 5a0086cf4a2d8618df46db0aeb43e89c001fc55d
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

		IllegalControlNodeInstanceIdException e = new IllegalControlNodeInstanceIdException(controlNodeId);
		log.error(e.getMessage());
		throw e;
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
		try {
			ControlNodeInstance nodeInstance = getControlNodeInstance(activityInstanceId);

			if (!(nodeInstance instanceof AbstractActivityInstance)) {
				IllegalControlNodeInstanceTypeException e = new IllegalControlNodeInstanceTypeException(nodeInstance, AbstractActivityInstance.class);
				log.error(e.getMessage());
				throw e;
			}

			return (AbstractActivityInstance) nodeInstance;
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	// LOGGING
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
