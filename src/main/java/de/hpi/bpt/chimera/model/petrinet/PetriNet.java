package de.hpi.bpt.chimera.model.petrinet;

import java.util.Collection;
import java.util.HashSet;

public class PetriNet {
	private Collection<Place> places = new HashSet<>();
	private Collection<Transition> transitions = new HashSet<>();

	public Place addPlace(Place newPlace) {
		places.add(newPlace);
		return newPlace;
	}

	public Collection<Place> getPlaces() {
		return places;
	}

	public Transition addTransition(Transition newTransition) {
		transitions.add(newTransition);
		return newTransition;
	}

	public Collection<Transition> getTransitions() {
		return transitions;
	}
}
