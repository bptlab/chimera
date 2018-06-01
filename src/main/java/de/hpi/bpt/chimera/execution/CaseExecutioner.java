package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import de.hpi.bpt.chimera.execution.controlnodes.event.behavior.MessageReceiveEventBehavior;
import de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.execution.data.DataAttributeInstance;
import de.hpi.bpt.chimera.execution.data.DataManager;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.execution.exception.IllegalControlNodeInstanceIdException;
import de.hpi.bpt.chimera.execution.exception.IllegalControlNodeInstanceTypeException;
import de.hpi.bpt.chimera.history.transportationbeans.ActivityLog;
import de.hpi.bpt.chimera.history.transportationbeans.DataAttributeLog;
import de.hpi.bpt.chimera.history.transportationbeans.DataObjectLog;
import de.hpi.bpt.chimera.history.transportationbeans.LogEntry;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.model.condition.TerminationCondition;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

@Entity
public class CaseExecutioner {
	private static final Logger log = Logger.getLogger(CaseExecutioner.class);

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private int dbId;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "caseExecutioner")
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
	@OneToMany(cascade = CascadeType.ALL)
	private Map<String, MessageReceiveEventBehavior> registeredEventInstanceIdToReceiveBehavior;

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

		this.registeredEventInstanceIdToReceiveBehavior = new HashMap<>();
	}

	/**
	 * Start the Case by starting all {@link FragmentInstance}s.
	 */
	public void startCase() {
		for (FragmentInstance fragmentInstance : caze.getFragmentInstances().values()) {
			fragmentInstance.enable();
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
	 * transitions of the DataObjects that result from the
	 * {@link DataStateCondition PostCondition}. Therefore the post condition
	 * needs to be unique. If the post condition is not unique,
	 * {@link #terminateDataControlNodeInstance(AbstractDataControlNodeInstance, Map)
	 * terminateDataControlNodeInstance} needs to be used. For additionally
	 * specifying the values for the DataAttributeInstances of the used
	 * DataObjects use
	 * {@link #terminateDataControlNodeInstance(AbstractDataControlNodeInstance, Map, Map)
	 * terminateDataControlNodeInstance}.
	 * 
	 * @param controlNodeInstance
	 *            - AbstractDataControlNodeInstance that shall terminate
	 * 
	 * @see {@link #terminateDataControlNodeInstance(AbstractDataControlNodeInstance, Map, Map)
	 *      terminateDataControlNodeInstance}
	 */
	public void terminateDataControlNodeInstance(AbstractDataControlNodeInstance controlNodeInstance) {
		try {
			if (!controlNodeInstance.canTerminate()) {
				IllegalArgumentException e = new IllegalArgumentException("DataControlNodeInstance cannot terminate");
				log.error(e.getMessage());
				throw e;
			}

			if (!controlNodeInstance.getControlNode().hasUniquePostCondition()) {
				return;
			}

			Map<DataClass, ObjectLifecycleState> dataClassToStateTransitions = new HashMap<>();
			if (controlNodeInstance.getControlNode().hasPostCondition()) {
				dataClassToStateTransitions = controlNodeInstance.getControlNode().getPostCondition().getConditionSets().get(0).getDataClassToObjectLifecycleState();
			}

			terminateDataControlNodeInstance(controlNodeInstance, dataClassToStateTransitions, new HashMap<>());
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
	 * @param dataClassToStateTransitions
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
	 * Update DataFlow of all ActivityInstances.
	 */
	public void updateDataFlow() {
		for (FragmentInstance fragmentInstance : caze.getFragmentInstances().values()) {
			fragmentInstance.checkDataFlow();
			fragmentInstance.getActivActivityInstances()
				.forEach(AbstractActivityInstance::checkDataFlow);
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
	 * Get an ControlNodeInstance over all FragmentInstances of the Case by an
	 * id.
	 * 
	 * @param instanceId
	 *            - id of the ControlNodeInstance
	 * @return the associated ControlNodeInstance if controlNodeId is assigned.
	 * @throws IllegalControlNodeInstanceIdException
	 *             if instanceId is not assigned.
	 */
	public ControlNodeInstance getControlNodeInstance(String instanceId) {
		for (FragmentInstance fragmentInstance : caze.getFragmentInstances().values()) {
			if (fragmentInstance.getControlNodeInstanceIdToInstance().containsKey(instanceId)) {
				return fragmentInstance.getControlNodeInstanceIdToInstance().get(instanceId);
			}
		}

		IllegalControlNodeInstanceIdException e = new IllegalControlNodeInstanceIdException(instanceId);
		log.error(e.getMessage());
		throw e;
	}

	/**
	 * Get a specific ActivityInstance by an id.
	 * 
	 * @param activityInstanceId
	 *            - Id of the ActivityInstance.
	 * @return the associated AbstractActivityInstance
	 * @throws IllegalControlNodeInstanceIdException
	 *             if controlNodeId is not assigned.
	 * @throws IllegalControlNodeInstanceTypeException
	 *             if associated ControlNodeInstance is not an ActivityInstance.
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

	/**
	 * Checks whether the Case can be terminated by testing if the
	 * TerminationCondition is fulfilled.
	 * 
	 * @return true if the Case can be terminated.
	 */
	public boolean canTerminate() {
		TerminationCondition terminationCondition = getCaseModel().getTerminationCondition();
		return terminationCondition.isFulfilled(dataManager.getDataStateConditions());
	}

	/**
	 * Terminate the Case. Only possible if the TerminationCondition is
	 * fulfilled.
	 * 
	 * @see #canTerminate()
	 */
	public void terminate() {
		// TODO think about the consequences
		if (canTerminate()) {
			// set all ControlNodeInstances to state Skipped, so no further
			// controlflow is possible.
			List<ControlNodeInstance> controlNodeInstances = this.getCase().getFragmentInstances().values().stream().map(FragmentInstance::getControlNodeInstances).flatMap(List::stream).collect(Collectors.toList());
			controlNodeInstances.forEach(each -> each.setState(State.SKIPPED));
			log.info("Terminating the Case" + this.getCase().getName() + ". All ControlNodeInstances are skipped.");

			EventDispatcher.deregisterReceiveEventsOfCase(this.getCase());

			setTerminated(true);
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

	/**
	 * Store the {@link MessageReceiveEventBehavior} of an
	 * {@link AbstractEventInstance} that has previously been registered at
	 * Unicorn by the message receive behavior of the event instance.
	 * 
	 * @param receiveBehavior
	 *            - MessageReceiveEventBehavior of the registered EventInstance
	 */
	public void addRegisteredEventBehavior(MessageReceiveEventBehavior receiveBehavior) {
		String instanceId = receiveBehavior.getEventInstance().getId();
		if (!registeredEventInstanceIdToReceiveBehavior.containsKey(instanceId)) {
			registeredEventInstanceIdToReceiveBehavior.put(instanceId, receiveBehavior);
		}
	}

	/**
	 * Remove the {@link MessageReceiveEventBehavior} of an
	 * {@link AbstractEventInstance} that has previously been registered at
	 * Unicorn by the message receive behavior of the event instance.
	 * 
	 * @param receiveBehavior
	 *            - MessageReceiveEventBehavior of the EventInstance to be
	 *            de-registered
	 */
	public void removeRegisteredEventBehavior(MessageReceiveEventBehavior receiveBehavior) {
		String instanceId = receiveBehavior.getEventInstance().getId();
		if (registeredEventInstanceIdToReceiveBehavior.containsKey(instanceId)) {
			registeredEventInstanceIdToReceiveBehavior.remove(instanceId, receiveBehavior);
		}
	}

	/**
	 * Receive the {@link MessageReceiveEventBehavior} of an
	 * {@link AbstractEventInstance} that has previously been registered at
	 * Unicorn by the Id of the event instance.
	 * 
	 * @param eventInstanceId
	 *            - Id of the registered event
	 * @return MessageReceiveEventBehavior of the specified registered
	 *         EventInstance
	 * @throws IllegalArgumentException
	 *             if the Id is not assigned
	 */
	public MessageReceiveEventBehavior getRegisteredEventBehavior(String eventInstanceId) {
		if (registeredEventInstanceIdToReceiveBehavior.containsKey(eventInstanceId)) {
			return registeredEventInstanceIdToReceiveBehavior.get(eventInstanceId);
		}
		String message = String.format("The catch event id: %s is not assigned", eventInstanceId);
		log.error(message);
		throw new IllegalArgumentException(message);
	}

	/**
	 * Receive all {@link MessageReceiveEventBehavior} of previously registered
	 * {@link AbstractEventInstance}.
	 * 
	 * @return List of {@link MessageReceiveEventBehavior} which
	 *         {@link AbstractEventInstance} is registered in Unicorn.
	 */
	public List<MessageReceiveEventBehavior> getRegisteredEventBehaviors() {
		return new ArrayList<>(registeredEventInstanceIdToReceiveBehavior.values());
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

	public Map<String, MessageReceiveEventBehavior> getRegisteredEventInstanceIdToReceiveBehavior() {
		return registeredEventInstanceIdToReceiveBehavior;
	}
}
