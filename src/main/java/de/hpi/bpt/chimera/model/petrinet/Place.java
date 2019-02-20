package de.hpi.bpt.chimera.model.petrinet;

public interface Place extends AbstractPetriNetNode {

	@Override
	default String getPrefixedIdString() {
		return "p_" + String.valueOf(getId());
	}

	public int getNumTokens();

	public void setNumTokens(int numTokens);

	public void addTokens(int numTokens);

	public boolean isSignificant();

	public void setSignificant(boolean isSignificant);
}
