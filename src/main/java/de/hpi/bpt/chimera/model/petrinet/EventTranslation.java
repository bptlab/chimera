package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;

public class EventTranslation extends AbstractTranslation {

	public EventTranslation(TranslationContext translationContext, AbstractEvent event) {
		super(translationContext, event.getId());

		final String prefixString = this.context.getPrefixString();

		initialPlace = addPlace(prefixString + "init");
		finalPlace = addPlace(prefixString + "final");

		addTransition(prefixString, initialPlace, finalPlace);
	}

}
