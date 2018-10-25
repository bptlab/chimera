package de.hpi.bpt.chimera.model.petrinet;

import java.util.ArrayList;
import java.util.List;

public class Transition extends AbstractPetriNetNode {
	private final List<Place> inputPlaces = new ArrayList<>();
	private final List<Place> outputPlaces = new ArrayList<>();

	public Transition(TranslationContext context, String name) {
		super(context, name);
	}

	public Transition(TranslationContext context, String name, Place inputPlace, Place outputPlace) {
		this(context, name);
		addInputPlace(inputPlace);
		addOutputPlace(outputPlace);
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
