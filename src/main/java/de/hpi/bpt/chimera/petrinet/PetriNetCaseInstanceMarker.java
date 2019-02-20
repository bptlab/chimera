package de.hpi.bpt.chimera.petrinet;

import java.util.Collection;
import java.util.Set;

import de.hpi.bpt.chimera.execution.Case;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
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
		case CREATED:
		case DISABLED:
		case TERMINATED:
			return;
		case ENABLED:
			addMarkingForEnabledFragment(fragmentInstance);
			break;
		case ACTIVE:
			for (AbstractActivityInstance activityInstance : fragmentInstance.getActivActivityInstances()) {
				addMarkingForActivityInstance(activityInstance);
			}
			break;
		}
	}

	private void addMarkingForEnabledFragment(FragmentInstance fragmentInstance) {
		FragmentTranslation fragmentTranslation = caseModelTranslation.getFragmentTranslationsByName()
				.get(fragmentInstance.getFragment().getName());
		Place initialPlace = fragmentTranslation.getInitialPlace();
		initialPlace.addTokens(1);
	}

	private void addMarkingForActivityInstance(AbstractActivityInstance activityInstance) {
		switch (activityInstance.getState()) {
		case CANCEL:
		case INIT:
		case REGISTERED:
		case SKIPPED:
		case TERMINATED:
			return;
		case CONTROLFLOW_ENABLED:
			break;
		case DATAFLOW_ENABLED:
			break;
		case EXECUTING:
			break;
		case READY:
			break;
		case RUNNING:
			break;
		}
	}

}
