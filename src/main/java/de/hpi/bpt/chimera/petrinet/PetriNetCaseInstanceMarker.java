package de.hpi.bpt.chimera.petrinet;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.hpi.bpt.chimera.execution.Case;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.model.petrinet.ActivityTranslation;
import de.hpi.bpt.chimera.model.petrinet.CaseModelTranslation;
import de.hpi.bpt.chimera.model.petrinet.DataClassTranslation;
import de.hpi.bpt.chimera.model.petrinet.FragmentTranslation;
import de.hpi.bpt.chimera.model.petrinet.Place;

public class PetriNetCaseInstanceMarker extends AbstractPetriNetMarker {

	private final CaseModelTranslation caseModelTranslation;
	private final Case caseInstance;

	public PetriNetCaseInstanceMarker(CaseModelTranslation caseModelTranslation, Case caseInstance) {
		this.caseModelTranslation = caseModelTranslation;
		this.caseInstance = caseInstance;
		assert (caseModelTranslation.getCaseModel().getId()
				.equals(caseInstance.getCaseExecutioner().getCaseModel().getId()));
	}

	public void addMarkingForInstance() {

		// Available data objects
		Set<DataObject> availableDataObjects = caseInstance.getCaseExecutioner().getDataManager()
				.getAvailableDataObjects();
		for (DataObject availableDataObject : availableDataObjects) {
			addMarkingForDataObject(availableDataObject);
		}

		// TODO bound data objects

		// TODO activity, gateway, event state
		Collection<FragmentInstance> fragmentInstances = caseInstance.getFragmentInstances().values();
		for (FragmentInstance fragmentInstance : fragmentInstances) {
			addMarkingForFragmentInstance(fragmentInstance);
		}

	}

	private void addMarkingForDataObject(DataObject dataObject) {
		DataClass dataObjectClass = dataObject.getDataClass();
		ObjectLifecycleState dataOlcState = dataObject.getObjectLifecycleState();
		DataClassTranslation dataClassTranslation = caseModelTranslation.getDataClassTranslationsByName()
				.get(dataObjectClass.getName());
		Place place = dataClassTranslation.getOlcStatePlacesByName().get(dataOlcState.getName());
		place.addTokens(1);

		System.out.println("Found data object " + dataObjectClass.getName() + "[" + dataOlcState.getName() + "]");
	}

	private void addMarkingForFragmentInstance(FragmentInstance fragmentInstance) {

		switch (fragmentInstance.getState()) {
		case TERMINATED:
			System.out.println("Skipping fragment " + fragmentInstance.getFragment().getName() + " in state "
					+ fragmentInstance.getState());
			break;
		case CREATED:
		case DISABLED:
			getFragmentTranslation(fragmentInstance).getInitialPlace().addTokens(1);
			break;
		case ENABLED:
			getFragmentTranslation(fragmentInstance).getStartEventTranslation().getInnerInitialPlace().addTokens(1);
			break;
		case ACTIVE:
			for (AbstractActivityInstance activityInstance : fragmentInstance.getActivActivityInstances()) {
				addMarkingForActivityInstance(activityInstance);
			}
			break;
		}
	}

	private void addMarkingForActivityInstance(AbstractActivityInstance activityInstance) {

		ActivityTranslation activityTranslation = getActivityTranslation(activityInstance);

		switch (activityInstance.getState()) {
		case CANCEL:
		case EXECUTING:
			// EXECUTING is only for gateways
		case INIT:
		case REGISTERED:
		case SKIPPED:
		case TERMINATED:
			System.out.println("Skipping activity " + activityInstance.getControlNode().getName() + " in state "
					+ activityInstance.getState());
			break;
		case CONTROLFLOW_ENABLED:
			activityTranslation.getInitialPlace().addTokens(1);
			break;
		case DATAFLOW_ENABLED:
			activityTranslation.getInitialPlace().addTokens(1);
			break;
		case READY:
			activityTranslation.getInnerInitialPlace().addTokens(1);
			break;
		case RUNNING:
			activityTranslation.getInnerFinalPlace().addTokens(1);
			break;
		}

		if (!activityInstance.getControlNode().getPreCondition().isEmpty()) {
			List<DataObject> selectedDataObjects = activityInstance.getSelectedDataObjects();
			List<AtomicDataStateCondition> selectedDataStateConditions = selectedDataObjects.stream()
					.map(d -> d.getCondition()).collect(Collectors.toList());

			boolean isBindPlaceFound = false;
			for (ConditionSet conditionSet : activityInstance.getControlNode().getPreCondition().getConditionSets()) {
				if (conditionSet.isFulfilled(selectedDataStateConditions)) {
					activityTranslation.getIoRelationTranslation().getDataObjectBindPlaceByConditionSet()
							.get(conditionSet).addTokens(1);
					isBindPlaceFound = true;
					break;
				}
			}
			assert (isBindPlaceFound);
		}
	}

	private FragmentTranslation getFragmentTranslation(FragmentInstance fragmentInstance) {
		return caseModelTranslation.getFragmentTranslationsByName().get(fragmentInstance.getFragment().getName());
	}

	private ActivityTranslation getActivityTranslation(AbstractActivityInstance activityInstance) {
		FragmentTranslation fragmentTranslation = getFragmentTranslation(activityInstance.getFragmentInstance());
		return (ActivityTranslation) fragmentTranslation.getControlNodeTranslationsById()
				.get(activityInstance.getControlNode().getId());
	}

}
