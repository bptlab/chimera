package de.hpi.bpt.chimera.model.petrinet;

import java.util.ArrayList;
import java.util.List;

import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.AbstractGateway;

public abstract class AbstractGatewayTranslation extends AbstractControlNodeTranslation {

	protected final List<Place> inputPlaces = new ArrayList<>();
	protected final List<Place> outputPlaces = new ArrayList<>();

	public AbstractGatewayTranslation(TranslationContext translationContext, AbstractGateway gateway) {
		super(translationContext, gateway.getId());
	}

	@Override
	public Place getInitialPlace() {
		if (isSplit()) {
			return getInputPlaces().iterator().next();
		} else {
			throw new RuntimeException("This exclusive gateway (" + getPrefixString()
					+ ") is not a split and therefore does not have a single initial place");
		}
	}

	@Override
	public Place getFinalPlace() {
		if (isJoin()) {
			return getOutputPlaces().iterator().next();
		} else {
			throw new RuntimeException("This exclusive gateway (" + getPrefixString()
					+ ") is not a join and therefore does not have a single final place");
		}
	}

	protected boolean isSplit() {
		return getInputPlaces().size() == 1 && getOutputPlaces().size() > 1;
	}

	protected boolean isJoin() {
		return getInputPlaces().size() > 1 && getOutputPlaces().size() == 1;
	}

	public List<Place> getInputPlaces() {
		return inputPlaces;
	}

	public List<Place> getOutputPlaces() {
		return outputPlaces;
	}
}
