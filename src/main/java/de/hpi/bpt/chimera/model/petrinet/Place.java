package de.hpi.bpt.chimera.model.petrinet;

public interface Place extends AbstractPetriNetNode {

	public int getNumTokens();

	public void setNumTokens(int numTokens);

	public boolean isSignificant();

	public void setSignificant(boolean isSignificant);
}
