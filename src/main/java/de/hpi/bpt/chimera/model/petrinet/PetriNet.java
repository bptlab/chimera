package de.hpi.bpt.chimera.model.petrinet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class PetriNet {
	private Collection<Place> places = new ArrayList<>();
	private Collection<Transition> transitions = new ArrayList<>();

	public PetriNet() {
		EntityWithId.resetIdCounter();
	}

	public Place addPlace(Place newPlace) {
		places.add(newPlace);
		return newPlace;
	}

	public Collection<Place> getPlaces() {
		return places.stream().distinct().collect(Collectors.toList());
	}

	public Transition addTransition(Transition newTransition) {
		transitions.add(newTransition);
		return newTransition;
	}

	public Collection<Transition> getTransitions() {
		return transitions.stream().distinct().collect(Collectors.toList());
	}
}
