package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;

public class EventTranslation extends AbstractDataControlNodeTranslation {

	private final Transition eventTransition;

	public EventTranslation(TranslationContext translationContext, AbstractEvent event) {
		super(translationContext, event);

		eventTransition = addTransition(event.getId(), getInnerInitialPlace(), getInnerFinalPlace());
	}

	public Transition getEventTransition() {
		return eventTransition;
	}
}
