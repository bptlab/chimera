package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;

public class EventTranslation extends AbstractTranslation {
	private final AbstractEvent event;

	public EventTranslation(TranslationContext translationContext, AbstractEvent event) {
		super(translationContext, event.getName());
		this.event = event;

		PetriNet petriNet = translationContext.getCaseModelTranslation().getPetriNet();
		final String prefixString = translationContext.getPrefixString();

		initialPlace = petriNet.addPlace(prefixString + "init");
		finalPlace = petriNet.addPlace(prefixString + "final");

		Transition transition = new Transition(prefixString);
		transition.addInputPlace(initialPlace);
		transition.addOutputPlace(finalPlace);
		petriNet.addTransition(transition);
	}
}
