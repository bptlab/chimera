package de.hpi.bpt.chimera.model.petrinet;

public abstract class AbstractControlNodeTranslation extends AbstractTranslation {

	protected final Place initialPlace;
	protected final Place finalPlace;

	public AbstractControlNodeTranslation(TranslationContext translationContext, String name) {
		super(translationContext, name);

		final String prefixString = this.context.getPrefixString();

		initialPlace = addPlace(prefixString + "init");
		finalPlace = addPlace(prefixString + "final");
	}

	public Place getInitialPlace() {
		return initialPlace;
	}

	public Place getFinalPlace() {
		return finalPlace;
	}
}
