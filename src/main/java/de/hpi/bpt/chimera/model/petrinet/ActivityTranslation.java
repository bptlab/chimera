package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;

public class ActivityTranslation extends AbstractControlNodeTranslation {

	public ActivityTranslation(TranslationContext translationContext, AbstractActivity activity) {
		super(translationContext, activity.getId());

		final String prefixString = this.context.getPrefixString();

		initialPlace = addPlace(prefixString + "init");
		finalPlace = addPlace(prefixString + "final");

		addTransition(prefixString, initialPlace, finalPlace);
	}
}
