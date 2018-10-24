package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;

public class ActivityTranslation extends AbstractTranslation {
	private final AbstractActivity activity;

	public ActivityTranslation(TranslationContext translationContext, AbstractActivity activity) {
		super(translationContext, activity.getName());
		this.activity = activity;

		PetriNet petriNet = translationContext.getCaseModelTranslation().getPetriNet();
		final String prefixString = translationContext.getPrefixString();

		initialPlace = petriNet.addPlace(prefixString + "init");
		finalPlace = petriNet.addPlace(prefixString + "final");

		Transition transition = new Transition(activity.getName());
		transition.addInputPlace(initialPlace);
		transition.addOutputPlace(finalPlace);
		petriNet.addTransition(transition);
	}
}
