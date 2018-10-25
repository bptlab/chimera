package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.AbstractGateway;

public class GatewayTranslation extends AbstractTranslation {

	public GatewayTranslation(TranslationContext translationContext, AbstractGateway gateway) {
		super(translationContext, gateway.getId());

		final String prefixString = this.context.getPrefixString();

		initialPlace = addPlace(prefixString + "init");
		finalPlace = addPlace(prefixString + "final");

		addTransition(prefixString, initialPlace, finalPlace);
	}
}
