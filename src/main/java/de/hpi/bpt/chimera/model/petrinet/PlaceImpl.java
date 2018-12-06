package de.hpi.bpt.chimera.model.petrinet;

public class PlaceImpl implements Place {

	private final TranslationContext context;
	private final String name;
	private int numTokens = 0;

	// Whether this Place represents information that must not be optimized out
	private boolean isSignificant = false;

	public PlaceImpl(TranslationContext context, String name) {
		this.context = context;
		this.name = name;
	}

	@Override
	public int getNumTokens() {
		return numTokens;
	}

	@Override
	public void setNumTokens(int numTokens) {
		this.numTokens = numTokens;
	}

	@Override
	public boolean isSignificant() {
		return isSignificant;
	}

	@Override
	public void setSignificant(boolean isSignificant) {
		this.isSignificant = isSignificant;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public TranslationContext getContext() {
		return context;
	}
}
