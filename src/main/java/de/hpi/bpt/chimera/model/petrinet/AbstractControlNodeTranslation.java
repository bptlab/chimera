package de.hpi.bpt.chimera.model.petrinet;

public abstract class AbstractControlNodeTranslation extends AbstractTranslation {

	protected Place initialPlace;
	protected Place finalPlace;

	public AbstractControlNodeTranslation(TranslationContext translationContext, String name) {
		super(translationContext, name);
	}

	public Place getInitialPlace() {
		return initialPlace;
	}

	public Place getFinalPlace() {
		return finalPlace;
	}
}
