package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;

public abstract class AbstractSequentialControlNodeTranslation extends AbstractControlNodeTranslation {

	protected final Place initialPlace;
	protected final Place finalPlace;

	public AbstractSequentialControlNodeTranslation(TranslationContext translationContext, AbstractControlNode node) {
		super(translationContext, node);

		initialPlace = addPlace("init");
		finalPlace = addPlace("final");
	}

	@Override
	public Place getInitialPlace() {
		return initialPlace;
	}

	@Override
	public Place getFinalPlace() {
		return finalPlace;
	}
}
