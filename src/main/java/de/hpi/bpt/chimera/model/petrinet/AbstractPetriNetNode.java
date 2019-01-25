package de.hpi.bpt.chimera.model.petrinet;

public interface AbstractPetriNetNode {

	public int getId();

	public String getPrefixedIdString();

	public String getName();

	public TranslationContext getContext();

}
