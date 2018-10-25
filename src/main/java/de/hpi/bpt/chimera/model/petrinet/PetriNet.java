package de.hpi.bpt.chimera.model.petrinet;

import java.util.ArrayList;
import java.util.List;

public class PetriNet {
	private List<Place> places = new ArrayList<>();
	private List<Transition> transitions = new ArrayList<>();

	public Place addPlace(Place newPlace) {
		places.add(newPlace);
		return newPlace;
	}

	public List<Place> getPlaces() {
		return places;
	}

	public void setPlaces(List<Place> places) {
		this.places = places;
	}

	public Transition addTransition(Transition newTransition) {
		transitions.add(newTransition);
		return newTransition;
	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}
}
