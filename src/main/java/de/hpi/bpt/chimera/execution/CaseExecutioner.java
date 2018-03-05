package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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

@Entity
public class CaseExecutioner {
	private static final Logger log = Logger.getLogger(CaseExecutioner.class);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int dbId;

	@OneToOne(cascade = CascadeType.ALL)
	private Case caze;
	@OneToOne
	private CaseModel caseModel;
	@OneToOne(cascade = CascadeType.ALL)
	private DataManager dataManager;
	@Column(name = "CaseExecutionerTerminated")
	private boolean terminated;
	@OneToMany(cascade = CascadeType.ALL)
	private List<ActivityLog> activityLogs;
	@OneToMany(cascade = CascadeType.ALL)
	private List<DataObjectLog> dataObjectLogs;
	@OneToMany(cascade = CascadeType.ALL)
	private List<DataAttributeLog> dataAttributeLogs;


	/**
	 * for JPA only
	 */
	public CaseExecutioner() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}


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
	 * Begin an {@link AbstractDataControlNodeInstance} if it is in the correct
	 * state for begin. Therefore lock all {@link DataObject}s that are used by
	 * the AbstractDataControlNodeInstance and set them for the
	 * AbstractDataControlNodeInstance.
	 * 
	 * @param instance
	 *            - AbstractDataControlNodeInstance that shall begin
	 * @param selectedDataObjects-
	 *            list of {@code ids } of DataObjects that are used by the
	 *            AbstractActivityInstance
	 */
	public void beginDataControlNodeInstance(AbstractDataControlNodeInstance instance, List<DataObject> selectedDataObjects) {
		try {
			if (!instance.canBegin()) {
				IllegalArgumentException e = new IllegalArgumentException("DataControlNodeInstance cannot begin");
				log.error(e.getMessage());
				throw e;
			}
			dataManager.lockDataObjects(selectedDataObjects);
			instance.setSelectedDataObjects(selectedDataObjects);
			// TODO: has this to be before lock?
			instance.getFragmentInstance().skipAlternativeControlNodes(instance);
			instance.begin();
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	/**
	 * Terminate an {@link DataControlNodeInstance}. Therefore handle the
	 * transitions of the DataObjects specified by
	 * {@code dataClassToStateTransition}. For additionally specifying the
	 * values for the DataAttributeInstances of the used DataObjects use
	 * {@link #terminateDataControlNodeInstance(AbstractDataControlNodeInstance, Map, Map)
	 * terminateDataControlNodeInstance}.
	 * 
	 * @param controlNodeInstance
	 *            - AbstractDataControlNodeInstance that shall terminate
	 * @param dataClassToStateTransition
	 *            - Map from DataClass to an ObjectLifecycleState of the
	 *            DataClass to define the new State for the DataObject with the
	 *            referred DataClass
	 * 
	 * @see {@link #terminateDataControlNodeInstance(AbstractDataControlNodeInstance, Map, Map)
	 *      terminateDataControlNodeInstance}
	 */
	public void terminateDataControlNodeInstance(AbstractDataControlNodeInstance controlNodeInstance, Map<DataClass, ObjectLifecycleState> dataClassToStateTransitions) {
		try {
			terminateDataControlNodeInstance(controlNodeInstance, dataClassToStateTransitions, new HashMap<>());
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	/**
	 * Terminate an {@link DataControlNodeInstance}. Therefore handle the
	 * transitions of the DataObjects specified by
	 * {@code dataClassToStateTransition}. After the transitions of the
	 * DataObjects change the values of those DataAttributeInstances specified
	 * by rawDataAttributeValues.
	 * 
	 * @param controlNodeInstance
	 *            - AbstractDataControlNodeInstance that shall terminate
	 * @param dataClassToStateTransitions
	 *            - Map from DataClass to an ObjectLifecycleState of the
	 *            DataClass to define the new State for the DataObject with the
	 *            referred DataClass
	 * @param rawDataAttributeValues
	 *            - Map from name of DataClass to a Map from DataAttribute name
	 *            to new DataAttributeInstance value
	 */
	public void terminateDataControlNodeInstance(AbstractDataControlNodeInstance controlNodeInstance, Map<DataClass, ObjectLifecycleState> dataClassToStateTransitions, Map<String, Map<String, Object>> rawDataAttributeValues) {
		try {
			if (!controlNodeInstance.canTerminate()) {
				IllegalArgumentException e = new IllegalArgumentException("DataControlNodeInstance cannot terminate");
				log.error(e.getMessage());
				throw e;
			}
			
			List<DataObject> usedDataObjects = handleDataControlNodeOutputTransitions(controlNodeInstance, dataClassToStateTransitions);
			dataManager.setDataAttributeValuesByNames(rawDataAttributeValues, usedDataObjects);
			controlNodeInstance.setOutputDataObjects(usedDataObjects);
			controlNodeInstance.terminate();
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}
	
	/**
	 * Handle the data object transition for an AbstractControlNodeInstance.
	 * Therefore unlock all DataObjects of the instance that were defined at the
	 * beginning of the instance. In addition let the DataManager handle the
	 * transitions of the DataObjects specified by the
	 * dataClassToStateTransitions.
	 * 
	 * @param controlNodeInstance
	 *            - AbstractDataControlNodeInstance to handle
	 * @param dataClassToStateTransitions
	 *            - Map from DataClass to an ObjectLifecycleState of the
	 *            DataClass to define the new State for the DataObject with the
	 *            referred DataClass
	 * @return List of all DataObjects that were used as working items and
	 *         created by the DataManager
	 */
	private List<DataObject> handleDataControlNodeOutputTransitions(AbstractDataControlNodeInstance controlNodeInstance, Map<DataClass, ObjectLifecycleState> dataClassToStateTransitions) {
		try {
			List<DataObject> workingItems = controlNodeInstance.getSelectedDataObjects();
			dataManager.unlockDataObjects(workingItems);
			
			// TODO: validate whether this works correct
			List<DataObject> usedDataObject = new ArrayList<>();
			if (!controlNodeInstance.getControlNode().getPostCondition().getAtomicDataStateConditions().isEmpty()) {
				usedDataObject = dataManager.handleDataObjectTransitions(workingItems, dataClassToStateTransitions);
			}

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

	/**
	 * Get an ControlNodeInstance over all FragmentInstances of the Case by an
	 * id.
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
