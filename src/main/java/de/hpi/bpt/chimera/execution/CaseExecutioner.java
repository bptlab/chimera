package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.controlnodes.AbstractDataControlNodeInstance;
import de.hpi.bpt.chimera.execution.controlnodes.ControlNodeInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.data.DataAttributeInstance;
import de.hpi.bpt.chimera.execution.data.DataManager;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.execution.exception.IllegalActivityInstanceStateException;
import de.hpi.bpt.chimera.execution.exception.IllegalControlNodeInstanceIdException;
import de.hpi.bpt.chimera.execution.exception.IllegalControlNodeInstanceTypeException;
import de.hpi.bpt.chimera.history.transportationbeans.ActivityLog;
import de.hpi.bpt.chimera.history.transportationbeans.DataAttributeLog;
import de.hpi.bpt.chimera.history.transportationbeans.DataObjectLog;
import de.hpi.bpt.chimera.history.transportationbeans.LogEntry;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
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
			for (ControlNodeInstance nodeInstance : fragmentInstance.getControlNodeInstanceIdToInstance().values()) {
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
	 * them in the AbstractActivityInstance. If the {@code activityInstance} is
	 * not READY or one of the selected DataObjects is already locked the
	 * AbstractActivityInstance cannot be begun.
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

			beginDataControlNodeInstance((AbstractDataControlNodeInstance) activityInstance, selectedDataObjects);
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	/**
	 * Begin an {@link AbstractEventInstance}. Therefore lock all
	 * {@link DataObject}s that are used by the AbstractEventInstance and set
	 * them in the AbstractEventInstance. If one of the selected DataObjects is
	 * already locked the AbstractEventInstance cannot be begun.
	 * 
	 * @param eventInstance
	 * @param selectedDataObjects
	 */
	public void beginEventInstance(AbstractEventInstance eventInstance, List<DataObject> selectedDataObjects) {
		try {
			if (!eventInstance.getState().equals(State.REGISTERED)) {
				// IllegalActivityInstanceStateException e = new IllegalActivityInstanceStateException(activityInstance, State.REGISTERED);
				// log.error(e.getMessage());
				// throw e;
			}

			beginDataControlNodeInstance((AbstractDataControlNodeInstance) eventInstance, selectedDataObjects);
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	/**
	 * Begin an {@link AbstractDataControlNodeInstance}. Therefore lock all
	 * {@link DataObject}s that are used by the AbstractDataControlNodeInstance
	 * and set them in the AbstractDataControlNodeInstance.
	 * 
	 * @param instance
	 * @param selectedDataObjects
	 */
	private void beginDataControlNodeInstance(AbstractDataControlNodeInstance instance, List<DataObject> selectedDataObjects) {
		try {
			List<DataObject> lockedDataObjects = dataManager.lockDataObjects(selectedDataObjects);
			instance.setSelectedDataObjects(lockedDataObjects);
			// TODO: has this to be before lock?
			instance.getFragmentInstance().skipAlternativeControlNodes(instance);
			instance.begin();
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	/**
	 * Does everything for terminating an activity excepted from the actual
	 * termination. This will be done after all DataObjects/DataAttributes are
	 * set via a Rest-Method which will be called by the Front-End.
	 * 
	 * @param activityInstanceId
	 * @param dataClassToStateTransition
	 * @return List of DataObjects that made a transition and those that are
	 *         newly created
	 */
	public List<DataObject> handleActivityOutputTransitions(AbstractActivityInstance activityInstance, Map<DataClass, ObjectLifecycleState> dataClassToStateTransitions) {
		try {
			if (!activityInstance.getState().equals(State.RUNNING)) {
				IllegalActivityInstanceStateException e = new IllegalActivityInstanceStateException(activityInstance, State.RUNNING);
				log.error(e.getMessage());
				throw e;
			}
			
			return handleDataControlNodeOutputTransitions((AbstractDataControlNodeInstance) activityInstance, dataClassToStateTransitions);
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	public List<DataObject> handleEventOutputTransitions(AbstractEventInstance eventInstance, Map<DataClass, ObjectLifecycleState> dataClassToStateTransitions) {
		try {
			if (!eventInstance.getState().equals(State.RUNNING)) {
				// IllegalActivityInstanceStateException e = new IllegalActivityInstanceStateException(eventInstance, State.RUNNING);
				// log.error(e.getMessage());
				// throw e;
			}
			
			return handleDataControlNodeOutputTransitions((AbstractDataControlNodeInstance) eventInstance, dataClassToStateTransitions);
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}
	
	private List<DataObject> handleDataControlNodeOutputTransitions(AbstractDataControlNodeInstance controlNodeInstance, Map<DataClass, ObjectLifecycleState> dataClassToStateTransitions) {
		try {
			List<DataObject> workingItems = controlNodeInstance.getSelectedDataObjects();
			dataManager.unlockDataObjects(workingItems);
			
			List<DataObject> usedDataObject = new ArrayList<>();
			if (!controlNodeInstance.getControlNode().getPostCondition().getAtomicDataStateConditions().isEmpty()) {
				usedDataObject = dataManager.handleDataObjectTransitions(workingItems, dataClassToStateTransitions);
			}

			controlNodeInstance.setOutputDataObjects(usedDataObject);
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
	public ControlNodeInstance getControlNodeInstance(String controlNodeId) {
		for (FragmentInstance fragmentInstance : caze.getFragmentInstances().values()) {
			if (fragmentInstance.getControlNodeInstanceIdToInstance().containsKey(controlNodeId)) {
				return fragmentInstance.getControlNodeInstanceIdToInstance().get(controlNodeId);
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
