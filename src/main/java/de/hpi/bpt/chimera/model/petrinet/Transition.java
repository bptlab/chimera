package de.hpi.bpt.chimera.model.petrinet;

import java.util.Collection;

public interface Transition extends AbstractPetriNetNode {

	public Transition addInputPlace(Place inputPlace);

	public Transition addOutputPlace(Place outputPlace);

	public Collection<Place> getInputPlaces();

	public Collection<Place> getOutputPlaces();
}
