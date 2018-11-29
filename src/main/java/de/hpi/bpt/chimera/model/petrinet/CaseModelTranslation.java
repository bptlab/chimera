package de.hpi.bpt.chimera.model.petrinet;

import java.util.HashMap;
import java.util.Map;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.condition.TerminationCondition;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.fragment.Fragment;

public class CaseModelTranslation {
	private final CaseModel caseModel;
	private final PetriNet petriNet;

	private TranslationContext translationContext;
	private final Map<String, FragmentTranslation> fragmentTranslationsByName = new HashMap<>();
	private final Map<String, DataClassTranslation> dataClassTranslationsByName = new HashMap<>();
	private DataStateConditionTranslation terminationConditionTranslation;

	private Place initialPlace;
	private Place finalPlace;
	private Transition fragmentInitializationTransition;

	public CaseModelTranslation(CaseModel caseModel) {
		this.caseModel = caseModel;
		petriNet = new PetriNet();
		translationContext = new TranslationContext(this);

		initialPlace = petriNet.addPlace(new Place(translationContext, "init"));
		initialPlace.setNumTokens(1);
		finalPlace = petriNet.addPlace(new Place(translationContext, "final"));

		fragmentInitializationTransition = petriNet
				.addTransition(new Transition(translationContext, "fragmentInitialization"));
		fragmentInitializationTransition.addInputPlace(initialPlace);

		for (DataClass dataClass : caseModel.getDataModel().getDataClasses()) {
			translateDataClass(dataClass);
		}

		for (Fragment fragment : caseModel.getFragments()) {
			translateFragment(fragment);
		}

		translateTerminationCondition(caseModel.getTerminationCondition());
	}

	private void translateFragment(Fragment fragment) {
		// Inner fragment translation
		FragmentTranslation fragmentTranslation = new FragmentTranslation(translationContext, fragment);
		fragmentTranslationsByName.put(fragment.getName(), fragmentTranslation);

		// Fragment initialization
		fragmentInitializationTransition.addOutputPlace(fragmentTranslation.getInitialPlace());

		// Fragment re-initialization on fragment termination
		Transition fragmentReInit = new Transition(translationContext, "reInitializeFragment-" + fragment.getId(),
				fragmentTranslation.getFinalPlace(), fragmentTranslation.getInitialPlace());
		petriNet.addTransition(fragmentReInit);
	}

	private void translateDataClass(DataClass dataClass) {
		dataClassTranslationsByName.put(dataClass.getName(), new DataClassTranslation(translationContext, dataClass));
	}

	private void translateTerminationCondition(TerminationCondition terminationCondition) {
		Place pseudoFragmentPlace = petriNet.addPlace(new Place(this.translationContext, "terminationConditionPre"));

		fragmentInitializationTransition.addOutputPlace(pseudoFragmentPlace);

		terminationConditionTranslation = new DataStateConditionTranslation(this.translationContext,
				terminationCondition, "terminationCondition", pseudoFragmentPlace, this.finalPlace);
	}

	public PetriNet getPetriNet() {
		return petriNet;
	}

	public Map<String, DataClassTranslation> getDataClassTranslationsByName() {
		return dataClassTranslationsByName;
	}
}
