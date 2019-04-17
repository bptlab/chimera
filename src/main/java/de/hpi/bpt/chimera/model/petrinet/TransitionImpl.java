package de.hpi.bpt.chimera.model.petrinet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A transition in a Petri net.
 */
public class TransitionImpl extends EntityWithId implements Transition {

	private final TranslationContext context;
	private final String name;
	private Fairness fairness = Fairness.NONE;
	private final Set<Place> inputPlaces = new HashSet<>();
	private final Set<Place> outputPlaces = new HashSet<>();

	public TransitionImpl(TranslationContext context, String name) {
		this.context = context;
		this.name = name;
	}

	public TransitionImpl(TranslationContext context, String name, Place inputPlace, Place outputPlace) {
		this(context, name);
		addInputPlace(inputPlace);
		addOutputPlace(outputPlace);
	}

	@Override
	public TransitionImpl addInputPlace(Place inputPlace) {
		inputPlaces.add(inputPlace);
		return this;
	}

	@Override
	public TransitionImpl addOutputPlace(Place outputPlace) {
		outputPlaces.add(outputPlace);
		return this;
	}

	@Override
	public final Collection<Place> getInputPlaces() {
		return inputPlaces;
	}

	@Override
	public final Collection<Place> getOutputPlaces() {
		return outputPlaces;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public TranslationContext getContext() {
		return context;
	}

	@Override
	public Fairness getFairness() {
		return fairness;
	}

	@Override
	public void setFairness(Fairness fairness) {
		this.fairness = fairness;
	}
}
