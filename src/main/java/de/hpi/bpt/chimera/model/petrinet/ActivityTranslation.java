package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;

public class ActivityTranslation extends AbstractDataControlNodeTranslation {

	public ActivityTranslation(TranslationContext translationContext, AbstractActivity activity) {
		super(translationContext, activity, activity.getId());

		final String prefixString = this.context.getPrefixString();

		addTransition(prefixString, getInnerInitialPlace(), getInnerFinalPlace());
	}
}
