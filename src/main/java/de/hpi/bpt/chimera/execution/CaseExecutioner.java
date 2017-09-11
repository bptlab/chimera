package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;

public class CaseExecutioner {
	private static Logger log = Logger.getLogger(CaseExecutioner.class);
	private Case caze;
	private CaseModel caseModel;
	private DataManager dataManager;

	public CaseExecutioner(CaseModel caseModel, String caseName) {
		this.caseModel = caseModel;
		this.caze = new Case(caseName, caseModel, this);
		log.info("Case created");
		this.dataManager = new DataManager(caze, caseModel.getDataModel());
		log.info("DataManager created");
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
		dataManager.createDataObjectInstances(node);
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
	public void terminateActivityInstance(String activityInstanceId, Map<String, String> dataObjectTransitions) {
		ControlNodeInstance nodeInstance = getControlNodeInstance(activityInstanceId);
		if (nodeInstance == null)
			return;

		if (nodeInstance instanceof AbstractActivityInstance && nodeInstance.getState() == State.READY) {
			Map<String, DataObjectInstance> toUnlockDataObjectInstances = ((AbstractActivityInstance) nodeInstance).getSelectedDataObjectInstances();
			dataManager.unlockDataObjectInstances(toUnlockDataObjectInstances);
			dataManager.transitionDataObjectInstances(dataObjectTransitions);
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
	private ControlNodeInstance getControlNodeInstance(String activityInstanceId) {
		for (FragmentInstance fragmentInstance : caze.getFragmentInstances().values()) {
			if (fragmentInstance.getControlNodeInstances().containsKey(activityInstanceId)) {
				return fragmentInstance.getControlNodeInstances().get(activityInstanceId);
			}
		}
		return null;
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
}
