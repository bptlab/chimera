package de.hpi.bpt.chimera.model.petrinet;

public abstract class AbstractControlNodeTranslation extends AbstractTranslation {

	public AbstractControlNodeTranslation(TranslationContext translationContext, String name) {
		super(translationContext, name);
	}

	public abstract Place getInitialPlace();

	public abstract Place getFinalPlace();
}
