package de.hpi.bpt.chimera.model.petrinet;

import java.util.Collection;

/**
 * A modifiable reference to a transition in a Petri net.
 */
public class TransitionReference implements Transition {

	private TransitionImpl impl;

	public TransitionReference(TransitionImpl impl) {
		this.impl = impl;
	}

	public TransitionReference(TranslationContext context, String name) {
		this(new TransitionImpl(context, name));
	}

	public TransitionReference(TranslationContext context, String name, Place inputPlace, Place outputPlace) {
		this(new TransitionImpl(context, name, inputPlace, outputPlace));
	}

	public void setImpl(TransitionImpl newImpl) {
		this.impl = newImpl;
	}

	public TransitionImpl getImpl() {
		return impl;
	}

	@Override
	public Transition addInputPlace(Place inputPlace) {
		return impl.addInputPlace(inputPlace);
	}

	@Override
	public Transition addOutputPlace(Place outputPlace) {
		return impl.addOutputPlace(outputPlace);
	}

	@Override
	public final Collection<Place> getInputPlaces() {
		return impl.getInputPlaces();
	}

	@Override
	public final Collection<Place> getOutputPlaces() {
		return impl.getOutputPlaces();
	}

	@Override
	public String getName() {
		return impl.getName();
	}

	@Override
	public TranslationContext getContext() {
		return impl.getContext();
	}
}
