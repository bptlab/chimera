package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;

public class EventTranslation extends AbstractDataControlNodeTranslation {

	public EventTranslation(TranslationContext translationContext, AbstractEvent event) {
		super(translationContext, event, event.getId());

		final String prefixString = this.context.getPrefixString();

		System.out.println("Event " + event.getId() + "inner initial place: " + getInnerInitialPlace().getName()
				+ " inner final place: " + getInnerFinalPlace());
		addTransition(prefixString, getInnerInitialPlace(), getInnerFinalPlace());
	}

}
