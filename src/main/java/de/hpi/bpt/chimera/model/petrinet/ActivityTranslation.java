package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;

public class ActivityTranslation extends AbstractDataControlNodeTranslation {

	public ActivityTranslation(TranslationContext translationContext, AbstractActivity activity) {
		super(translationContext, activity);

		String name = activity.getName().isEmpty() ? activity.getId() : activity.getName();

		addTransition(name, getInnerInitialPlace(), getInnerFinalPlace());
	}
}
