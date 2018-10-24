package de.hpi.bpt.chimera.model.petrinet;

public abstract class AbstractTranslation {

	protected final TranslationContext translationContext;
	protected final String name;

	protected Place initialPlace;
	protected Place finalPlace;

	public AbstractTranslation(TranslationContext translationContext, String name) {
		this.translationContext = translationContext.withPrefix(name);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getPrefixString() {
		return translationContext.getPrefixString();
	}

	public PetriNet getPetriNet() {
		return translationContext.getCaseModelTranslation().getPetriNet();
	}

	public Place getInitialPlace() {
		return initialPlace;
	}

	public Place getFinalPlace() {
		return finalPlace;
	}
}
