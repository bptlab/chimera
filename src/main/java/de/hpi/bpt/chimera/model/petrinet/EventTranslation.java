package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;

public class EventTranslation extends AbstractDataControlNodeTranslation {

	public EventTranslation(TranslationContext translationContext, AbstractEvent event) {
		super(translationContext, event, event.getId());

		addTransition(event.getId(), getInnerInitialPlace(), getInnerFinalPlace());
	}

}
