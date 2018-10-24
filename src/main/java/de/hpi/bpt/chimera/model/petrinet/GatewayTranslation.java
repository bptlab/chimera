package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.AbstractGateway;

public class GatewayTranslation extends AbstractTranslation {
	private final AbstractGateway gateway;

	public GatewayTranslation(TranslationContext translationContext, AbstractGateway gateway) {
		super(translationContext, gateway.getName());
		this.gateway = gateway;

		PetriNet petriNet = translationContext.getCaseModelTranslation().getPetriNet();
		final String prefixString = translationContext.getPrefixString();

		initialPlace = petriNet.addPlace(prefixString + "init");
		finalPlace = petriNet.addPlace(prefixString + "final");

		Transition transition = new Transition(prefixString);
		transition.addInputPlace(initialPlace);
		transition.addOutputPlace(finalPlace);
		petriNet.addTransition(transition);
	}
}
