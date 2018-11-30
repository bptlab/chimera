package de.hpi.bpt.chimera.model.petrinet;

public abstract class AbstractSequentialControlNodeTranslation extends AbstractControlNodeTranslation {

	protected final Place initialPlace;
	protected final Place finalPlace;

	public AbstractSequentialControlNodeTranslation(TranslationContext translationContext, String name) {
		super(translationContext, name);

		final String prefixString = this.context.getPrefixString();

		initialPlace = addPlace(prefixString + "init");
		finalPlace = addPlace(prefixString + "final");
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
