package de.hpi.bpt.chimera.model.petrinet;

public abstract class AbstractPetriNetNode {

	private final String name;
	private final TranslationContext context;

	AbstractPetriNetNode(TranslationContext context, String name) {
		this.context = context;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public TranslationContext getContext() {
		return context;
	}
}
