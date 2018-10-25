package de.hpi.bpt.chimera.model.petrinet;

public class Place extends AbstractPetriNetNode {
	private int numTokens = 0;

	public Place(TranslationContext context, String name) {
		super(context, name);
	}

	public int getNumTokens() {
		return numTokens;
	}

	public void setNumTokens(int numTokens) {
		this.numTokens = numTokens;
	}
}
