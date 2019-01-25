package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;

public class ActivityTranslation extends AbstractDataControlNodeTranslation {

	public ActivityTranslation(TranslationContext translationContext, AbstractActivity activity) {
		super(translationContext, activity, activity.getId());

		addTransition(activity.getId(), getInnerInitialPlace(), getInnerFinalPlace());
	}
}
