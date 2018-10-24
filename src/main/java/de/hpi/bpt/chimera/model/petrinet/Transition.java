package de.hpi.bpt.chimera.model.petrinet;

import java.util.ArrayList;
import java.util.List;

import de.hpi.bpt.chimera.model.Nameable;

public class Transition implements Nameable {
	private String name;
	private final List<Place> inputPlaces = new ArrayList<>();
	private final List<Place> outputPlaces = new ArrayList<>();

	public Transition() {

	}

	public Transition(String name) {
		setName(name);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public Transition addInputPlace(Place inputPlace) {
		inputPlaces.add(inputPlace);
		return this;
	}

	public Transition addOutputPlace(Place outputPlace) {
		outputPlaces.add(outputPlace);
		return this;
	}

	public final List<Place> getInputPlaces() {
		return inputPlaces;
	}

	public final List<Place> getOutputPlaces() {
		return outputPlaces;
	}
}
