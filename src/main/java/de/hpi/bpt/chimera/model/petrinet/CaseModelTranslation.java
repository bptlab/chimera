package de.hpi.bpt.chimera.model.petrinet;

import java.util.ArrayList;
import java.util.List;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.fragment.Fragment;

public class CaseModelTranslation {
	private final CaseModel caseModel;
	private final PetriNet petriNet;

	private TranslationContext translationContext;
	private final List<FragmentTranslation> fragmentTranslations = new ArrayList<>();

	private Place initialPlace;
	private Place finalPlace;

	public CaseModelTranslation(CaseModel caseModel) {
		this.caseModel = caseModel;
		petriNet = new PetriNet();
		translationContext = new TranslationContext(this);

		initialPlace = petriNet.addPlace(new Place(translationContext, "init"));
		initialPlace.setNumTokens(1);
		finalPlace = petriNet.addPlace(new Place(translationContext, "final"));

		for (Fragment fragment : caseModel.getFragments()) {
			translateFragment(fragment);
		}
	}

	private void translateFragment(Fragment fragment) {
		// Inner fragment translation
		FragmentTranslation fragmentTranslation = new FragmentTranslation(translationContext, fragment);
		fragmentTranslations.add(fragmentTranslation);

		// Fragment initialization
		Transition fragmentInit = new Transition(translationContext, "initialize-fragment-" + fragment.getId(),
				initialPlace, fragmentTranslation.getInitialPlace());
		petriNet.addTransition(fragmentInit);

		// Fragment re-initialization on fragment termination
		Transition fragmentReInit = new Transition(translationContext, "re-initialize-fragment-" + fragment.getId(),
				fragmentTranslation.getFinalPlace(), fragmentTranslation.getInitialPlace());
		petriNet.addTransition(fragmentReInit);
	}

	public PetriNet getPetriNet() {
		return petriNet;
	}
}
