package de.hpi.bpt.chimera.petrinet;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import de.hpi.bpt.chimera.model.petrinet.Place;
import de.hpi.bpt.chimera.model.petrinet.Transition;

public class PetriNetCluster {
	private final String name;
	private final Map<String, PetriNetCluster> childrenByName = new HashMap<>();
	private final Collection<Place> places = new HashSet<>();
	private final Collection<Transition> transitions = new HashSet<>();

	public PetriNetCluster(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Collection<Place> getPlaces() {
		return places;
	}

	public void addPlace(Place p) {
		places.add(p);
	}

	public Collection<Transition> getTransitions() {
		return transitions;
	}

	public void addTransition(Transition t) {
		transitions.add(t);
	}

	public PetriNetCluster getChildByName(String clusterName) {
		if (!childrenByName.containsKey(clusterName)) {
			childrenByName.put(clusterName, new PetriNetCluster(clusterName));
		}
		return childrenByName.get(clusterName);
	}

	public Collection<PetriNetCluster> getChildren() {
		return childrenByName.values();
	}
}